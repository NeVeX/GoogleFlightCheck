package com.mark.dal;

import java.util.List;

import com.mark.model.FlightResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;

public interface IFlightResultDAL {
	
	FlightSearchResult getFlightResultForToday(FlightSavedSearch savedSearch);
	
	boolean saveFlightSearchResult(FlightSearchResult fd);

	List<FlightResult> getFlightSearchResultHistory(FlightSavedSearch savedSearch);

}
