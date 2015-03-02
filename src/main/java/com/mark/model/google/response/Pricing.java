package com.mark.model.google.response;

import java.util.List;

import com.mark.model.google.request.Passengers;

public class Pricing {
	private String kind;
	private List<Fare> fare;
	private List<SegmentPricing> segmentPricing;
	private String baseFareTotal;
	private String saleFareTotal;
	private String saleTaxTotal;
	private String saleTotal;
	private String fareCalculation;
	private String latestTicketingTime;
	private String ptc;
	private boolean refundable;
	private Passengers passengers;
	private List<TaxDetail> tax;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public List<Fare> getFare() {
		return fare;
	}
	public void setFare(List<Fare> fare) {
		this.fare = fare;
	}
	public List<SegmentPricing> getSegmentPricing() {
		return segmentPricing;
	}
	public void setSegmentPricing(List<SegmentPricing> segmentPricing) {
		this.segmentPricing = segmentPricing;
	}
	public String getBaseFareTotal() {
		return baseFareTotal;
	}
	public void setBaseFareTotal(String baseFareTotal) {
		this.baseFareTotal = baseFareTotal;
	}
	public String getSaleFareTotal() {
		return saleFareTotal;
	}
	public void setSaleFareTotal(String saleFareTotal) {
		this.saleFareTotal = saleFareTotal;
	}
	public String getSaleTaxTotal() {
		return saleTaxTotal;
	}
	public void setSaleTaxTotal(String saleTaxTotal) {
		this.saleTaxTotal = saleTaxTotal;
	}
	public String getSaleTotal() {
		return saleTotal;
	}
	public void setSaleTotal(String saleTotal) {
		this.saleTotal = saleTotal;
	}
	public String getFareCalculation() {
		return fareCalculation;
	}
	public void setFareCalculation(String fareCalculation) {
		this.fareCalculation = fareCalculation;
	}
	public String getLatestTicketingTime() {
		return latestTicketingTime;
	}
	public void setLatestTicketingTime(String latestTicketingTime) {
		this.latestTicketingTime = latestTicketingTime;
	}
	public String getPtc() {
		return ptc;
	}
	public void setPtc(String ptc) {
		this.ptc = ptc;
	}
	public boolean isRefundable() {
		return refundable;
	}
	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}
	public Passengers getPassengers() {
		return passengers;
	}
	public void setPassengers(Passengers passengers) {
		this.passengers = passengers;
	}
	public List<TaxDetail> getTax() {
		return tax;
	}
	public void setTax(List<TaxDetail> tax) {
		this.tax = tax;
	}
	

}
