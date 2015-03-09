package com.mark.model.dal;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Primary;

import com.google.appengine.api.datastore.Key;
import com.mark.model.FlightSearch;
import com.mark.util.converter.DateConverter;

public class FlightSavedSearch extends FlightSearch implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Key key;
	private Boolean existingSearch;
	private LocalDate departureDate;
	private LocalDate returnDate;
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	

	public Boolean getExistingSearch() {
		return existingSearch;
	}
	
	public Boolean isExistingSearch() {
		return existingSearch;
	}
	public void setExistingSearch(Boolean existingSearch) {
		this.existingSearch = existingSearch;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}
	@Override
	public String toString() {
		return "FlightSavedSearch ["+super.toString()+", key=" + key + ", existingSearch="
				+ existingSearch + ", departureDate=" + departureDate
				+ ", returnDate=" + returnDate + "]";
	}
	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
		super.setDepartureDateString(DateConverter.toString(departureDate));
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
		super.setReturnDateString(DateConverter.toString(returnDate));
	}

}

