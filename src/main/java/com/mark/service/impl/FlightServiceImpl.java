package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IFlightDataDAL;
import com.mark.dal.IFlightSearchDAL;
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
import com.mark.util.client.RestClient;
import com.mark.util.client.type.IGoogleFlightClient;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.JsonConverter;

@Service
public class FlightServiceImpl implements IFlightService {

	private String googleBaseUrl = FlightProperties.getProperty("google.flight.api.baseUrl");
	@Autowired
	private IFlightSearchDAL flightSearchDAL;
	@Autowired
	private IFlightDataDAL flightDataDAL;
	@Autowired
	private IApplicationDAL applicationDAL;
	private IGoogleFlightClient client;
	private static String apiKey = FlightProperties.getProperty("google.flight.api.key");
	private static int flightCallLmit = Integer.valueOf(FlightProperties.getProperty("google.flight.api.daily.call.limit"));
	private AtomicLong flightCallCurrentCount = new AtomicLong(0);
	@PostConstruct
	public void setup()
	{
		ApplicationState appState = applicationDAL.getApplicationState();
		if ( appState != null )
		{
			flightCallCurrentCount = new AtomicLong(appState.getFlightApiCount());
		}
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
		appState.setDate(new LocalDate());
		appState.setFlightApiCount(flightCallCurrentCount.get());
		if ( async )
		{
			// TODO: implement this
		}
		applicationDAL.saveApplicationState(appState);
	}
	
	@Override
	public FlightData getFlights(String from, String to, String departureDateString, String returnDateString, Boolean forceBatchUsage)
	{
		LocalDate departureDate = DateConverter.toDate(departureDateString);
		LocalDate returnDate = DateConverter.toDate(returnDateString);
		return this.getFlights(from, to, departureDate, returnDate, forceBatchUsage);
	}
	
	public FlightData getFlights(String from, String to, LocalDate departureDate, LocalDate returnDate, Boolean forceBatchUsage)
	{	
		FlightSavedSearch savedSearch = flightSearchDAL.find(from, to, departureDate, returnDate);
		if ( savedSearch == null )
		{
			savedSearch = flightSearchDAL.save(from, to, departureDate, returnDate);
		}
		
		if ( forceBatchUsage != null && forceBatchUsage )
		{
			System.out.println("Will not get flight details since told to wait for batch process instead");
			return null;
		}
		
		// we have the saved search now. 
		// Check if we have the data for this search
		if ( savedSearch.isExistingSearch())
		{
			// find if we have data for today already
			FlightData fd = flightDataDAL.findFlightData(savedSearch);
			if ( fd != null )
			{
				// just return it
				System.out.println("Returning saved Flight Data instead of calling Flight API");
				return fd;
			}
		}
		GoogleFlightResponse response = null;
		// if we are here, then we know that we need to call the API to get current data for today	
		if ( flightCallCurrentCount.get() < flightCallLmit ) // check if our limit is reached
		{
			flightCallCurrentCount.incrementAndGet(); // increment by one
			response = client.postForFlightInfo(apiKey, createRequest(savedSearch));
		}
		else
		{
			System.err.println("The limit for today's Flight API call has being reached");
		}
		this.saveState(true); // save the state again -> async
		if (response != null)
		{
			// now for the fun part, parse the result
			List<FlightParsedData> listOfFlights = this.parseGoogleResponseToFlightData(response, savedSearch);
			// now get the prices we care about
			FlightData fd = this.getFlightData(listOfFlights);
			// need to save this!
			fd.setKey(savedSearch.getKey()); // save the key for this search too
			flightDataDAL.saveFlightData(fd);
			return fd;
		}
		System.err.println("Response from google flights is null. Cannot do anything with that.");
		return null;
	}
	
	private FlightData getFlightData(List<FlightParsedData> listOfFlights) {
		FlightData fd = new FlightData();
		Collections.sort(listOfFlights, new FlightLowestPriceCompare());
		FlightParsedData fpdCheap = listOfFlights.get(0);
		fd.setDepartureDate(fpdCheap.getDepartureDate());
		fd.setReturnDate(fpdCheap.getReturnDate());
		fd.setOrigin(fpdCheap.getOrigin());
		fd.setDestination(fpdCheap.getDestination());
		fd.setLowestPrice(fpdCheap.getPrice());
		fd.setLowestPriceTripDuration(new Long(fpdCheap.getTripLength()));
		Collections.sort(listOfFlights, new FlightLowestPriceForShortestTimeCompare());
		FlightParsedData fpdShortest = listOfFlights.get(0);
		fd.setShortestTimePrice(fpdShortest.getPrice());
		fd.setShortestTimePriceTripDuration(new Long(fpdShortest.getTripLength()));
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
			fd.setDepartureDate(fss.getDepartureDate());
			fd.setReturnDate(fss.getReturnDate());
			fd.setDestination(fss.getDestination());
			fd.setOrigin(fss.getOrigin());
			parsedData.add(fd);
		}
		return parsedData;
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
		s.setDate(DateConverter.toString(fss.getDepartureDate()));
		slices.add(s);
		gfr.setSlice(slices);
		gfr.setSolutions(20);
		gfr.setSaleCountry("US"); // hard code to America - all flights in USD
		request.setRequest(gfr);
		return request;
	}

	@Override
	public List<FlightSavedSearch> getAllFlightSavedSearches() {
		return flightSearchDAL.getAllFlightSavedSearches(false);
	}

	@Override
	public List<FlightData> getAllFlightData() {
		return flightDataDAL.getAllFlightData();
	}

	@Override
	public List<ApplicationState> getAllApplicationStates() {
		return applicationDAL.getAllApplicationStates();
	}

	@Override
	public void runUpdates() {
		System.out.println("Getting list of saved searches with departure dates in the future");
		List<FlightSavedSearch> savedSearches = flightSearchDAL.getAllFlightSavedSearches(true);
		if ( savedSearches != null && savedSearches.size() > 0)
		{
			System.out.println("Found ["+savedSearches.size()+"] saved searches with departure dates in the future - getting Flight Data with no search for today");
			// get all the flight searches that do not have updates for today
			List<FlightSavedSearch> needsUpdating = flightDataDAL.getFlightDataThatNeedsUpdating(savedSearches);
			if ( needsUpdating != null && needsUpdating.size() > 0 )
			{
				System.out.println("Found ["+needsUpdating.size()+"] searches that will be updated now");
				for (FlightSavedSearch fss : savedSearches)
				{
					this.getFlights(fss.getOrigin(), fss.getDestination(), fss.getDepartureDate(), fss.getReturnDate(), false);
					System.out.println("Updated Flight Data for: "+fss);
				}
			}
		}
	}
}
