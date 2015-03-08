package com.mark.model.dal;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Primary;

import com.google.appengine.api.datastore.Key;
import com.mark.model.FlightSearch;

public class FlightSavedSearch extends FlightSearch implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Key key;
	private Boolean existingSearch;
	private DateTime date;
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
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
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "FlightSavedSearch [key=" + key + ", existingSearch="
				+ existingSearch + ", date=" + date + "]";
	}

}

