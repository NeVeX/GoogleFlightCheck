package com.mark.dal;

import java.util.Date;
import java.util.List;

import com.mark.model.FlightSearch;

public interface IFlightSearchDAL extends IFlightDAL {

	public FlightSearch save(String origin, String destination, Date departureDate, Date returnDate);
	
	public FlightSearch find(String origin, String destination, Date departureDate, Date returnDate);
	
	public List<FlightSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly);
	
}
