package com.mark.model;

import java.io.Serializable;

import com.mark.model.dal.FlightSavedSearch;

public class FlightData extends FlightSavedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private float priceShortestTime;
	private float priceLowestPrice;
	
	public float getPriceShortestTime() {
		return priceShortestTime;
	}
	public void setPriceShortestTime(float priceShortestTime) {
		this.priceShortestTime = priceShortestTime;
	}
	public float getPriceLowestPrice() {
		return priceLowestPrice;
	}
	public void setPriceLowestPrice(float priceLowestPrice) {
		this.priceLowestPrice = priceLowestPrice;
	}
	
	

}
