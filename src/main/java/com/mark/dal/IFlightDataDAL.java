package com.mark.dal;

import java.util.List;

import com.mark.model.FlightData;
import com.mark.model.FlightSearch;

public interface IFlightDataDAL extends IFlightDAL {
	
	public List<FlightData> getAllFlightData();
	
	public FlightData findFlightData(FlightSearch savedSearch);
	
	public boolean saveFlightData(FlightData fd);

	public List<FlightSearch> getFlightDataThatNeedsUpdating(List<FlightSearch> savedSearches);

	public List<FlightData> getAllFlightData(FlightSearch savedSearch);


}
