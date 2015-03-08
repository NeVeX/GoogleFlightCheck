package com.mark.dal;

import java.util.List;

import org.joda.time.DateTime;

import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

public interface FlightDAL {

	public FlightSavedSearch save(String origin, String destination, DateTime departureDate, DateTime returnDate);
	
	public FlightSavedSearch find(String origin, String destination, DateTime departureDate, DateTime returnDate);

	public FlightData findFlightData(FlightSavedSearch savedSearch);
	
	public boolean saveFlightData(FlightData fd);

	public List<FlightSavedSearch> getAllFlightSavedSearches();

	public List<FlightData> getAllFlightData();
}
