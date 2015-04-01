package com.mark.controller.api.impl;

import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.constant.ControllerConstants;
import com.mark.controller.api.IAdminAPIController;
import com.mark.service.IAdminService;
import com.mark.service.IFlightService;
import com.mark.util.converter.TimeConverter;

@Controller
public class AdminAPIControllerImpl implements IAdminAPIController {

	private static final Logger log = Logger.getLogger(AdminAPIControllerImpl.class.getName()); 
	
	@Autowired
	private IAdminService adminService;
	
	@RequestMapping(value=ControllerConstants.ADMIN_API_TRACKER_URI, method=RequestMethod.GET)
	public @ResponseBody String runTracker()
	{
		long start = System.currentTimeMillis();
		log.info("About to run tracker job for today's date: "+new DateTime());
		adminService.runTracker();
		long end = (System.currentTimeMillis() - start);
		String msg = "Job ran for "+TimeConverter.convertMillisecondTimeToString(end);
		log.info(msg);
		return msg;
	}
	
	
}
