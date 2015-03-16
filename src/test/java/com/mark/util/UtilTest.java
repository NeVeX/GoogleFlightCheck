package com.mark.util;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
		assertTrue(FlightProperties.GOOGLE_FLIGHT_API_BASE_URL != null && FlightProperties.GOOGLE_FLIGHT_API_BASE_URL.length() > 0);
		assertTrue(FlightProperties.GOOGLE_FLIGHT_API_KEY != null && FlightProperties.GOOGLE_FLIGHT_API_KEY.length() > 0);
	}
	
	@Test
	public void testRequestParams()
	{
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDepartureDate(DateConverter.toDate("2014-10-19"));
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
		Date dt = DateConverter.toDate(date);
		assertDateIsAsExpected(dt, 2014, 10, 20);
		date = "20-10-2014";
		dt = DateConverter.toDate(date);
		assertNull(dt);
		date = null;
		dt = DateConverter.toDate(date);
		assertNull(dt);
		date = "";
		dt = DateConverter.toDate(date);
		assertNull(dt);
		dt = new LocalDate(2015, 9, 6).toDate();
		String dateShouldMatch = "2015-09-06";
		date = DateConverter.toString(dt);
		assertEquals(dateShouldMatch, date);
		date = DateConverter.toString((Date)null);
		assertEquals(date, null);
		
	}
	
	@Test
	public void testMinuteTimeConverter()
	{
		String expected = "0d 0h 30m";
		Long time = 30L;
		assertEquals(expected, TimeConverter.convertMinuteTimeToString(time));
		expected = "0d 2h 10m";
		time = 130L;
		assertEquals(expected, TimeConverter.convertMinuteTimeToString(time));
		expected = "1d 3h 40m";
		time = 1660L;
		assertEquals(expected, TimeConverter.convertMinuteTimeToString(time));
		expected = "1d 0h 2m";
		time = 1442L;
		assertEquals(expected, TimeConverter.convertMinuteTimeToString(time));
		time = null;
		assertEquals("", TimeConverter.convertMinuteTimeToString(time));
	}
	
	@Test
	public void testMilliSecondTimeConverter()
	{
		String expected = "0h 0m 30s";
		Long time = 30000L;
		assertEquals(expected, TimeConverter.convertMillisecondTimeToString(time));
		expected = "0h 1m 0s";
		time = 60000L;
		assertEquals(expected, TimeConverter.convertMillisecondTimeToString(time));
		expected = "0h 1m 5s";
		time = 65000L;
		assertEquals(expected, TimeConverter.convertMillisecondTimeToString(time));
		time = null;
		assertEquals("", TimeConverter.convertMillisecondTimeToString(time));
	}
	
	
	private void assertDateIsAsExpected(Date dt, int year, int month, int day)
	{
		LocalDate ld = DateConverter.toLocalDate(dt);
		assertEquals(ld.getDayOfMonth(), day);
		assertEquals(ld.getMonthOfYear(), month);
		assertEquals(ld.getYear(), year);
	}
	
}
