package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.FlightDAL;
import com.mark.dal.IApplicationDAL;
import com.mark.exception.FlightException;
import com.mark.model.FlightData;
import com.mark.model.FlightParsedData;
import com.mark.model.compare.FlightLowestPriceCompare;
import com.mark.model.compare.FlightLowestPriceForShortestTimeCompare;
import com.mark.model.dal.ApplicationState;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.model.google.request.DepartureTime;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.request.Passengers;
import com.mark.model.google.request.Slice;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.model.google.response.Leg;
import com.mark.model.google.response.ResponseSlice;
import com.mark.model.google.response.Segment;
import com.mark.model.google.response.Trip;
import com.mark.model.google.response.TripOption;
import com.mark.service.IFlightService;
import com.mark.util.FlightProperties;
import com.mark.util.JsonConverter;
import com.mark.util.client.RestClient;
import com.mark.util.client.type.IGoogleFlightClient;
import com.mark.util.converter.DateConverter;

@Service
public class FlightServiceImpl implements IFlightService {

	private String googleBaseUrl = FlightProperties.getProperty("google.flight.api.baseUrl");
	@Autowired
	private FlightDAL flightDAL;
	@Autowired
	private IApplicationDAL applicationDAL;
	private IGoogleFlightClient client;
	private static boolean debugMode = Boolean.valueOf(FlightProperties.getProperty("debugMode"));
	private static String apiKey = FlightProperties.getProperty("google.flight.api.key");
	private static int flightCallLmit = Integer.valueOf(FlightProperties.getProperty("google.flight.api.daily.call.limit"));
	private AtomicInteger flightCallCurrentCount = new AtomicInteger(0);
	@PostConstruct
	public void setup()
	{
		ApplicationState appState = applicationDAL.getApplicationState();
		flightCallCurrentCount = new AtomicInteger(appState.getFlightApiCount());
		// get the client we need to call Google
		client = RestClient.getClient(googleBaseUrl, IGoogleFlightClient.class);
	}
	
	@PreDestroy
	public void preDestroy()
	{
		this.saveState(false);
	}
	
	@Override
	public void finalize(){
		this.saveState(false);
	}
	
	
	private void saveState(boolean async)
	{
		ApplicationState appState = new ApplicationState();
		appState.setDate(new DateTime());
		appState.setFlightApiCount(flightCallCurrentCount.get());
		if ( async )
		{
			// TODO: implement this
		}
		
		applicationDAL.saveApplicationState(appState);
	}
	
	@Override
	public String sayHi(String name) {
		return "Hello "+name+"!";
	}

	// For now return the mock data from the google docs
	@Override
	public FlightData getFlights(String from, String to, String dateString)
	{	
		DateTime dt = DateConverter.convertToDateTime(dateString);
		FlightSavedSearch savedSearch = flightDAL.find(from, to, dt);
		if ( savedSearch == null )
		{
			savedSearch = flightDAL.save(from, to, dt);
		}
		// we have the saved search now. 
		// Check if we have the data for this search
		if ( savedSearch.isExistingSearch())
		{
			FlightData fd = flightDAL.findFlightData(savedSearch);
			if ( fd != null )
			{
				// just return it
				System.out.println("Returning saved Flight Data instead of calling Flight API");
				return fd;
			}
		}
		GoogleFlightResponse response = null;
		// if we are here, then we know that we need to call the API to get current data for today	
		if ( debugMode)
		{
			response = getTestResponse(); // for debugging, just return mock data instead of using api calls
		}
		else
		{
			// check if our limit is reached
			if ( flightCallCurrentCount.get() < flightCallLmit )
			{
				flightCallCurrentCount.incrementAndGet(); // increment by one
				response = client.postForFlightInfo(apiKey, createRequest(savedSearch));
			}
			else
			{
				System.err.println("The limit for today's Flight API call has being reached");
			}
		}
		// save the state again -> async
		this.saveState(true);
		if (response != null)
		{
			// now for the fun part, parse the result
			List<FlightParsedData> listOfFlights = this.parseGoogleResponseToFlightData(response, savedSearch);
			// now get the prices we care about
			FlightData fd = this.getFlightData(listOfFlights);
			// need to save this!
			fd.setKey(savedSearch.getKey()); // save the key for this search too
			flightDAL.saveFlightData(fd);
			
			return fd;
		}
		System.err.println("Response from google flights is null. Cannot do anything with that.");
		return null;
	}
	
	private FlightData getFlightData(List<FlightParsedData> listOfFlights) {
		FlightData fd = new FlightData();
		Collections.sort(listOfFlights, new FlightLowestPriceCompare());
		FlightParsedData fpdCheap = listOfFlights.get(0);
		fd.setDate(fpdCheap.getDate());
		fd.setOrigin(fpdCheap.getOrigin());
		fd.setDestination(fpdCheap.getDestination());
		fd.setLowestPrice(fpdCheap.getPrice());
		Collections.sort(listOfFlights, new FlightLowestPriceForShortestTimeCompare());
		FlightParsedData fpdShortest = listOfFlights.get(0);
		fd.setShortestTimePrice(fpdShortest.getPrice());
		return fd;
	}

	private List<FlightParsedData> parseGoogleResponseToFlightData(GoogleFlightResponse response, FlightSavedSearch fss) {
		Trip trip = response.getTrips();
		List<FlightParsedData> parsedData = new ArrayList<FlightParsedData>();
		for (TripOption t : trip.getTripOption())
		{
			float price = Float.valueOf(t.getSaleTotal().substring(3, t.getSaleTotal().length()));
			int stops = 0;
			int tripLength = Integer.valueOf(0);
			for (ResponseSlice rs : t.getSlice())
			{
				
				for (Segment seg : rs.getSegment())
				{
					for(Leg leg : seg.getLeg())
					{
						stops++;
						tripLength += leg.getDuration() != null ? leg.getDuration() : 0;
					}
				}
			}
			// create the FlightData object
			FlightParsedData fd = new FlightParsedData();
			fd.setPrice(price);
			fd.setNumberOfStops(stops);
			fd.setTripLength(tripLength);
			fd.setDate(fss.getDate());
			fd.setDestination(fss.getDestination());
			
			fd.setOrigin(fss.getOrigin());
			parsedData.add(fd);
		}
		
		return parsedData;
	}

	private GoogleFlightResponse getTestResponse()
	{
		String testDataLocation = "data/example_flight_response.json";
		try( InputStream in = this.getClass().getClassLoader().getResourceAsStream(testDataLocation))
		{
			return JsonConverter.getObjectFromJson(in, GoogleFlightResponse.class);
		}
		catch (Exception e) {
			throw new FlightException("Could not load test data in inputstream from location [%s]", null, testDataLocation);
		}	
	}
	
	public GoogleFlightRequest createRequest(FlightSavedSearch fss)
	{
		GoogleFlightRequest request = new GoogleFlightRequest();
		GoogleFlightRequestDetail gfr = new GoogleFlightRequestDetail();
		Passengers p = new Passengers();
		p.setAdultCount(1);
		p.setChildCount(0);
		gfr.setPassengers(p);
		List<Slice> slices = new ArrayList<>();
		Slice s = new Slice();
		s.setOrigin(fss.getOrigin());
		s.setDestination(fss.getDestination());
		s.setDate(DateConverter.convertToString(fss.getDate()));
		slices.add(s);
		gfr.setSlice(slices);
		gfr.setSolutions(20);
		gfr.setSaleCountry("US"); // hard code to America - all flights in USD
		request.setRequest(gfr);
		return request;
	}
}
