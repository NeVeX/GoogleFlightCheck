package com.mark.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.mark.model.dal.FlightSavedSearch;
import com.mark.util.converter.TimeConverter;

public class FlightData extends FlightSavedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Float shortestTimePrice;
	private Float lowestPrice;
	private LocalDate dateSearched;
	private Long lowestPriceTripDuration;
	private Long shortestTimePriceTripDuration;
	
	public String getLowestTripDurationAsString()
	{
		return TimeConverter.convertToTimeString(lowestPriceTripDuration);
	}
	
	public String getShortestTripDurationAsString()
	{
		return TimeConverter.convertToTimeString(shortestTimePriceTripDuration);
	}
	
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
	public LocalDate getDateSearched() {
		return dateSearched;
	}
	public void setDateSearched(LocalDate dateSearched) {
		this.dateSearched = dateSearched;
	}
	@Override
	public String toString() {
		return "FlightData ["+super.toString()+" shortestTimePrice=" + shortestTimePrice
				+ ", lowestPrice=" + lowestPrice + ", dateSearched="
				+ dateSearched + "]";
	}
	public Long getLowestPriceTripDuration() {
		return lowestPriceTripDuration;
	}
	public void setLowestPriceTripDuration(Long lowestPriceTripDuration) {
		this.lowestPriceTripDuration = lowestPriceTripDuration;
	}
	public Long getShortestTimePriceTripDuration() {
		return shortestTimePriceTripDuration;
	}
	public void setShortestTimePriceTripDuration(
			Long shortestTimePriceTripDuration) {
		this.shortestTimePriceTripDuration = shortestTimePriceTripDuration;
	}
	
	

}
