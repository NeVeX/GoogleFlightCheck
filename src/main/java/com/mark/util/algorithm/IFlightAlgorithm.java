package com.mark.util.algorithm;

import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.model.algorithm.AlgorithmResult;
import com.mark.model.google.response.GoogleFlightResponse;

public interface IFlightAlgorithm {

	public AlgorithmResult execute(FlightSavedSearch flightSearch, GoogleFlightResponse googleFlightResponse);
	
}
