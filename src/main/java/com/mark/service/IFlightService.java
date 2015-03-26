package com.mark.service;

import java.util.List;

import com.mark.model.ApplicationState;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightSearchHistoricalResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {

	FlightSearchResult getFlightResult(FlightInputSearch inputSearch);
	
	FlightSearchResult getFlightResult(FlightSavedSearch savedSearch);

	FlightSearchHistoricalResult getFlightHistoricalResult(FlightInputSearch fis);

	List<FlightSearchResult> getAllFlightSearchResults();
}
