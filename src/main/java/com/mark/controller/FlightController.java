package com.mark.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Controller;

@Controller
@Path(value="/")
public class FlightController {

	@GET
	@Path("/sanity")
	public String testSanity()
	{
		return "Sanity Check OK on "+LocalDateTime.now();
	}
	
}
