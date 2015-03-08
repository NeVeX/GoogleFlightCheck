package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mark.model.dal.FlightSavedSearch;

public class FlightData extends FlightSavedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Float shortestTimePrice;
	private Float lowestPrice;
	private DateTime dateSearched;
	
	public Float getShortestTimePrice() {
		return shortestTimePrice;
	}
	public void setShortestTimePrice(Float shortestTimePrice) {
		this.shortestTimePrice = shortestTimePrice;
	}
	public Float getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(Float lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DateTime getDateSearched() {
		return dateSearched;
	}
	public void setDateSearched(DateTime dateSearched) {
		this.dateSearched = dateSearched;
	}
	@Override
	public String toString() {
		return "FlightData ["+super.toString()+" shortestTimePrice=" + shortestTimePrice
				+ ", lowestPrice=" + lowestPrice + ", dateSearched="
				+ dateSearched + "]";
	}
	
	

}
