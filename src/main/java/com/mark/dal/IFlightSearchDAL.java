package com.mark.dal;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

public interface IFlightSearchDAL extends IFlightDAL {

	public FlightSavedSearch save(String origin, String destination, LocalDate departureDate, LocalDate returnDate);
	
	public FlightSavedSearch find(String origin, String destination, LocalDate departureDate, LocalDate returnDate);
	
	public List<FlightSavedSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly);
	
}
