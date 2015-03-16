package com.mark.model;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class FlightParsedData extends FlightSearch implements Serializable {

	private static final long serialVersionUID = 1L;
	private float price;
	private int numberOfStops;
	private int tripLength;
	private Date departureDate;
	private Date returnDate;
	
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
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	
	

}
