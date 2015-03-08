package com.mark.exception;

public class FlightException extends RuntimeException {

	public FlightException(String msg, Exception e, Object... args)
	{
		this(String.format(msg, args),e);
	}
	
	public FlightException(String msg, Exception e)
	{
		super(msg, e);
	}

	public FlightException(String msg) {
		super(msg);
	}
	
}
