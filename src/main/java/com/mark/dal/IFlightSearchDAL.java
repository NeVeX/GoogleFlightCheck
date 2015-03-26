package com.mark.dal;

import java.util.List;

import com.mark.model.FlightInputSearch;
import com.mark.model.FlightSavedSearch;

public interface IFlightSearchDAL {

	public FlightSavedSearch saveFlightSearch(FlightInputSearch fs);
	
	public FlightSavedSearch getFlightSavedSearch(FlightInputSearch fs);
	
	public boolean updateFlightSavedSearch(FlightSavedSearch fs);
	
	public List<FlightSavedSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly);

	List<FlightSavedSearch> getFlightSearchesThatNeedsTrackingForToday(List<FlightSavedSearch> savedSearches);
	
}
