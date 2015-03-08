package com.mark.model.dal;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.google.appengine.api.datastore.Key;
import com.mark.util.converter.DateConverter;

public class ApplicationState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long flightApiCount;
	private DateTime date;
	private String dateString;
	private Key key;
	
	public Long getFlightApiCount() {
		return flightApiCount;
	}
	public void setFlightApiCount(Long flightApiCount) {
		this.flightApiCount = flightApiCount;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
		this.dateString = DateConverter.convertToString(date);
	}
	
	@Override
	public String toString() {
		return "ApplicationState [flightApiCount=" + flightApiCount + ", date="
				+ date + ", dateString=" + dateString + ", key=" + key + "]";
	}

	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getDateString() {
		return dateString;
	}
	
}
