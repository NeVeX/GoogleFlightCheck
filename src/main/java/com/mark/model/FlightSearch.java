package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mark.util.converter.DateConverter;

public class FlightSearch implements Serializable {

	private static final long serialVersionUID = 4160281245555626055L;
	private String origin;
	private String destination;
	private String departureDateString;
	private String returnDateString;
	private Boolean forceBatchUsage;
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDepartureDateString() {
		return departureDateString;
	}
	public void setDepartureDateString(String departureDateString) {
		this.departureDateString = departureDateString;
	}
	public String getReturnDateString() {
		return returnDateString;
	}
	public void setReturnDateString(String returnDateString) {
		this.returnDateString = returnDateString;
	}
	
	@Override
	public String toString() {
		return "FlightSearch [origin=" + origin + ", destination="
				+ destination + ", departureDateString=" + departureDateString
				+ ", returnDateString=" + returnDateString
				+ ", forceBatchUsage=" + forceBatchUsage + "]";
	}
	public Boolean getForceBatchUsage() {
		return forceBatchUsage;
	}
	public void setForceBatchUsage(Boolean forceBatchUsage) {
		this.forceBatchUsage = forceBatchUsage;
	}

}
