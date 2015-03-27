package com.mark.controller.api.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.constant.ControllerConstants;
import com.mark.controller.api.IFlightAPIController;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;
import com.mark.service.IFlightService;

@Controller
public class FlightAPIControllerImpl implements IFlightAPIController {

	@Autowired
	private IFlightService flightService;
	
	@RequestMapping(value=ControllerConstants.API_URI, method=RequestMethod.POST)
	public @ResponseBody FlightSearchResult postForFlightInfo(@Valid @RequestBody FlightInputSearch flightInputSearch)
	{
//		if( bindingResult.hasErrors() || flightInputSearch == null)
//		{
//			throw new FlightException("There are problems with the flight search input\n"+bindingResult.getAllErrors());
//		}
		return this.flightService.getFlightHistoricalResult(flightInputSearch);	
	}
	
}
