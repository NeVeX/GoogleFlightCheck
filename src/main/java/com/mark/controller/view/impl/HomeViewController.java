package com.mark.controller.view.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.mark.constant.ControllerConstants;
import com.mark.controller.api.IFlightAPIController;
import com.mark.exception.FlightException;
import com.mark.model.FlightResult;
import com.mark.model.FlightSearchHistoricalResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightSavedSearch;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.util.converter.DateConverter;

@Controller
public class HomeViewController {
	public static final String FLIGHT_DATA_OBJECT = "flightData";
	
	@Autowired
	private IFlightAPIController iFlightApiController;
	
	@RequestMapping(value=ControllerConstants.HOME_VIEW_URI, method=RequestMethod.GET)
	public String getMainPage(ModelMap model)
	{
		if (!model.containsKey(FLIGHT_DATA_OBJECT))
		{
			// add defaulted data
			model.addAttribute(FLIGHT_DATA_OBJECT, this.createDefaultFlightInfo());
		}
		return ControllerConstants.HOME_VIEW_NAME;
	}
	
	@RequestMapping(value=ControllerConstants.HOME_VIEW_URI, method=RequestMethod.POST)
	public String postDataToMainPage(@Valid @ModelAttribute FlightInputSearch flightInputSearch, BindingResult bindingResult, ModelMap model)
	{
		if ( bindingResult.hasErrors())
		{
			model.addAttribute(ControllerConstants.VIEW_ERROR_FLAG, true); // tell the user in general there is an error
			return ControllerConstants.HOME_VIEW_NAME; // go back to the page since there are errors with the input
		}
		FlightSearchResult flightInfo = this.iFlightApiController.postForFlightInfo(flightInputSearch, bindingResult);
		if (flightInfo != null)
		{
			model.addAttribute(FLIGHT_DATA_OBJECT, flightInfo);	
		}
		return ControllerConstants.HOME_VIEW_NAME;
	}
		
	
	private FlightSearchHistoricalResult createDefaultFlightInfo()
	{
		FlightInputSearch fis = new FlightInputSearch();
		fis.setDepartureDate(DateConverter.toDate(new LocalDate().plusMonths(6)));
		fis.setDestination("DUB");
		fis.setOrigin("SFO");
		FlightSavedSearch fss = new FlightSavedSearch(KeyFactory.createKey("blah", "blah"), fis);
		FlightSearchResult fsr = new FlightSearchResult(new FlightResult((Date)null), fss);
		FlightSearchHistoricalResult flightInfo = new FlightSearchHistoricalResult(fsr);
		return flightInfo;
	}
}
