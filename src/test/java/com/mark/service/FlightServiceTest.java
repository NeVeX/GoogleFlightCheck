package com.mark.service;

import org.junit.Test;

import com.mark.service.impl.FlightServiceImpl;

public class FlightServiceTest {
	
	IFlightService flightService = new FlightServiceImpl();
	
	@Test
	public void test()
	{
		this.flightService.getFlights("blah");
	}

}
