package com.mark.controller.view.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mark.constant.ControllerConstants;
import com.mark.exception.FlightException;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.service.IFlightTrackerService;
import com.mark.util.converter.DateConverter;

@Controller
public class TrackedViewController {
	private static final String SAVED_SEARCHES_ATTRIBUTE_NAME = "savedSearches";
	
	@Autowired
	private IFlightTrackerService trackedFlightService;
	
	@RequestMapping(value=ControllerConstants.TRACKER_VIEW_URI, method=RequestMethod.GET)
	public String showTrackedPage(ModelMap map)
	{
		List<FlightSavedSearch> list = trackedFlightService.getTrackedFlights();
		if ( list != null )
		{
			map.addAttribute(SAVED_SEARCHES_ATTRIBUTE_NAME, list);
		}
		else
		{
			map.addAttribute(SAVED_SEARCHES_ATTRIBUTE_NAME, new ArrayList<>());
		}
		return ControllerConstants.TRACKER_VIEW_NAME;
	}
	
}
