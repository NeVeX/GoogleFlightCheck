package com.mark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.mark.util.converter.DateConverter;
import com.mark.util.converter.TimeConverter;

public class FlightInfo extends FlightSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Float shortestTimePrice;
	private Float lowestPrice;
	private Date dateSearched;
	private String dateSearchedString;
	private Long lowestPriceTripDuration;
	private Long shortestTimePriceTripDuration;
	private List<FlightInfo> history;
	private String infoMessage;
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
		return "FlightInfo [shortestTimePrice=" + shortestTimePrice
				+ ", lowestPrice=" + lowestPrice + ", dateSearched="
				+ dateSearched + ", dateSearchedString=" + dateSearchedString
				+ ", lowestPriceTripDuration=" + lowestPriceTripDuration
				+ ", shortestTimePriceTripDuration="
				+ shortestTimePriceTripDuration + ", history=" + history
				+ ", infoMessage=" + infoMessage + ", exceptionMessage="
				+ exceptionMessage + "]";
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
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	public List<FlightInfo> getHistory() {
		return history;
	}

	public void setHistory(List<FlightInfo> history) {
		this.history = history;
	}

	public String getDateSearchedString() {
		return dateSearchedString;
	}

	public void setDateSearchedString(String dateSearchedString) {
		this.dateSearchedString = dateSearchedString;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

}