package com.mark.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
		return "Sanity Check OK\nTime: "+new Date();
	}
	
	@RequestMapping("/hello/{name}")
	public @ResponseBody String hello(@PathVariable("name") String name)
	{
		return flightService.sayHi(name)+"<br><br>Time: "+new Date();
	}
	
	@RequestMapping("/hello")
	public @ResponseBody String hello()
	{
		return "Hi!<br><br>Time: "+new Date();
	}
}
