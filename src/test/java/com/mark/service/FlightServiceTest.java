package com.mark.service;


import static org.junit.Assert.*;

import org.junit.Test;

import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.impl.FlightServiceImpl;

public class FlightServiceTest {
	
	IFlightService flightService = new FlightServiceImpl();
	
	@Test
	public void testMockDataReturns()
	{
		GoogleFlightResponse gfr = this.flightService.getFlights("test", "test", "test");
		assertNotNull(gfr);
	}
	
	@Test
	public void testCallRealGoogleAPI()
	{
		GoogleFlightResponse gfr = this.flightService.getFlights("MARK", "DUB", "2015-02-23");
		assertNotNull(gfr);
	}

}
