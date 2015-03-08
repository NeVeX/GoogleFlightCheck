package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mark.util.converter.DateConverter;

public class FlightSearch implements Serializable {

	private static final long serialVersionUID = 4160281245555626055L;
	private String origin;
	private String destination;
	
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
	
	@Override
	public String toString() {
		return "FlightSearch ["+super.toString()+" origin=" + origin + ", destination="
				+ destination+"]";
	}
	
	
}
