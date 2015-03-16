package com.mark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.mark.model.dal.FlightSavedSearch;
import com.mark.util.converter.DateConverter;
import com.mark.util.converter.TimeConverter;

public class FlightData extends FlightSavedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Float shortestTimePrice;
	private Float lowestPrice;
	private Date dateSearched;
	private String dateSearchedString;
	private Long lowestPriceTripDuration;
	private Long shortestTimePriceTripDuration;
	private List<FlightData> history;
	private String message;
	private String exceptionMessage;
	
	
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getDateSearched() {
		return dateSearched;
	}
	public void setDateSearched(Date dateSearched) {
		this.dateSearched = dateSearched;
		this.dateSearchedString = DateConverter.toString(dateSearched);
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	public List<FlightData> getHistory() {
		return history;
	}

	public void setHistory(List<FlightData> history) {
		this.history = history;
	}

	public String getDateSearchedString() {
		return dateSearchedString;
	}

	public void setDateSearchedString(String dateSearchedString) {
		this.dateSearchedString = dateSearchedString;
	}

}
