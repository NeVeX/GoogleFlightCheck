package com.mark.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.mark.model.google.response.GoogleFlightResponse;
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
	public String hello(@PathVariable("name") String name)
	{
		return flightService.sayHi(name)+"<br><br>Time: "+new Date();
	}
	
	@RequestMapping("/hello")
	public String hello()
	{
		return "Hi!<br><br>Time: "+new Date();
	}
	
//	@RequestMapping(value="/info/{flight}", /*headers="Accept=application/json",*/ produces = {"application/json"})
//	public GoogleFlightResponse getFlightDetails(@PathVariable("flight") String flightInfo)
//	{
//		return flightService.getFlights(flightInfo);
//	}
	
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String getMainPage(ModelMap model)
	{
		return "main";
	}
	
	@RequestMapping(value="/main/inputs", method=RequestMethod.POST)
	public String getFlightsFromMainPageInputs(@RequestParam Map map, HttpServletRequest request)
	{
		//{date=[2015-03-10], fromText=[markMe], toText=[markTo]} --- data should read like this
		if ( map != null)
		{
			String from = (String) map.get("fromText");
			String to = (String) map.get("toText");
			String date = (String) map.get("date");
			GoogleFlightResponse gfr = flightService.getFlights(from, to, date);
		}
		String re = request.getServletPath() ;
		return "redirect:"+re+ "/main";
	}
}
