package com.mark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FlightSearchHistoricalResult extends FlightSearchResult implements Serializable {
	
	public FlightSearchHistoricalResult(FlightSearchResult searchResult) {
		super(searchResult);
	
	}

	private List<FlightResult> history;
	
	public List<FlightResult> getHistory() {
		return history;
	}

	public void setHistory(List<FlightResult> history) {
		this.history = history;
	}


}
