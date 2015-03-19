package com.mark.controller;

import java.util.List;

import javax.ws.rs.Path;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.model.ApplicationState;
import com.mark.model.FlightInfo;
import com.mark.model.FlightSearch;
import com.mark.service.IFlightService;
import com.mark.util.client.type.resteasy.IRestEasyGoogleFlightApiClient;
import com.mark.util.converter.TimeConverter;

@Controller
public class AdminController {
	@Autowired
	private IFlightService flightService;
	private final static String BASE_URI = "/admin";
	
	@RequestMapping(value=BASE_URI, method=RequestMethod.GET)
	public String getMainPage(ModelMap model)
	{
		List<FlightSearch> allSavedSearches = flightService.getAllFlightSavedSearches();
		List<FlightInfo> allFlightData = flightService.getAllSavedFlightInfo();
		List<ApplicationState> allApplicationStates = flightService.getAllApplicationStates();
		model.put("allSavedSearches", allSavedSearches);
		model.put("allFlightData", allFlightData);
		model.put("allApplicationStates", allApplicationStates);
		return "admin"; // the admin page
	}
	
	@RequestMapping(value=BASE_URI+"/job/tracker", method=RequestMethod.GET)
	public @ResponseBody String runFlightUpdate()
	{
		long start = System.currentTimeMillis();
		System.out.println("About to run tracker job for today's date: "+new DateTime());
		flightService.runTracker();
		long end = (System.currentTimeMillis() - start);
		return "Job ran for "+TimeConverter.convertMillisecondTimeToString(end);
	}
	
}
