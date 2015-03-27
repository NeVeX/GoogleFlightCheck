package com.mark.model;

import java.text.DateFormat;
import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mark.util.converter.DateConverter;

public class FlightInputSearch {
	
	private static final long serialVersionUID = 4160281245555626055L;
	@Size(min=3, max=3, message="Origin name must be 3 characters long")
	@NotBlank(message="Origin cannot be left blank")
	private String origin;
	@Size(min=3, max=3, message="Destination name must be 3 characters long")
	@NotBlank(message="Destination cannot be left blank")
	private String destination;
	@JsonProperty
	private Boolean forceBatchUsage;
	@Future(message="Depature date cannot be in the past")
	@DateTimeFormat(style="S-", pattern=DateConverter.DATE_FORMAT,iso=ISO.DATE)
	@JsonFormat(timezone="GMT", pattern=DateConverter.DATE_FORMAT, shape=Shape.STRING)
	private Date departureDate;
	private Date returnDate;
	
	/**
	 * for ui usage
	 * @param inputSearch
	 */
	private String departureDateAsString;
	private String returnDateAsString;
	
	public FlightInputSearch(FlightInputSearch inputSearch) {
		this.origin = inputSearch.getOrigin();
		this.destination = inputSearch.getDestination();
		this.setDepartureDate(inputSearch.getDepartureDate());
		this.setReturnDate(inputSearch.getReturnDate());
	}
	public FlightInputSearch() {
	}
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		if ( origin != null)
		{
			this.origin = origin.toUpperCase();
		}
		this.origin = origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		if ( destination != null)
		{
			this.destination = destination.toUpperCase();
		}
		this.destination = destination;
	}

	@JsonIgnore
	public Boolean getForceBatchUsage() {
		return forceBatchUsage;
	}
	@JsonProperty
	public void setForceBatchUsage(Boolean forceBatchUsage) {
		this.forceBatchUsage = forceBatchUsage;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
		this.departureDateAsString = DateConverter.toString(departureDate);
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
		this.returnDateAsString = DateConverter.toString(returnDate);
	}
	@Override
	public String toString() {
		return "FlightInputSearch [origin=" + origin + ", destination="
				+ destination + ", forceBatchUsage=" + forceBatchUsage
				+ ", departureDate=" + departureDate + ", returnDate="
				+ returnDate + "]";
	}
	public String getReturnDateAsString() {
		return returnDateAsString;
	}
	public void setReturnDateAsString(String returnDateAsString) {
		this.returnDateAsString = returnDateAsString;
	}
	public String getDepartureDateAsString() {
		return departureDateAsString;
	}
	public void setDepartureDateAsString(String departureDateAsString) {
		this.departureDateAsString = departureDateAsString;
	}
	
	
	
}
