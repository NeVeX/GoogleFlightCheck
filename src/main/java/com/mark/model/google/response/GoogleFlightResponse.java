package com.mark.model.google.response;

public class GoogleFlightResponse {

	private String kind;
	private Trip trips;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public Trip getTrips() {
		return trips;
	}
	public void setTrips(Trip trips) {
		this.trips = trips;
	}
	
}
