package com.mark.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
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
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.util.converter.DateConverter;

@Controller
public class FlightController {

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
		if (!model.containsKey("flightData"))
		{
			FlightData fd = new FlightData();
			fd.setDepartureDate(new LocalDate());
			fd.setDestination("DUB");
			fd.setOrigin("SFO");
			model.addAttribute("flightData", fd);
		}
		return "main";
	}
	
	@RequestMapping(value=BASE_URL+"/inputs", method=RequestMethod.POST)
	public String getFlightsFromMainPageInputs(@ModelAttribute("flightData") FlightData fii, HttpServletRequest request, RedirectAttributes redirectAttributes)
	{
		if ( fii != null)
		{
			String from = fii.getOrigin();
			String to = fii.getDestination();
			String departureDate = fii.getDepartureDateString();
			String returnDate = fii.getReturnDateString();
			checkInputs(from, to, departureDate);
			FlightData fd;
			try
			{
				fd = flightService.getFlights(from, to, departureDate, returnDate, fii.getForceBatchUsage());
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
			redirectAttributes.addFlashAttribute("flightData", fd);	
		}
		String re = request.getServletPath();
		return "redirect:"+re+BASE_URL;
	}

	private void checkInputs(String from, String to, String date) {
		if ( StringUtils.isEmpty(from))
		{
			throw new FlightException("'From' input is empty/null");
		}
		if ( StringUtils.isEmpty(to))
		{
			throw new FlightException("'To' input is empty/null");
		}
		if ( StringUtils.isEmpty(date))
		{
			throw new FlightException("'Date' input is empty/null");
		}
	}
}
