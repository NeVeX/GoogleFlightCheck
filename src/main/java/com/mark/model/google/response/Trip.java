package com.mark.model.google.response;

import java.util.List;

public class Trip {
	private String kind;
	private String requestId;
	private TripDetail data;
	private List<TripOption> tripOption;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public TripDetail getData() {
		return data;
	}
	public void setData(TripDetail data) {
		this.data = data;
	}
	public List<TripOption> getTripOption() {
		return tripOption;
	}
	public void setTripOption(List<TripOption> tripOption) {
		this.tripOption = tripOption;
	}
	
}

