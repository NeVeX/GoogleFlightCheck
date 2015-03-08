package com.mark.controller;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mark.model.FlightData;
import com.mark.model.dal.ApplicationState;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.service.IFlightService;
import com.mark.util.client.type.IGoogleFlightClient;

@Controller
@Path("/admin")
public class AdminController {
	@Autowired
	private IFlightService flightService;

	@RequestMapping(method=RequestMethod.GET)
	public String getMainPage(ModelMap model)
	{
		List<FlightSavedSearch> allSavedSearches = flightService.getAllFlightSavedSearches();
		List<FlightData> allFlightData = flightService.getAllFlightData();
		List<ApplicationState> allApplicationStates = flightService.getAllApplicationStates();
		model.put("allSavedSearches", allSavedSearches);
		model.put("allFlightData", allFlightData);
		model.put("allApplicationStates", allApplicationStates);
		return "admin"; // the admin page
	}
}
