package com.mark.util.converter;

public class TimeConverter {

	
	private static final String DAY = "d";
	private static final String MINUTE = "m";
	private static final String HOUR = "h";
	private static final String SECOND = "s";
	private static final String SPACE = " ";
	private static final String NA = "N/A";
	
	
	/**
	 * Return a string in time representation form. e.g. 1d 14h 32m
	 * @param time
	 * @return
	 */
	public static String convertMinuteTimeToString(Long time)
	{
		if( time != null)
		{
			int reducedTime = time.intValue();
			int minutes = 0, days = 0, hours = 0;
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
			return getTimeString(days, hours, minutes, -1);
		}
		return NA;
	}


	public static String convertMillisecondTimeToString(Long time) {
		if( time != null)
		{
			int reducedTime = time.intValue();
			int minutes = 0, seconds = 0, hours = 0;
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
			return getTimeString(-1, hours, minutes, seconds);
		}
		return NA;
	}
	
	private static String getTimeString(int day, int hour, int min, int sec)
	{
		String output = "";
		boolean cannotSkip = false;
		if ( day > 0 ) { output += day + DAY + SPACE; cannotSkip = true;}
		if ( hour > 0 || cannotSkip) { output += hour + HOUR + SPACE; cannotSkip = true;}
		if ( min > 0 || cannotSkip) { output += min + MINUTE + SPACE; cannotSkip = true;}
		if ( sec > 0 || (cannotSkip && sec != -1)) { output += sec + SECOND + SPACE; }
		return output.length() > 0 ? output.trim() : NA;	
	}
	
}
