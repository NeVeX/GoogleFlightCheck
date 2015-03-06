package com.mark.dal;

import com.google.appengine.api.datastore.Key;
import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

public interface FlightDAL {

	public FlightSavedSearch save(String origin, String destination, String date);
	
	public FlightSavedSearch find(String origin, String destination, String date);

	public FlightData findFlightData(FlightSavedSearch savedSearch);
	
	public boolean saveFlightData(FlightData fd);
}
