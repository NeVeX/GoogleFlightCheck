package com.mark.service;

import java.util.List;

import com.mark.model.FlightSavedSearch;

public interface IFlightTrackerService {
	
	List<FlightSavedSearch> getTrackedFlights();

}
