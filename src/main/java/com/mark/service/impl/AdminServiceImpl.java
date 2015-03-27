package com.mark.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IAdminDAL;
import com.mark.dal.IFlightResultDAL;
import com.mark.dal.IFlightSearchDAL;
import com.mark.model.ApplicationState;
import com.mark.model.FlightSavedSearch;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.FlightProperties;

@Service
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminDAL applicationDAL;
	@Autowired
	private IFlightSearchDAL flightSearchDAL;
	@Autowired
	private IFlightResultDAL flightResultDAL;
	@Autowired
	private IFlightService flightService;
	
	private AtomicLong flightCallCurrentCount = new AtomicLong(0);
	
	@PostConstruct
	public void setup() {
		ApplicationState appState = applicationDAL.getApplicationState();
		if (appState != null) {
			flightCallCurrentCount = new AtomicLong(appState.getFlightApiCount());
		}
	}
	
	@Override
	public List < ApplicationState > getAllApplicationStates() {
		return applicationDAL.getAllApplicationStates();
	}

	@Override
	public boolean runTracker() {
		boolean encounteredProblems = false;
		System.out.println("Batch Job: Getting list of saved searches with departure dates in the future");
		List < FlightSavedSearch > savedSearches = flightSearchDAL.getAllFlightSavedSearches(true);
		if (savedSearches != null && savedSearches.size() > 0) {
			System.out.println("Batch Job: Found [" + savedSearches.size() + "] saved searches with departure dates in the future - getting Flight Data with no search for today");
			// get all the flight searches that do not have updates for today
			List < FlightSavedSearch > needsUpdating = flightSearchDAL.getFlightSearchesThatNeedsTrackingForToday(savedSearches);
			if (needsUpdating != null && needsUpdating.size() > 0) {
				System.out.println("Batch Job: Found [" + needsUpdating.size() + "] searches that will be updated now");
				for (FlightSavedSearch fss: needsUpdating) {
					try {
						System.out.println("Batch Job: Attempting to update Flight Data for: " + fss);
						this.flightService.getFlightSearchResult(fss);
						System.out.println("Updated Flight Data for: " + fss);
					} catch (Exception e) {
						encounteredProblems = true;
						System.err.println("Batch Job: Could not update flight details for [" + fss + "].\n" + e);
					}
				}
			}
		}
		System.out.println("Batch Job: Finished. Encountered Problems? ["+encounteredProblems+"]");
		return encounteredProblems;
	}

	@Override
	public void incrementFlightAPICount() {
		flightCallCurrentCount.incrementAndGet(); // increment by one
		saveFlightApplicationState();
	}
	
	// TODO: Make async call possible
	private void saveFlightApplicationState() {
		ApplicationState appState = new ApplicationState();
		appState.setDate(new LocalDate().toDate());
		appState.setFlightApiCount(flightCallCurrentCount.get());
		applicationDAL.saveApplicationState(appState);
	}

	@Override
	public boolean isAllowedToCallFlightAPI() {
		return flightCallCurrentCount.get() < FlightProperties.GOOGLE_FLIGHT_API_DAILY_INVOKE_LIMIT;
	}


}
