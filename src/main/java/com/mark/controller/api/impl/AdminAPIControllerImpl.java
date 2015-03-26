package com.mark.controller.api.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.constant.ControllerConstants;
import com.mark.controller.api.IAdminAPIController;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.converter.TimeConverter;

public class AdminAPIControllerImpl implements IAdminAPIController {

	@Autowired
	private IAdminService adminService;
	
	@RequestMapping(value=ControllerConstants.ADMIN_API_TRACKER_URI, method=RequestMethod.GET)
	public @ResponseBody String runTracker()
	{
		long start = System.currentTimeMillis();
		System.out.println("About to run tracker job for today's date: "+new DateTime());
		adminService.runTracker();
		long end = (System.currentTimeMillis() - start);
		return "Job ran for "+TimeConverter.convertMillisecondTimeToString(end);
	}
	
	
}
