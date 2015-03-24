package com.mark.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IFlightSearchDAL;
import com.mark.model.FlightSearch;
import com.mark.service.ITrackedFlightService;

@Service
public class TrackedFlightServiceImpl implements ITrackedFlightService {

	@Autowired
	private IFlightSearchDAL flightSearchDAL;
	
	@Override
	public List<FlightSearch> getTrackedFlights() {
		// TODO Auto-generated method stub
		List<FlightSearch> list = flightSearchDAL.getAllFlightSavedSearches(false);
		return list;
	}
	
	

}
