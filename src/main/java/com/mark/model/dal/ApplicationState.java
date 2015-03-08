package com.mark.model.dal;

import java.io.Serializable;

import org.joda.time.DateTime;

public class ApplicationState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer flightApiCount;
	private DateTime date;
	
	public Integer getFlightApiCount() {
		return flightApiCount;
	}
	public void setFlightApiCount(Integer flightApiCount) {
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
