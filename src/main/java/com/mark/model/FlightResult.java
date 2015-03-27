package com.mark.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.TimeConverter;

public class FlightResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private Float shortestTimePrice;
	private Float lowestPrice;
	@JsonFormat(timezone="GMT", pattern=DateConverter.DATE_FORMAT, shape=Shape.STRING)
	private Date dateSearched;
	private Long lowestPriceTripDuration;
	private Long shortestTimePriceTripDuration;
	
	public FlightResult(Date dateSearched)
	{
		this.dateSearched = dateSearched;
	}
	
	public FlightResult(FlightResult flightResult)
	{
		this(flightResult.getDateSearched());
		this.shortestTimePrice = flightResult.getShortestTimePrice();
		this.lowestPrice = flightResult.getLowestPrice();
		this.lowestPriceTripDuration = flightResult.getLowestPriceTripDuration();
		this.shortestTimePriceTripDuration = flightResult.getShortestTimePriceTripDuration();
	}
	
	public String getLowestTripDurationAsString()
	{
		return TimeConverter.convertMinuteTimeToString(lowestPriceTripDuration);
	}
	
	public String getShortestTripDurationAsString()
	{
		return TimeConverter.convertMinuteTimeToString(shortestTimePriceTripDuration);
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
	public Date getDateSearched() {
		return dateSearched;
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
