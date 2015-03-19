package com.mark.model;

import java.io.Serializable;



import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.google.appengine.api.datastore.Key;
import com.mark.util.converter.DateConverter;

public class FlightSearch implements Serializable {

	private static final long serialVersionUID = 4160281245555626055L;
	@Size(min=3, max=3, message="Origin name must be 3 characters long")
	@NotBlank(message="Origin cannot be left blank")
	private String origin;
	@Size(min=3, max=3, message="Destination name must be 3 characters long")
	@NotBlank(message="Destination cannot be left blank")
	private String destination;
	@NotBlank(message="Depature Date cannot be blank")
//	private String departureDateString;
	private String returnDateString;
	private Boolean forceBatchUsage;
	private Key key;
	private Boolean existingSearch;
	@Future
	@DateTimeFormat(pattern=DateConverter.DATE_FORMAT)
	private Date departureDate;
	@Future
	private Date returnDate;
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
//	public String getDepartureDateString() {
//		return departureDateString;
//	}
//	public void setDepartureDateString(String departureDateString) {
//		this.departureDateString = departureDateString;
//		this.departureDate = DateConverter.toDate(this.departureDateString);
//		
//	}
	public String getReturnDateString() {
		return returnDateString;
	}
	public void setReturnDateString(String returnDateString) {
		this.returnDateString = returnDateString;
		this.returnDate = DateConverter.toDate(this.returnDateString);
	}
	
	
	public Boolean getForceBatchUsage() {
		return forceBatchUsage;
	}
	public void setForceBatchUsage(Boolean forceBatchUsage) {
		this.forceBatchUsage = forceBatchUsage;
	}
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public Boolean getExistingSearch() {
		return existingSearch;
	}
	public void setExistingSearch(Boolean existingSearch) {
		this.existingSearch = existingSearch;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
//		this.departureDateString = DateConverter.toString(departureDate);
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
		this.returnDateString = DateConverter.toString(this.returnDate);
	}
	public Boolean isExistingSearch() {
		return existingSearch;
	}

}
