package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class FlightParsedData extends FlightSearch implements Serializable {

	private static final long serialVersionUID = 1L;
	private float price;
	private int numberOfStops;
	private int tripLength;
	private DateTime departureDate;
	private DateTime returnDate;
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getNumberOfStops() {
		return numberOfStops;
	}
	public void setNumberOfStops(int numberOfStops) {
		this.numberOfStops = numberOfStops;
	}
	public int getTripLength() {
		return tripLength;
	}
	public void setTripLength(int tripLength) {
		this.tripLength = tripLength;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DateTime getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(DateTime departureDate) {
		this.departureDate = departureDate;
	}
	public DateTime getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(DateTime returnDate) {
		this.returnDate = returnDate;
	}
	
	
	

}
