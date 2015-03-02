package com.mark.model.google.request;

import java.util.List;

public class Slice
{
	private String kind;
	private String origin;
	private String destination;
	private String date;
	private Integer maxStops;
	private Integer maxConnectionDuration;
	private String preferredCabin;
	private DepartureTime permittedDepartureTime;
	private List<String> permittedCarrier;
	private String alliance;
	private List<String> prohibitedCarrier;
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getMaxStops() {
		return maxStops;
	}
	public void setMaxStops(Integer maxStops) {
		this.maxStops = maxStops;
	}
	public Integer getMaxConnectionDuration() {
		return maxConnectionDuration;
	}
	public void setMaxConnectionDuration(Integer maxConnectionDuration) {
		this.maxConnectionDuration = maxConnectionDuration;
	}
	public String getPreferredCabin() {
		return preferredCabin;
	}
	public void setPreferredCabin(String preferredCabin) {
		this.preferredCabin = preferredCabin;
	}
	public DepartureTime getPermittedDepartureTime() {
		return permittedDepartureTime;
	}
	public void setPermittedDepartureTime(DepartureTime permittedDepartureTime) {
		this.permittedDepartureTime = permittedDepartureTime;
	}
	public List<String> getPermittedCarrier() {
		return permittedCarrier;
	}
	public void setPermittedCarrier(List<String> permittedCarrier) {
		this.permittedCarrier = permittedCarrier;
	}
	public String getAlliance() {
		return alliance;
	}
	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}
	public List<String> getProhibitedCarrier() {
		return prohibitedCarrier;
	}
	public void setProhibitedCarrier(List<String> prohibitedCarrier) {
		this.prohibitedCarrier = prohibitedCarrier;
	}
	
}