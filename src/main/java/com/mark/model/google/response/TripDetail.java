package com.mark.model.google.response;

import java.util.List;

public class TripDetail {
	private String kind;
	private List<Airport> airport;
	private List<City> city;
	private List<Aircraft> aircraft;
	private List<Tax> tax;
	private List<Carrier> carrier;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public List<Airport> getAirport() {
		return airport;
	}
	public void setAirport(List<Airport> airport) {
		this.airport = airport;
	}
	public List<City> getCity() {
		return city;
	}
	public void setCity(List<City> city) {
		this.city = city;
	}
	public List<Aircraft> getAircraft() {
		return aircraft;
	}
	public void setAircraft(List<Aircraft> aircraft) {
		this.aircraft = aircraft;
	}
	public List<Tax> getTax() {
		return tax;
	}
	public void setTax(List<Tax> tax) {
		this.tax = tax;
	}
	public List<Carrier> getCarrier() {
		return carrier;
	}
	public void setCarrier(List<Carrier> carrier) {
		this.carrier = carrier;
	}
	
}

