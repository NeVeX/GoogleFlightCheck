package com.mark.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			fd.setDate(new DateTime());
			fd.setDateString(DateConverter.convertToString(fd.getDate()));
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
			String date = fii.getDateString();
			FlightData fd = flightService.getFlights(from, to, date);
			redirectAttributes.addFlashAttribute("flightData", fd);	
		}
		String re = request.getServletPath() ;
		return "redirect:"+re+BASE_URL;
	}
}
