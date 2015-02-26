package com.mark.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mark.service.IFlightService;

@Controller
@Path(value="/")
public class FlightController {

	@Autowired
	private IFlightService flightService;
	
	@GET
	@Path("/sanity")
	public String testSanity()
	{
		return "Sanity Check OK\nTime: "+LocalDateTime.now();
	}
	
	@GET
	@Path("/hello")
	public String hello()
	{
		return "Computer says: "+flightService.sayHi()+"\nTime: "+LocalDateTime.now();
	}
	
}
