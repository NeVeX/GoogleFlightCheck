package com.mark.model;

import java.io.Serializable;

public class FlightParsedData extends FlightSearch implements Serializable {

	private static final long serialVersionUID = 1L;
	private float price;
	private int numberOfStops;
	private int tripLength;
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
	
	

}
