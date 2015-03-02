package com.mark.service;

import com.mark.model.google.response.GoogleFlightResponse;


public interface IFlightService {
	
	public String sayHi(String name);
	
	public GoogleFlightResponse getFlights(String information);
}
