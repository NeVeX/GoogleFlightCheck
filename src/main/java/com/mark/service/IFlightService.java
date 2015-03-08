package com.mark.service;

import java.util.List;

import com.mark.model.FlightData;
import com.mark.model.FlightParsedData;
import com.mark.model.dal.ApplicationState;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {

	public FlightData getFlights(String from, String to, String departureDateString, String returnDateString);

	public List<FlightSavedSearch> getAllFlightSavedSearches();

	public List<FlightData> getAllFlightData();

	public List<ApplicationState> getAllApplicationStates();
}
