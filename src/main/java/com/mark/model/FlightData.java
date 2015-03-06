package com.mark.model;

import java.io.Serializable;

import com.mark.model.dal.FlightSavedSearch;

public class FlightData extends FlightSavedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private float shortestTimePrice;
	private float lowestPrice;
	public float getShortestTimePrice() {
		return shortestTimePrice;
	}
	public void setShortestTimePrice(float shortestTimePrice) {
		this.shortestTimePrice = shortestTimePrice;
	}
	public float getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(float lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
