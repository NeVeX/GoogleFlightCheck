package com.mark.util.converter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import com.mark.util.client.type.vanilla.JavaNetGoogleFlightApiClientImpl;

public class DateConverter extends JsonSerializer<Date> {
	private static final Logger log = Logger.getLogger(DateConverter.class.getName());
	public static final String DATE_FORMAT = "yyyy-MM-dd";
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
				log.warning("Could not convert LocalDate ["+dt+"] to String with Format ["+DATE_FORMAT+"]");
			}
		}
		return null;
	}
	
	/**
	 * Convert Date to String. If it fails to convert, an empty string is returned
	 * @param dt
	 * @return
	 */
	public static String toString(Date dt)
	{
		if ( dt != null )
		{
			return toString(new LocalDate(dt, DateTimeZone.UTC));
		}
		return null;
	}
	
	/**
	 * Converts a string to a DateTime. If it fails, a null DateTime is returned.
	 * @param s
	 * @return
	 */
	public static LocalDate toLocalDate(String s)
	{
		if ( !StringUtils.isEmpty(s))
		{
			try 
			{
				return formatter.parseLocalDate(s);
			}
			catch(Exception e)
			{
				log.warning("Could not convert String ["+s+"] to LocalDate with Format ["+DATE_FORMAT+"]");
			}	
		}
		return null;
	}
	
	/**
	 * Converts a string to a Date. If it fails, a null is returned.
	 * @param s
	 * @return
	 */
	public static Date toDate(String s)
	{
		LocalDate ld = toLocalDate(s);
		if ( ld != null )
		{
			return ld.toDate();
		}
		return null;
	}
	
	public static Date toDate(LocalDate ld)
	{
		if ( ld != null )
		{
			return ld.toDate();
		}
		return null;
	}

	public static LocalDate toLocalDate(Date dt) {
		if ( dt != null )
		{
			return new LocalDate(dt, DateTimeZone.UTC);
		}
		return null;
	}

//	@Override
//	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
//		System.out.println("value is "+value);
//		
//	}

	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		log.info("value is "+value);
			
	}


	
}
