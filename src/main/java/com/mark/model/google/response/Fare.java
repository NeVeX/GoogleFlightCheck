package com.mark.model.google.response;

public class Fare {
	private String kind;

	private String id;
	private String carrier;
	private String origin;
	private String destination;
	private String basisCode;
	private Integer is_private;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
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
	public String getBasisCode() {
		return basisCode;
	}
	public void setBasisCode(String basisCode) {
		this.basisCode = basisCode;
	}
	public Integer getIs_private() {
		return is_private;
	}
	public void setIs_private(Integer is_private) {
		this.is_private = is_private;
	}
	
}
