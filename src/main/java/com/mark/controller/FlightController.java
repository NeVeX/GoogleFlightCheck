package com.mark.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.service.IFlightService;

@Controller
public class FlightController {

	@Autowired
	private IFlightService flightService;
	
	@RequestMapping(value="/sanity", method=RequestMethod.GET)
	public @ResponseBody String testSanity()
	{
		return "Sanity Check OK\nTime: "+LocalDateTime.now();
	}
	

	@RequestMapping("/hello")
	public @ResponseBody String hello()
	{
		return "Computer says: "+flightService.sayHi()+"\nTime: "+LocalDateTime.now();
	}
	
}
