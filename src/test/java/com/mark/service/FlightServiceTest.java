package com.mark.service;


import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mark.model.FlightData;
import com.mark.model.FlightParsedData;
import com.mark.model.dal.FlightSavedSearch;
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

	@Test
	public void testCallRealGoogleAPI()
	{
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setOrigin("SFO");
		fss.setDestination("DUB");
		fss.setDepartureDate(new LocalDate(2016,6,9).toDate());
		GoogleFlightRequest gfr = new FlightServiceImpl().createRequest(fss);
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

}
