package com.mark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IFlightSearchDAL;
import com.mark.model.FlightSavedSearch;
import com.mark.service.IFlightTrackerService;

@Service
public class FlightTrackerServiceImpl implements IFlightTrackerService {

	@Autowired
	private IFlightSearchDAL flightSearchDAL;
	
	@Override
	public List<FlightSavedSearch> getTrackedFlights() {
		// TODO Auto-generated method stub
		List<FlightSavedSearch> list = flightSearchDAL.getAllFlightSavedSearches(false);
		return list;
	}
	
	

}
