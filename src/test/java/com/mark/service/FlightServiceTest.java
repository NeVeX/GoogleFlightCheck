package com.mark.service;


import static org.junit.Assert.*;

import org.junit.Test;

import com.mark.model.FlightParsedData;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.impl.FlightServiceImpl;

public class FlightServiceTest {
	
	IFlightService flightService = new FlightServiceImpl();
	
	@Test
	public void testMockDataReturns()
	{
		FlightParsedData gfr = this.flightService.getFlights("test", "test", "test");
		assertNotNull(gfr);
	}
	
	@Test
	public void testCallRealGoogleAPI()
	{
		FlightParsedData gfr = this.flightService.getFlights("MARK", "DUB", "2015-02-23");
		assertNotNull(gfr);
	}
	
	@Test
	public void testSaleTotal()
	{
		String price = "USD133.34";
		Float f = Float.valueOf(price.substring(3, price.length()));
		assertEquals(f, 133.34f, 0d);
	}

}
