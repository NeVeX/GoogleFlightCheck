package com.mark.service;

import java.util.List;

import com.mark.model.ApplicationState;

public interface IAdminService {

	/**
	 * Get the list of all historical application states
	 * @return
	 */
	public List<ApplicationState> getAllApplicationStates();

	/**
	 * Determine all the flight info that needs to be updated...and get perform those updates
	 */
	public boolean runTracker();

	public void incrementFlightAPICount();

	public boolean isAllowedToCallFlightAPI();

}
