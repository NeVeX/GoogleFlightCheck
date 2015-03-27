package com.mark.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IFlightResultDAL;
import com.mark.dal.IFlightSearchDAL;
import com.mark.exception.FlightException;
import com.mark.exception.FlightException.FlightExceptionType;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightResult;
import com.mark.model.FlightSavedSearch;
import com.mark.model.FlightSearchHistoricalResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.algorithm.AlgorithmResult;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.request.Passengers;
import com.mark.model.google.request.Slice;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.algorithm.IFlightAlgorithm;
import com.mark.util.client.IGoogleFlightApiClient;
import com.mark.util.converter.DateConverter;

@Service
public class FlightServiceImpl implements IFlightService {

	@Autowired
	private IAdminService adminService;
	@Autowired
	private IFlightSearchDAL flightSearchDAL;
	@Autowired
	private IFlightResultDAL flightResultFAL;
	@Autowired
	private IGoogleFlightApiClient googleFlightApiClient;
	@Autowired
	private IFlightAlgorithm flightAlgorithm;
	
	private int TOTAL_FLIGHT_SOLUTIONS_TO_REQUEST = 100;
	
	@Override
	public FlightSearchHistoricalResult getFlightHistoricalResult(FlightInputSearch fis) {
		FlightSearchResult flightResult = null;
		try
		{
			flightResult = this.getFlightResult(fis);
		}
		catch(FlightException fe)
		{
			if ( fe.getExceptionType() != FlightExceptionType.GENERAL)
			{
				// get the history we know of this flight so far
				FlightSavedSearch fss = flightSearchDAL.getFlightSavedSearch(fis);
				if ( fss != null)
				{
					flightResult = new FlightSearchResult(new FlightResult(new LocalDate().toDate()), fss);
					flightResult.setMessage("The Flight API limit has being reached today, but here is the history of this flight thus far");	
				}
				// could not get the saved search, bad news...
				throw new FlightException("Could not get the history of flight search [] after initial search for today failed", FlightExceptionType.FLIGHT_SAVED_SEARCH_EXCEPTION);
			}
			else
			{
				throw fe;
			}
		}
		FlightSearchHistoricalResult historyResult = new FlightSearchHistoricalResult(flightResult);
		historyResult.setHistory(flightResultFAL.getFlightSearchResultHistory(flightResult.getFlightSearch()));
		return historyResult;
	}
	
	

	@Override
	public FlightSearchResult getFlightResult(FlightInputSearch inputSearch) {
		FlightSavedSearch flightSavedSearch = flightSearchDAL.getFlightSavedSearch(inputSearch);
		if ( flightSavedSearch == null)
		{
			// we don't have this flight so, save it
			flightSavedSearch = flightSearchDAL.saveFlightSearch(inputSearch);
		}

		if (inputSearch.getForceBatchUsage() != null && inputSearch.getForceBatchUsage()) {
			throw new FlightException("This search request will be batched instead of invoked immediately.");
		}
		
		return this.getFlightResult(flightSavedSearch);
	}


	@Override
	public FlightSearchResult getFlightResult(FlightSavedSearch flightSavedSearch) {
		if ( flightSavedSearch.getFlightOptionsExists() != null && !flightSavedSearch.getFlightOptionsExists())
		{
			// no flight options exists for this search, so don't continue
			throw new FlightException("No flight options exists for this search", FlightExceptionType.NO_FLIGHT_EXISTS_FOR_SEARCH);
		}
		
		// we have the saved search now.
		// Check if we have the data for this search
		if ( flightSavedSearch.getFlightOptionsExists() != null && flightSavedSearch.getFlightOptionsExists()) {
			// find if we have data for today already
			FlightSearchResult flightSearchResultToday = flightResultFAL.getFlightResultForToday(flightSavedSearch);
			if (flightSearchResultToday != null) {
				System.out.println("Found FlightSearchResult for today already stored, so using stored data instead of calling API");
				return flightSearchResultToday;
			}
		}
		GoogleFlightResponse apiResponse = this.getFlightAPIResult(flightSavedSearch);
		if (apiResponse != null) {
			// Use the setup algorithm to get results
			AlgorithmResult algoResult = flightAlgorithm.execute(flightSavedSearch, apiResponse);
			if ( algoResult != null )
			{
				// we have data to use, so create FlightInfo
				FlightResult fResult = new FlightResult(new LocalDate().toDate());
				fResult.setLowestPrice(algoResult.getLowestPrice());
				fResult.setLowestPriceTripDuration(algoResult.getLowestPriceTripDuration());
				fResult.setShortestTimePrice(algoResult.getShortestTimePrice());
				fResult.setShortestTimePriceTripDuration(algoResult.getShortestTimePriceTripDuration());
				FlightSearchResult flightResult = new FlightSearchResult(fResult, flightSavedSearch);
				// save the new data
				flightResultFAL.saveFlightSearchResult(flightResult);
				if ( flightSavedSearch.getFlightOptionsExists() == null || !flightSavedSearch.getFlightOptionsExists()) 
				{
					// this is the first time getting this flight info, so flag that there is data for this search
					flightSavedSearch.setFlightOptionsExists(true);
					this.flightSearchDAL.updateFlightSavedSearch(flightSavedSearch);
				}
				return flightResult;
			}
			throw new FlightException("The Flight Algorithm returned no results!", FlightExceptionType.FLIGHT_ALGORITHM_DID_NOT_WORK);
		}
		else
		{
			// save that this flight search does not have any results for it - to stop further unnessecary searches
			flightSavedSearch.setFlightOptionsExists(false);
			this.flightSearchDAL.updateFlightSavedSearch(flightSavedSearch);
		}	
		throw new FlightException("No results were returned from Google Flight API", FlightExceptionType.FLIGHT_API_RETURNED_NO_DATA);
	}
	
	private GoogleFlightResponse getFlightAPIResult(FlightSavedSearch flightSearch)
	{
		GoogleFlightResponse response = null;
		// if we are here, then we know that we need to call the API to get current data for today
		if (adminService.isAllowedToCallFlightAPI()) // check if our limit is reached
		{
			adminService.incrementFlightAPICount(); // increment it
			try
			{
				response = googleFlightApiClient.postForFlightInfo(createGoogleFlightRequest(flightSearch));
			}
			catch(Exception e)
			{
				throw new FlightException("There was a problem calling the FlightAPI", e, FlightExceptionType.FLIGHT_API_EXCEPTION);
			}
		} 
		else {
			// can't call the Flight API again, but get the history so far for this search (if any)
			String msg = "The limit for today's Flight API call has being reached";
			throw new FlightException(msg, FlightExceptionType.FLIGHT_API_LIMIT_REACHED);
		}
		return response;
	}	

	public GoogleFlightRequest createGoogleFlightRequest(FlightInputSearch flightSearch) {
		GoogleFlightRequest request = new GoogleFlightRequest();
		GoogleFlightRequestDetail gfr = new GoogleFlightRequestDetail();
		Passengers p = new Passengers();
		p.setAdultCount(1);
		p.setChildCount(0);
		gfr.setPassengers(p);
		List < Slice > slices = new ArrayList < > ();
		Slice departSlice = new Slice();
		departSlice.setOrigin(flightSearch.getOrigin());
		departSlice.setDestination(flightSearch.getDestination());
		departSlice.setDate(DateConverter.toString(flightSearch.getDepartureDate()));
		slices.add(departSlice);
		if ( flightSearch.getReturnDate() != null )
		{
			// Just flip the original flight around (origin <--> destination)
			Slice returnSlice = new Slice();
			returnSlice.setOrigin(flightSearch.getDestination());
			returnSlice.setDestination(flightSearch.getOrigin());
			returnSlice.setDate(DateConverter.toString(flightSearch.getReturnDate()));
			slices.add(returnSlice);
		}
		gfr.setSlice(slices);
		gfr.setSolutions(TOTAL_FLIGHT_SOLUTIONS_TO_REQUEST);
		gfr.setSaleCountry("US"); // hard code to America - all flights in USD
		request.setRequest(gfr);
		return request;
	}



	@Override
	public List<FlightSearchResult> getAllFlightSearchResults() {
		// get all flight searches first
		List<FlightSavedSearch> savedSearches = flightSearchDAL.getAllFlightSavedSearches(true);
		List<FlightSearchResult> searchResults = new ArrayList<FlightSearchResult>();
		if ( savedSearches != null)
		{
			for(FlightSavedSearch fss : savedSearches)
			{
				List<FlightResult> flightResults = flightResultFAL.getFlightSearchResultHistory(fss);
				if ( flightResults != null)
				{
					for ( FlightResult fr : flightResults)
					{
						searchResults.add(new FlightSearchResult(fr, fss));
					}
				}
			}
		}
		return searchResults;
	}

}