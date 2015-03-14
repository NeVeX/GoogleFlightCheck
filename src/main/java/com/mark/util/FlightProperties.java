package com.mark.util;

import java.io.InputStream;
import java.util.Properties;

import com.mark.exception.FlightException;

public class FlightProperties {
	/**
	 * Common application property for ease of use
	 */
	public final static Boolean IN_DEBUG_MODE;
	public final static String GOOGLE_FLIGHT_API_KEY;
	public final static int GOOGLE_FLIGHT_API_DAILY_INVOKE_LIMIT;
	public final static String GOOGLE_FLIGHT_API_BASE_URL;
	public final static String GOOGLE_FLIGHT_API_BASE_URI;
	
	private static String FLIGHT_PROPERTIES_NAME = "flight.properties";
	private static Properties PROPERTIES = new Properties();
	
	static
	{
		try(InputStream in = FlightProperties.class.getClassLoader().getResourceAsStream(FLIGHT_PROPERTIES_NAME))
		{
			PROPERTIES.load(in);
		}
		catch(Exception e)
		{
			throw new FlightException("Could not load properties file ["+FLIGHT_PROPERTIES_NAME+"]", e);
		}
		IN_DEBUG_MODE = Boolean.valueOf(FlightProperties.getProperty("debugMode"));
		GOOGLE_FLIGHT_API_KEY = FlightProperties.getProperty("google.flight.api.key");
		GOOGLE_FLIGHT_API_DAILY_INVOKE_LIMIT = Integer.valueOf(FlightProperties.getProperty("google.flight.api.daily.call.limit"));
		GOOGLE_FLIGHT_API_BASE_URL = FlightProperties.getProperty("google.flight.api.baseUrl");
		GOOGLE_FLIGHT_API_BASE_URI = FlightProperties.getProperty("google.flight.api.baseUri");
		
		
	}
	
	private static String getProperty(String name)
	{
		return PROPERTIES.getProperty(name);
	}
	
}
