package com.mark.dal;

import java.util.List;

import com.mark.model.FlightInfo;
import com.mark.model.FlightSearch;

public interface IFlightInfoDAL extends IFlightDAL {
	
	public List<FlightInfo> getAllSavedFlightInfo();
	
	public FlightInfo findFlightInfo(FlightSearch savedSearch);
	
	public boolean saveFlightInfo(FlightInfo fd);

	public List<FlightSearch> getFlightInfoThatNeedsTracking(List<FlightSearch> savedSearches);

	public List<FlightInfo> getAllSavedFlightInfo(FlightSearch savedSearch);


}
