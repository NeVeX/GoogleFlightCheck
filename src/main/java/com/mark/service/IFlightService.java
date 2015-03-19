package com.mark.service;

import java.util.List;

import com.mark.model.FlightInfo;
import com.mark.model.FlightParsedData;
import com.mark.model.FlightSearch;
import com.mark.model.dal.ApplicationState;
import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {

	public List<FlightSearch> getAllFlightSavedSearches();

	public List<FlightInfo> getAllSavedFlightInfo();

	public List<ApplicationState> getAllApplicationStates();

	/**
	 * Determine all the flight info that needs to be updated...and get perform those updates
	 */
	public void runTracker();

	public FlightInfo getFlightInfo(FlightSearch flightData);
}
