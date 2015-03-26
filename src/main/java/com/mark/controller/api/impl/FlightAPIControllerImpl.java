package com.mark.controller.api.impl;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.constant.ControllerConstants;
import com.mark.controller.api.IFlightAPIController;
import com.mark.exception.FlightException;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;

@Controller
public class FlightAPIControllerImpl implements IFlightAPIController {

	@RequestMapping(value=ControllerConstants.API_URI, method=RequestMethod.POST)
	public @ResponseBody FlightSearchResult postForFlightInfo(@Valid @RequestBody FlightInputSearch flightInputSearch, BindingResult bindingResult)
	{
//		if( bindingResult.hasErrors() )
//		{
//			throw new FlightException("There are problems with the flight search input\n"+bindingResult.getAllErrors());
//		}
//		FlightInfo flightInfo = null;
//		if ( flightData != null)
//		{
//			try
//			{
//				flightInfo = flightService.getFlightInfo(flightData);
//			}
//			catch(FlightException fe)
//			{
//				System.err.println("Caught exception in controller when getting flight info: "+fe.getMessage());
//				flightInfo = new FlightInfo();
//				flightInfo.setExceptionMessage(fe.getMessage());
//				flightInfo.setOrigin(flightData.getOrigin());
//				flightInfo.setDestination(flightData.getDestination());
//			}
//				
//		}
//		return flightInfo;
		return null;
	}
	
	@RequestMapping(value=ControllerConstants.API_HISTORY_URI, method=RequestMethod.POST)
	public @ResponseBody FlightSearchResult postForFlightHistory(@Valid @RequestBody FlightInputSearch flightInputSearch, BindingResult bindingResult)
	{
//		if( bindingResult.hasErrors() )
//		{
//			throw new FlightException("There are problems with the flight search input\n"+bindingResult.getAllErrors());
//		}
//		return flightService.getFlightHistory(flightData);
		return null;
	}

	
	
	
}
