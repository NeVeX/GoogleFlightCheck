package com.mark.exception;

public class FlightException extends RuntimeException {

	public enum FlightExceptionType {
		GENERAL, 
		FLIGHT_API_LIMIT_REACHED, 
		FLIGHT_API_EXCEPTION, 
		FLIGHT_API_RETURNED_NO_DATA, 
		FLIGHT_ALGORITHM_DID_NOT_WORK, NO_FLIGHT_EXISTS_FOR_SEARCH, FLIGHT_SAVED_SEARCH_EXCEPTION
	}
	private FlightExceptionType exceptionType;
	
	public FlightException(String msg, Exception e, FlightExceptionType fe)
	{
		super(msg, e);
		exceptionType = fe != null ? fe : FlightExceptionType.GENERAL;
	}
	
	public FlightException(String msg, Exception e)
	{
		this(msg, e, FlightExceptionType.GENERAL);
	}
	
	public FlightException(String msg, Exception e, FlightExceptionType fe, Object... args)
	{
		this(String.format(msg, args),e, fe);
	}
	
	public FlightException(String msg, Exception e, Object... args)
	{
		this(String.format(msg, args),e, FlightExceptionType.GENERAL);
	}
	
	
	public FlightException(String msg, FlightExceptionType fe) {
		this(msg,null, fe);
	}
	
	public FlightException(String msg) {
		this(msg, FlightExceptionType.GENERAL);
	}

	public FlightExceptionType getExceptionType() {
		return exceptionType;
	}
	
}
