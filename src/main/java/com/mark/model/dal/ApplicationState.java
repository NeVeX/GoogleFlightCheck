package com.mark.model.dal;

import java.io.Serializable;

import org.joda.time.DateTime;

public class ApplicationState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long flightApiCount;
	private DateTime date;
	
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
	}
	
	public void incrementFlightCounter()
	{
		this.flightApiCount++;
	}
	
	
	
}
