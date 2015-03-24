package com.mark.controller;

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

import com.mark.exception.FlightException;
import com.mark.model.FlightInfo;
import com.mark.model.FlightSearch;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.service.ITrackedFlightService;
import com.mark.util.converter.DateConverter;

@Controller
public class TrackedFlightController {
	private static final String BASE_URL = "/tracked";
	private static final String TRACKED_VIEW_NAME = "tracked";
	private static final String SAVED_SEARCHES_ATTRIBUTE_NAME = "savedSearches";
	
	@Autowired
	private ITrackedFlightService trackedFlightService;
	
	@RequestMapping(value=BASE_URL, method=RequestMethod.GET)
	public String showTrackedPage(ModelMap map)
	{
		List<FlightSearch> list = trackedFlightService.getTrackedFlights();
		if ( list != null )
		{
			map.addAttribute(SAVED_SEARCHES_ATTRIBUTE_NAME, list);
		}
		else
		{
			map.addAttribute(SAVED_SEARCHES_ATTRIBUTE_NAME, new ArrayList<>());
		}
		return TRACKED_VIEW_NAME;
	}
	
}
