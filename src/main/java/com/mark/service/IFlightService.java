package com.mark.service;

import java.util.List;

import com.mark.model.ApplicationState;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightSearchHistoricalResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {

	/**
	 * For the given input search, return the results (invoking API) for that search only
	 * @param inputSearch
	 * @return
	 */
	FlightSearchResult getFlightSearchResult(FlightInputSearch inputSearch);

	/**
	 * For the given input search, return the results (invoking API) for this search and include
	 * all historical searches before too (if exists)
	 * @param fis
	 * @return
	 */
	FlightSearchHistoricalResult getFlightSearchHistoricalResult(FlightInputSearch fis);
	
	/**
	 * Given the input search, return all the stored flight results for this search
	 * @param fis
	 * @return
	 */
	FlightSearchHistoricalResult getFlightSearchHistoricalStoredResults(FlightInputSearch fis);
	
	/**
	 * Return all Flight search results so far
	 * @return
	 */
	List<FlightSearchResult> getAllFlightSearchResults();
}
