package com.mark.controller.api;

import org.springframework.validation.BindingResult;

import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;

public interface IFlightAPIController {

	public FlightSearchResult postForFlightInfo(FlightInputSearch flightInputSearch);
	
}
