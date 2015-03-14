package com.mark.dal;

import java.util.List;

import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

public interface IFlightDataDAL extends IFlightDAL {
	
	public List<FlightData> getAllFlightData();
	
	public FlightData findFlightData(FlightSavedSearch savedSearch);
	
	public boolean saveFlightData(FlightData fd);

	public List<FlightSavedSearch> getFlightDataThatNeedsUpdating(List<FlightSavedSearch> savedSearches);

	public List<FlightData> getAllFlightData(FlightSavedSearch savedSearch);


}
