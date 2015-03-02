package com.mark.model.google.response;

import java.util.List;

public class TripOption {
	private String kind;
	private String saleTotal;
	private String id;
	private List<ResponseSlice> slice;
	private List<Pricing> pricing;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getSaleTotal() {
		return saleTotal;
	}
	public void setSaleTotal(String saleTotal) {
		this.saleTotal = saleTotal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<ResponseSlice> getSlice() {
		return slice;
	}
	public void setSlice(List<ResponseSlice> slice) {
		this.slice = slice;
	}
	public List<Pricing> getPricing() {
		return pricing;
	}
	public void setPricing(List<Pricing> pricing) {
		this.pricing = pricing;
	}
	
}
