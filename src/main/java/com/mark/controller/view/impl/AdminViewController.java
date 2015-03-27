package com.mark.controller.view.impl;

import java.util.List;

import javax.ws.rs.Path;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.constant.ControllerConstants;
import com.mark.controller.view.IAdminViewController;
import com.mark.model.ApplicationState;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.client.type.resteasy.IRestEasyGoogleFlightApiClient;
import com.mark.util.converter.TimeConverter;

@Controller
public class AdminViewController implements IAdminViewController {
	
	private static final String ALL_FLIGHT_SEARCH_RESULTS = "allFlightSearchResults";
	private static final String ALL_APPLICATION_STATES = "allApplicationStates";
	
	@Autowired
	private IAdminService adminService;
	@Autowired
	private IFlightService flightService;
	
	@RequestMapping(value=ControllerConstants.ADMIN_VIEW_URI, method=RequestMethod.GET)
	public String getAdminView(ModelMap model)
	{
		List<FlightSearchResult> allFlightData = flightService.getAllFlightSearchResults();
		List<ApplicationState> allApplicationStates = adminService.getAllApplicationStates();
		model.put(ALL_FLIGHT_SEARCH_RESULTS, allFlightData);
		model.put(ALL_APPLICATION_STATES, allApplicationStates);
		return ControllerConstants.ADMIN_VIEW_NAME; // the admin page
	}
	
}
