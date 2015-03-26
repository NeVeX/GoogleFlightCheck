package com.mark.model;

import java.io.Serializable;



import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.appengine.api.datastore.Key;
import com.mark.util.converter.DateConverter;

public class FlightSavedSearch extends FlightInputSearch implements Serializable {

	private static final long serialVersionUID = 4160281245555626055L;
	private Key key;
	private Boolean flightOptionsExists;
	
	public FlightSavedSearch(Key key, FlightInputSearch inputSearch)
	{
		super(inputSearch);
		this.key = key;
	}
	
	public FlightSavedSearch(Key key, FlightInputSearch inputSearch, boolean flightOptionsExist) {
		this(key, inputSearch);
		this.flightOptionsExists = flightOptionsExist;
	}

	
	public Key getKey() {
		return key;
	}
	
	public Boolean getFlightOptionsExists() {
		return flightOptionsExists;
	}
	public void setFlightOptionsExists(Boolean flightOptionsExists) {
		this.flightOptionsExists = flightOptionsExists;
	}

}
