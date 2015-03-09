package com.mark.util.converter;

public class TimeConverter {

	
	private static final String DAY = "d";
	private static final String MINUTE = "m";
	private static final String HOUR = "h";
	private static final String SECOND = "s";
	private static final String SPACE = " ";
	
	
	/**
	 * Return a string in time representation form. e.g. 1d 14h 32m
	 * @param time
	 * @return
	 */
	public static String convertMinuteTimeToString(Long time)
	{
		if( time != null)
		{
			long reducedTime = time;
			long minutes = 0, days = 0, hours = 0;
			days = reducedTime / 1440;
			if ( days > 0 )
			{
				reducedTime = reducedTime % 1440; 
			}
			hours = reducedTime / 60;
			if ( hours > 0)
			{
				reducedTime = reducedTime % 60;
			}
			minutes = reducedTime;
			return days + DAY + SPACE + hours + HOUR + SPACE + minutes + MINUTE;
		}
		return "";
	}


	public static String convertMillisecondTimeToString(Long time) {
		if( time != null)
		{
			long reducedTime = time;
			long minutes = 0, seconds = 0, hours = 0;
			hours = reducedTime / 3600000;
			if ( hours > 0 )
			{
				reducedTime = reducedTime % 3600000; 
			}
			minutes = reducedTime / 60000;
			if ( minutes > 0)
			{
				reducedTime = reducedTime % 60000;
			}
			seconds = reducedTime / 1000;
			return hours + HOUR + SPACE + minutes + MINUTE +SPACE + seconds + SECOND;
		}
		return "";
	}
	
}
