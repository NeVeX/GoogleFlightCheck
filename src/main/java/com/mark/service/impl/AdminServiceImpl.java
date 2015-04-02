package com.mark.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.dal.IAdminDAL;
import com.mark.dal.IFlightResultDAL;
import com.mark.dal.IFlightSearchDAL;
import com.mark.dal.impl.FlightSearchDALImpl;
import com.mark.model.ApplicationState;
import com.mark.model.FlightSavedSearch;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.FlightProperties;

@Service
public class AdminServiceImpl implements IAdminService {

	private static final Logger log = Logger.getLogger(AdminServiceImpl.class.getName()); 
	
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
	public List<String> runTracker() {
		List<String> problemList = new ArrayList<String>();
		log.info("Batch Job: Getting list of saved searches with departure dates in the future");
		List < FlightSavedSearch > savedSearches = flightSearchDAL.getAllFlightSavedSearches(true);
		if (savedSearches != null && savedSearches.size() > 0) {
			log.info("Batch Job: Found [" + savedSearches.size() + "] saved searches with departure dates in the future - getting Flight Data with no search for today");
			// get all the flight searches that do not have updates for today
			List < FlightSavedSearch > needsUpdating = flightSearchDAL.getFlightSearchesThatNeedsTrackingForToday(savedSearches);
			if (needsUpdating != null && needsUpdating.size() > 0) {
				log.info("Batch Job: Found [" + needsUpdating.size() + "] searches that will be updated now");
				for (FlightSavedSearch fss: needsUpdating) {
					try {
						log.info("Batch Job: Attempting to update Flight Data for: " + fss);
						this.flightService.getFlightSearchResult(fss);
						log.info("Updated Flight Data for: " + fss);
					} catch (Exception e) {
						String msg = "Batch Job: Could not update flight details for [" + fss + "]";
						log.warning(msg + e);
						problemList.add(msg + " ---> Exception: "+e.getMessage());
					}
				}
			}
		}
		log.info("Batch Job: Finished. Encountered Problems? ["+ (problemList.size() > 0) +"]");
		return problemList;
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
