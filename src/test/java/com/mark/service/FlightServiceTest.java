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

import com.google.appengine.api.datastore.KeyFactory;
import com.mark.dal.impl.FlightResultDAL;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
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
		FlightInputSearch fss = new FlightInputSearch();
		fss.setOrigin("SFO");
		fss.setDestination("DUB");
		fss.setDepartureDate(new LocalDate(2016,6,9).toDate());
		GoogleFlightRequest gfr = new FlightServiceImpl().createGoogleFlightRequest(fss);
		GoogleFlightResponse fd = this.googleApiClient.postForFlightInfo(gfr);
		assertNotNull(fd);
	}
	
	// Only use these to test various ad hoc things
	@Ignore
	public void testCallRealGoogleAPI_Return()
	{
		FlightInputSearch flight = new FlightInputSearch();
		flight.setOrigin("SFO");
		flight.setDestination("DUB");
		flight.setDepartureDate(new LocalDate(2015,12,19).toDate());
		flight.setReturnDate(new LocalDate(2015,12,30).toDate());
		GoogleFlightRequest gfr = new FlightServiceImpl().createGoogleFlightRequest(flight);
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
	
//	@Test
//	public void testMockFlightData()
//	{
//		FlightInputSearch fss = new FlightInputSearch();
//		fss.setOrigin("XXX"); // trigger for mock data return
//		List<FlightResult> list = new FlightResultDAL().getFlightSearchResultHistory(fss);
//		assertNotNull(list);
//		assertTrue(list.size() > 0);
//	}
	

}
