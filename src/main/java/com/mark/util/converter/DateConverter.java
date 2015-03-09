package com.mark.util.converter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

public class DateConverter {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
	
	/**
	 * Convert DateTime to String. If it fails to convert, an empty string is returned
	 * @param dt
	 * @return
	 */
	public static String toString(LocalDate dt)
	{
		if ( dt != null )
		{
			try
			{
				return dt.toString(DATE_FORMAT);
			}
			catch(Exception e)
			{
				System.err.println("Could not convert LocalDate ["+dt+"] to String with Format ["+DATE_FORMAT+"]");
			}
		}
		return null;
	}
	
	/**
	 * Converts a string to a DateTime. If it fails, a null DateTime is returned.
	 * @param s
	 * @return
	 */
	public static LocalDate toDate(String s)
	{
		if ( !StringUtils.isEmpty(s))
		{
			try 
			{
				return formatter.parseLocalDate(s);
			}
			catch(Exception e)
			{
				System.err.println("Could not convert String ["+s+"] to LocalDate with Format ["+DATE_FORMAT+"]");
			}	
		}
		return null;
	}
	
}
