package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class FlightParsedData extends FlightSearch implements Serializable {

	private static final long serialVersionUID = 1L;
	private float price;
	private int numberOfStops;
	private int tripLength;
	private LocalDate departureDate;
	private LocalDate returnDate;
	
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
	public LocalDate getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	
	
	

}
