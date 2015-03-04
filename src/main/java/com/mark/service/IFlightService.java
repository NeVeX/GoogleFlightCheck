package com.mark.service;

import com.mark.model.FlightData;
import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {
	
	public String sayHi(String name);

	public FlightData getFlights(String from, String to, String date);
}
