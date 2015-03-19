package com.mark.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mark.exception.FlightException;
import com.mark.model.FlightData;
import com.mark.model.FlightSearch;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.util.converter.DateConverter;

@Controller
public class FlightController {
	private static final String MAIN_PAGE_STRING = "main";
	private static final String FLIGHT_DATA_OBJECT = "flightData";
	private static final String BASE_URL = "/main";
	
	@Autowired
	private IFlightService flightService;
	
	@RequestMapping(value="/sanity", method=RequestMethod.GET)
	public @ResponseBody String testSanity()
	{
		return "Sanity Check OK\nTime: "+new Date();
	}
	
	@RequestMapping(value=BASE_URL, method=RequestMethod.GET)
	public String getMainPage(ModelMap model)
	{
		if (!model.containsKey(FLIGHT_DATA_OBJECT))
		{
			FlightData fs = new FlightData();
			fs.setDepartureDate(DateConverter.toDate(new LocalDate().plusMonths(6)));
			fs.setDestination("DUB");
			fs.setOrigin("SFO");
			model.addAttribute(FLIGHT_DATA_OBJECT, fs);
		}
		return MAIN_PAGE_STRING;
	}
	
	@RequestMapping(value=BASE_URL, method=RequestMethod.POST)
	public String postDataToMainPage(@Valid @ModelAttribute(FLIGHT_DATA_OBJECT) FlightData flightData, BindingResult bindingResult, ModelMap model)
	{
		if ( bindingResult.hasErrors())
		{
			return MAIN_PAGE_STRING; // go back to the page since there are errors with the input
		}
		if ( flightData != null)
		{
			String from = flightData.getOrigin();
			String to = flightData.getDestination();
			String departureDate = flightData.getDepartureDateString();
			String returnDate = flightData.getReturnDateString();
			FlightData fd;
			try
			{
				fd = flightService.getFlights(from, to, departureDate, returnDate, flightData.getForceBatchUsage());
			}
			catch(FlightException fe)
			{
				System.err.println("Caught exception in controller when getting flight info: "+fe.getMessage());
				fd = new FlightData();
				fd.setExceptionMessage(fe.getMessage());
				fd.setOrigin(from);
				fd.setDestination(to);
				fd.setDepartureDateString(departureDate);
			}
			model.addAttribute(FLIGHT_DATA_OBJECT, fd);	
		}
		return MAIN_PAGE_STRING;
	}
}
