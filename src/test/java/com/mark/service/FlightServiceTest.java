package com.mark.service;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mark.model.FlightData;
import com.mark.model.FlightParsedData;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.impl.FlightServiceImpl;
import com.mark.util.FlightProperties;

public class FlightServiceTest {
	
	IFlightService flightService = new FlightServiceImpl();

	@Ignore
	public void testMockDataReturns()
	{
		FlightData fd = this.flightService.getFlights("test", "test", "test", "test");
		assertNotNull(fd);
	}
	

	@Ignore
	public void testCallRealGoogleAPI()
	{
		FlightData fd = this.flightService.getFlights("MARK", "DUB", "2015-02-23", null);
		assertNotNull(fd);
	}
	

	@Ignore
	public void testSaleTotal()
	{
		String price = "USD133.34";
		Float f = Float.valueOf(price.substring(3, price.length()));
		assertEquals(f, 133.34f, 0d);
	}

}
