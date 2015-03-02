package com.mark.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;

@RestController
public class FlightController {

	@Autowired
	private IFlightService flightService;
	
	@RequestMapping(value="/sanity", method=RequestMethod.GET)
	public @ResponseBody String testSanity()
	{
		return "Sanity Check OK\nTime: "+new Date();
	}
	
	@RequestMapping("/hello/{name}")
	public String hello(@PathVariable("name") String name)
	{
		return flightService.sayHi(name)+"<br><br>Time: "+new Date();
	}
	
	@RequestMapping("/hello")
	public String hello()
	{
		return "Hi!<br><br>Time: "+new Date();
	}
	
	@RequestMapping(value="/info/{flight}", /*headers="Accept=application/json",*/ produces = {"application/json"})
	public GoogleFlightResponse getFlightDetails(@PathVariable("flight") String flightInfo)
	{
		return flightService.getFlights(flightInfo);
	}
}
