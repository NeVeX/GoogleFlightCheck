package com.mark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mark.exception.FlightException;
import com.mark.exception.FlightException.FlightExceptionType;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.TimeConverter;

public class FlightSearchResult extends FlightResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private FlightSavedSearch flightSearch;
	private String message;

	public FlightSearchResult(FlightResult flightResult, FlightSavedSearch savedSearch) {
		super(flightResult);
		if ( savedSearch == null)
		{
			throw new FlightException("Trying to create a Search Result without a saved search!");
		}
		this.flightSearch = savedSearch;
	}

	public FlightSearchResult(FlightSearchResult searchResult) {
		this(searchResult, searchResult.getFlightSearch());
	}

	public FlightSavedSearch getFlightSearch() {
		return flightSearch;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
