package com.mark.dal;

public interface FlightDAL {

	public boolean save(String origin, String destination, String date);
	
	public boolean search(String origin, String destination, String date);
}
