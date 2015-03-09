package com.mark.util;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.gson.Gson;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.request.Slice;
import com.mark.service.impl.FlightServiceImpl;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.TimeConverter;

public class UtilTest {

	@Test
	public void testPropertiesLoad()
	{
		String url = FlightProperties.getProperty("google.flight.api.baseUrl");
		assertTrue(url != null && url.length() > 0);
		String apiKey = FlightProperties.getProperty("google.flight.api.key");
		assertTrue(apiKey != null && apiKey.length() > 0);
	}
	
	@Test
	public void testRequestParams()
	{
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDepartureDate(DateConverter.convertToDateTime("2014-10-19"));
		fss.setDestination("dest");
		fss.setOrigin("origin");
		GoogleFlightRequest gfr = new FlightServiceImpl().createRequest(fss);
		Gson gson = new Gson();
		String s = gson.toJson(gfr);//, new TypeToken<List<Slice>>() {}.getType());
		assertTrue(s != null && s.length() > 0);
	}
	
	@Test
	public void testDateConverter()
	{
		String date = "2014-10-20";
		DateTime dt = DateConverter.convertToDateTime(date);
		assertDateIsAsExpected(dt, 2014, 10, 20);
		date = "20-10-2014";
		dt = DateConverter.convertToDateTime(date);
		assertNull(dt);
		date = null;
		dt = DateConverter.convertToDateTime(date);
		assertNull(dt);
		date = "";
		dt = DateConverter.convertToDateTime(date);
		assertNull(dt);
		dt = new DateTime(2015, 9, 6, 0, 0);
		String dateShouldMatch = "2015-09-06";
		date = DateConverter.convertToString(dt);
		assertEquals(dateShouldMatch, date);
		date = DateConverter.convertToString(null);
		assertEquals(date, "");
		
	}
	
	@Test
	public void testTimeConverter()
	{
		String expected = "0d 0h 30m";
		Long time = 30L;
		assertEquals(expected, TimeConverter.convertToTimeString(time));
		expected = "0d 2h 10m";
		time = 130L;
		assertEquals(expected, TimeConverter.convertToTimeString(time));
		expected = "1d 3h 40m";
		time = 1660L;
		assertEquals(expected, TimeConverter.convertToTimeString(time));
		expected = "1d 0h 2m";
		time = 1442L;
		assertEquals(expected, TimeConverter.convertToTimeString(time));
		time = null;
		assertEquals("", TimeConverter.convertToTimeString(time));
	}
	
	private void assertDateIsAsExpected(DateTime dt, int year, int month, int day)
	{
		assertEquals(dt.getDayOfMonth(), day);
		assertEquals(dt.getMonthOfYear(), month);
		assertEquals(dt.getYear(), year);
	}
	
}
