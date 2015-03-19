package com.mark.dal;

import java.util.List;

import com.mark.model.FlightSearch;

public interface IFlightSearchDAL extends IFlightDAL {

	public FlightSearch saveFlightSearch(FlightSearch fs);
	
	public FlightSearch findFlightSavedSearch(FlightSearch fs);
	
	public List<FlightSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly);
	
}
