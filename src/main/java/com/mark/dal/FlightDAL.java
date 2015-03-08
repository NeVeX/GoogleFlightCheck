package com.mark.dal;

import org.joda.time.DateTime;

import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

public interface FlightDAL {

	public FlightSavedSearch save(String origin, String destination, DateTime date);
	
	public FlightSavedSearch find(String origin, String destination, DateTime date);

	public FlightData findFlightData(FlightSavedSearch savedSearch);
	
	public boolean saveFlightData(FlightData fd);
}
