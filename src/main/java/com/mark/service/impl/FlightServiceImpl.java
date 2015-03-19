package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IFlightInfoDAL;
import com.mark.dal.IFlightSearchDAL;
import com.mark.dal.IApplicationDAL;
import com.mark.exception.FlightException;
import com.mark.model.FlightInfo;
import com.mark.model.FlightParsedData;
import com.mark.model.FlightSearch;
import com.mark.model.compare.FlightLowestPriceCompare;
import com.mark.model.compare.FlightLowestPriceForShortestTimeCompare;
import com.mark.model.dal.ApplicationState;
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
import com.mark.util.client.IGoogleFlightApiClient;
import com.mark.util.client.type.resteasy.IRestEasyGoogleFlightApiClient;
import com.mark.util.client.type.resteasy.RestEasyClient;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.JsonConverter;

@Service
public class FlightServiceImpl implements IFlightService {

	@Autowired
	private IFlightSearchDAL flightSearchDAL;@Autowired
	private IFlightInfoDAL flightDataDAL;@Autowired
	private IApplicationDAL applicationDAL;@Autowired
	private IGoogleFlightApiClient googleFlightApiClient;
	private AtomicLong flightCallCurrentCount = new AtomicLong(0);

	@PostConstruct
	public void setup() {
		ApplicationState appState = applicationDAL.getApplicationState();
		if (appState != null) {
			flightCallCurrentCount = new AtomicLong(
			appState.getFlightApiCount());
		}
	}

	// TODO: Make async call possible
	private void saveState(boolean async) {
		ApplicationState appState = new ApplicationState();
		appState.setDate(new LocalDate());
		appState.setFlightApiCount(flightCallCurrentCount.get());
		applicationDAL.saveApplicationState(appState);
	}

	@Override
	public FlightInfo getFlightInfo(FlightSearch fs) {
		FlightInfo fd = this.getFlightInfoWithoutHistory(fs);
		fd.setHistory(this.getAllFlightDataForSearch(fd));
		return fd;
	}

	private FlightInfo getFlightInfoWithoutHistory(FlightSearch fs) {
		fs.setDestination(fs.getDestination().toUpperCase());
		fs.setOrigin(fs.getOrigin().toUpperCase());
		FlightSearch savedSearch = flightSearchDAL.findFlightSavedSearch(fs);
		if (savedSearch == null) {
			savedSearch = flightSearchDAL.saveFlightSearch(fs);
		}

		if (fs.getForceBatchUsage() != null && fs.getForceBatchUsage()) {
			throw new FlightException("Will not get flight details since told to wait for batch process instead");
		}

		// we have the saved search now.
		// Check if we have the data for this search
		if (savedSearch.isExistingSearch() != null && savedSearch.isExistingSearch()) {
			// find if we have data for today already
			FlightInfo fd = flightDataDAL.findFlightInfo(savedSearch);
			if (fd != null) {
				String s = "Returning saved Flight Data instead of calling Flight API";
				// just return it
				System.out.println(s);
				fd.setMessage(s);
				return fd;
			}
		}
		GoogleFlightResponse response = null;
		// if we are here, then we know that we need to call the API to get
		// current data for today
		if (flightCallCurrentCount.get() < FlightProperties.GOOGLE_FLIGHT_API_DAILY_INVOKE_LIMIT) // check if  our limit is reached
		{
			flightCallCurrentCount.incrementAndGet(); // increment by one
			response = googleFlightApiClient.postForFlightInfo(createRequest(savedSearch));
		} else {
			throw new FlightException(
				"The limit for today's Flight API call has being reached");
		}
		this.saveState(true); // save the state again -> async

		if (response != null) {
			// now for the fun part, parse the result
			List < FlightParsedData > listOfFlights = this.parseGoogleResponseToFlightData(response, savedSearch);
			if (listOfFlights != null && listOfFlights.size() > 0) {
				// now get the prices we care about
				FlightInfo fd = this.getFlightData(listOfFlights);
				if (fd != null) {
					// need to save this!
					fd.setKey(savedSearch.getKey()); // save the key for this
					// search too
					fd.setExistingSearch(savedSearch.isExistingSearch());
					flightDataDAL.saveFlightInfo(fd);
					return fd;
				}
				throw new FlightException("Found flight information but could not parse details from the objects");
			}
			throw new FlightException("Did not find any flights from Google API response");
		}
		throw new FlightException("Response from google flights is null. Cannot do anything with no data! :-(.");
	}

	private List<FlightInfo> getAllFlightDataForSearch(FlightSearch savedSearch) {
		return flightDataDAL.getAllSavedFlightInfo(savedSearch);
	}

	private FlightInfo getFlightData(List<FlightParsedData> listOfFlights) {
		FlightInfo fd = new FlightInfo();
		Collections.sort(listOfFlights, new FlightLowestPriceCompare());
		FlightParsedData fpdCheap = listOfFlights.get(0);
		fd.setDepartureDate(fpdCheap.getDepartureDate());
		fd.setReturnDate(fpdCheap.getReturnDate());
		fd.setOrigin(fpdCheap.getOrigin());
		fd.setDestination(fpdCheap.getDestination());
		fd.setLowestPrice(fpdCheap.getPrice());
		fd.setLowestPriceTripDuration(new Long(fpdCheap.getTripLength()));
		Collections.sort(listOfFlights,
		new FlightLowestPriceForShortestTimeCompare());
		FlightParsedData fpdShortest = listOfFlights.get(0);
		fd.setShortestTimePrice(fpdShortest.getPrice());
		fd.setShortestTimePriceTripDuration(new Long(fpdShortest.getTripLength()));
		return fd;
	}

	private List<FlightParsedData> parseGoogleResponseToFlightData(GoogleFlightResponse response, FlightSearch fss) {
		List<FlightParsedData> parsedData = new ArrayList < FlightParsedData > ();
		if (response == null || response.getTrips() == null || response.getTrips().getTripOption() == null) {
			throw new FlightException("No flight information returned for search query");
		}
		Trip trip = response.getTrips();
		for (TripOption t: trip.getTripOption()) {
			float price = Float.valueOf(t.getSaleTotal().substring(3,
			t.getSaleTotal().length()));
			int stops = 0;
			boolean foundFlightData = false;
			int tripLength = 0;
			if (t.getSlice() != null) {
				for (ResponseSlice rs: t.getSlice()) {
					if (rs.getSegment() != null) {
						for (Segment seg: rs.getSegment()) {
							if (seg.getLeg() != null) {
								for (Leg leg: seg.getLeg()) {
									stops++;
									tripLength += leg.getDuration() != null ? leg.getDuration() : 0;
									foundFlightData = true;
								}
							}
						}
					}
				}
			}
			if (foundFlightData) {
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
			} else {
				System.out.println("Info: Did not find flight data in Trip Options.");
			}
		}
		return parsedData;
	}

	public GoogleFlightRequest createRequest(FlightSearch fss) {
		GoogleFlightRequest request = new GoogleFlightRequest();
		GoogleFlightRequestDetail gfr = new GoogleFlightRequestDetail();
		Passengers p = new Passengers();
		p.setAdultCount(1);
		p.setChildCount(0);
		gfr.setPassengers(p);
		List < Slice > slices = new ArrayList < > ();
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
	public List < FlightSearch > getAllFlightSavedSearches() {
		return flightSearchDAL.getAllFlightSavedSearches(false);
	}

	@Override
	public List <FlightInfo> getAllSavedFlightInfo() {
		return flightDataDAL.getAllSavedFlightInfo();
	}

	@Override
	public List < ApplicationState > getAllApplicationStates() {
		return applicationDAL.getAllApplicationStates();
	}

	@Override
	public void runTracker() {
		System.out.println("Batch Job: Getting list of saved searches with departure dates in the future");
		List < FlightSearch > savedSearches = flightSearchDAL.getAllFlightSavedSearches(true);
		if (savedSearches != null && savedSearches.size() > 0) {
			System.out.println("Batch Job: Found [" + savedSearches.size() + "] saved searches with departure dates in the future - getting Flight Data with no search for today");
			// get all the flight searches that do not have updates for today
			List < FlightSearch > needsUpdating = flightDataDAL.getFlightInfoThatNeedsTracking(savedSearches);
			if (needsUpdating != null && needsUpdating.size() > 0) {
				System.out.println("Batch Job: Found [" + needsUpdating.size() + "] searches that will be updated now");
				for (FlightSearch fss: needsUpdating) {
					try {
						System.out.println("Batch Job: Attempting to update Flight Data for: " + fss);
						this.getFlightInfoWithoutHistory(fss);
						System.out.println("Updated Flight Data for: " + fss);
					} catch (Exception e) {
						System.err.println("Batch Job: Could not update flight details for [" + fss + "].\n" + e);
					}
				}
			}
		}
		System.out.println("Batch Job: Finished.");
	}

}