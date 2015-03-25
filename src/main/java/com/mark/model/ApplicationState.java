package com.mark.model;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.appengine.api.datastore.Key;
import com.mark.util.converter.DateConverter;

public class ApplicationState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long flightApiCount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConverter.DATE_FORMAT)
	private Date date;
	private Key key;
	
	public Long getFlightApiCount() {
		return flightApiCount;
	}
	public void setFlightApiCount(Long flightApiCount) {
		this.flightApiCount = flightApiCount;
	}

	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
