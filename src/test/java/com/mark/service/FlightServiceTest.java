package com.mark.service;


import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mark.dal.impl.FlightInfoDALImpl;
import com.mark.model.FlightInfo;
import com.mark.model.FlightParsedData;
import com.mark.model.FlightSearch;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.impl.FlightServiceImpl;
import com.mark.util.FlightProperties;
import com.mark.util.client.IGoogleFlightApiClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring/spring-mvc-test-google-client.xml")
public class FlightServiceTest {
	@Autowired
	IGoogleFlightApiClient googleApiClient;

	// Only use these to test various ad hoc things
	@Ignore
	public void testCallRealGoogleAPI_OneWay()
	{
		FlightSearch fss = new FlightSearch();
		fss.setOrigin("SFO");
		fss.setDestination("DUB");
		fss.setDepartureDate(new LocalDate(2016,6,9).toDate());
		GoogleFlightRequest gfr = new FlightServiceImpl().createRequest(fss);
		GoogleFlightResponse fd = this.googleApiClient.postForFlightInfo(gfr);
		assertNotNull(fd);
	}
	
	// Only use these to test various ad hoc things
	@Ignore
	public void testCallRealGoogleAPI_Return()
	{
		FlightSearch departFlight = new FlightSearch();
		departFlight.setOrigin("SFO");
		departFlight.setDestination("DUB");
		departFlight.setDepartureDate(new LocalDate(2015,12,19).toDate());

		FlightSearch returnFlight = new FlightSearch();
		returnFlight.setOrigin("DUB");
		returnFlight.setDestination("SFO");
		returnFlight.setDepartureDate(new LocalDate(2015,12,30).toDate());
		
		
		GoogleFlightRequest gfr = new FlightServiceImpl().createRequest(departFlight, returnFlight);
		GoogleFlightResponse fd = this.googleApiClient.postForFlightInfo(gfr);
		assertNotNull(fd);
	}

	@Ignore
	public void testSaleTotal()
	{
		String price = "USD133.34";
		Float f = Float.valueOf(price.substring(3, price.length()));
		assertEquals(f, 133.34f, 0d);
	}
	
	@Test
	public void testMockFlightData()
	{
		FlightSearch fss = new FlightSearch();
		fss.setOrigin("XXX"); // trigger for mock data return
		List<FlightInfo> list = new FlightInfoDALImpl().getAllSavedFlightInfo(fss);
		assertNotNull(list);
		assertTrue(list.size() > 0);
	}
	

}
