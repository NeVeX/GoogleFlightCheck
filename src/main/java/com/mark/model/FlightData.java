package com.mark.model;

import java.io.Serializable;

public class FlightData implements Serializable {
	
	public enum FlightDataStatus
	{
		SAVED, ALREADY_EXISTS
	}
	private FlightDataStatus dataStatus;
	private String dataStatusString;
	
	private static final long serialVersionUID = 1L;
	private String origin;
	private String destination;
	private String date;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public FlightDataStatus getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(FlightDataStatus dataStatus) {
		this.dataStatus = dataStatus;
		this.dataStatusString = dataStatus != null ? dataStatus.name() : "";
	}
	public String getDataStatusString() {
		return dataStatusString;
	}
	public void setDataStatusString(String dataStatusString) {
		this.dataStatusString = dataStatusString;
	}
	
	

}
