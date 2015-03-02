package com.mark.util;

import java.io.InputStream;
import java.util.Properties;

import com.mark.exception.FlightException;

public class FlightProperties {
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
	}
	
	public static String getProperty(String name)
	{
		return PROPERTIES.getProperty(name);
	}
	
}
