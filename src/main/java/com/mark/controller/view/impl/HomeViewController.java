package com.mark.controller.view.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import com.mark.controller.view.IHomeViewController;
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
public class HomeViewController implements IHomeViewController {
	public static final String FLIGHT_SEARCH_RESULT = "flightResult";
	public static final String FLIGHT_INPUT_SEARCH = "flightInputSearch";

	@Autowired
	private IFlightAPIController iFlightApiController;

	@RequestMapping(value = ControllerConstants.HOME_VIEW_URI, method = RequestMethod.GET)
	public String getMainPage(ModelMap model) {
		if (!model.containsKey(FLIGHT_INPUT_SEARCH)) {
			FlightInputSearch fis = this.createDefaultFlightInfo();
			// add defaulted data
			model.addAttribute(FLIGHT_INPUT_SEARCH, fis);
			model.addAttribute(FLIGHT_SEARCH_RESULT,
					this.createDefaultFlightSearchResult(fis));
		}
		return ControllerConstants.HOME_VIEW_NAME;
	}

	@RequestMapping(value = ControllerConstants.HOME_VIEW_URI, method = RequestMethod.POST)
	public String postDataToMainPage(
			@Valid @ModelAttribute(FLIGHT_INPUT_SEARCH) FlightInputSearch flightInputSearch,
			BindingResult bindingResult, ModelMap model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(ControllerConstants.VIEW_ERROR_FLAG, true); // tell
																			// the
																			// user
																			// in
																			// general
																			// there
																			// is
																			// an
																			// error
			model.addAttribute(FLIGHT_SEARCH_RESULT,
					this.createDefaultFlightSearchResult(flightInputSearch));
			return ControllerConstants.HOME_VIEW_NAME; // go back to the page
														// since there are
														// errors with the input
		}
		FlightSearchResult flightSearchResult = this.iFlightApiController
				.postForFlightResults(flightInputSearch);
		if (flightSearchResult != null) {

			model.addAttribute(FLIGHT_INPUT_SEARCH,
					flightSearchResult.getFlightSearch());
			model.addAttribute(FLIGHT_SEARCH_RESULT, flightSearchResult);
		}
		return ControllerConstants.HOME_VIEW_NAME;
	}

	private FlightInputSearch createDefaultFlightInfo() {
		FlightInputSearch fis = new FlightInputSearch();
		fis.setDepartureDate(DateConverter.toDate(new LocalDate(DateTimeZone.UTC).plusMonths(6)));
		fis.setDestination("DUB");
		fis.setOrigin("SFO");
		return fis;
	}

	// TODO: Get rid of this, this should not happen, redesign the model object
	// inheritance/create dto's.
	private FlightSearchHistoricalResult createDefaultFlightSearchResult(
			FlightInputSearch fis) {
		FlightResult fr = new FlightResult((Date) null);
		FlightSavedSearch fss = new FlightSavedSearch(KeyFactory.createKey(
				"this_is", "terrible"), fis);
		FlightSearchResult fsr = new FlightSearchResult(fr, fss);
		FlightSearchHistoricalResult fhr = new FlightSearchHistoricalResult(fsr);
		return fhr;
	}

}
