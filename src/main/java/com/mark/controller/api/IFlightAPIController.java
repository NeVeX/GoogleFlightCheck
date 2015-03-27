package com.mark.controller.api;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;

public interface IFlightAPIController {

	FlightSearchResult postForFlightResults(FlightInputSearch flightInputSearch);
	
	FlightSearchResult postForFlightHistoricalResults(FlightInputSearch flightInputSearch);
	
}
