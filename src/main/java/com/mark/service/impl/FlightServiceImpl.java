package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.FlightDAL;
import com.mark.exception.FlightException;
import com.mark.model.FlightData;
import com.mark.model.FlightParsedData;
import com.mark.model.compare.FlightLowestPriceCompare;
import com.mark.model.compare.FlightLowestPriceForShortestTimeCompare;
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

@Service
public class FlightServiceImpl implements IFlightService {

	private String googleBaseUrl = FlightProperties.getProperty("google.flight.api.baseUrl");
	@Autowired
	private FlightDAL flightDAL;
	private static boolean debugMode;
	private static String apiKey = FlightProperties.getProperty("google.flight.api.key");
	static
	{
		debugMode = Boolean.valueOf(FlightProperties.getProperty("debugMode"));
	}
	
	@Override
	public String sayHi(String name) {
		return "Hello "+name+"!";
	}

	// For now return the mock data from the google docs
	@Override
	public FlightData getFlights(String from, String to, String date)
	{	
		FlightSavedSearch savedSearch = flightDAL.find(from, to, date);
		if ( savedSearch == null )
		{
			savedSearch = flightDAL.save(from, to, date);
		}
		// we have the saved search now. 
		// Check if we have the data for this search
		if ( savedSearch.isExistingSearch())
		{
			FlightData fd = flightDAL.findFlightData(savedSearch);
		}
		GoogleFlightResponse response = null;
		// if we are here, then we know that we need to call the API to get current data for today	
		if ( debugMode)
		{
			response = getTestResponse(); // for debugging, just return mock data instead of using api calls
		}
		else
		{
			// get the client we need to call Google
			IGoogleFlightClient client = RestClient.getClient(googleBaseUrl, IGoogleFlightClient.class);
			response = client.postForFlightInfo(apiKey, createRequest(savedSearch));
		}
		
		if (response != null)
		{
			// now for the fun part, parse the result
			List<FlightParsedData> listOfFlights = this.parseGoogleResponseToFlightData(response, savedSearch);
			// now get the prices we care about
			FlightData fd = this.getFlightData(listOfFlights);
			// need to save this!
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
		s.setDate(fss.getDate());
//		DepartureTime dt = new DepartureTime();
//		dt.setEarliestTime("05:00");
//		dt.setLatestTime("23:00");
//		s.setPermittedDepartureTime(dt);
		slices.add(s);
		gfr.setSlice(slices);
		gfr.setSolutions(20);
		gfr.setSaleCountry("US");
		request.setRequest(gfr);
		return request;
	}
}
