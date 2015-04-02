package com.mark.controller.api.impl;

import java.util.List;
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
	private static final String LINE_BREAK = "<br>";
	@Autowired
	private IAdminService adminService;
	
	@RequestMapping(value=ControllerConstants.ADMIN_API_TRACKER_URI, method=RequestMethod.GET)
	public @ResponseBody String runTracker()
	{
		long start = System.currentTimeMillis();
		log.info("About to run tracker job for today's date: "+new DateTime());
		List<String> infoList = adminService.runTracker();
		long end = (System.currentTimeMillis() - start);
		StringBuilder outputMessage = new StringBuilder(LINE_BREAK+"Job ran for "+TimeConverter.convertMillisecondTimeToString(end)+LINE_BREAK);
		log.info(outputMessage.toString());
		if ( infoList != null && !infoList.isEmpty())
		{
			outputMessage.append(LINE_BREAK+LINE_BREAK+"The Following Problems Were Encountered:"+LINE_BREAK);
			for(String s : infoList)
			{
				outputMessage.append(" - "+s+LINE_BREAK);
			}

			outputMessage.append(LINE_BREAK+LINE_BREAK);
		}
		else
		{
			outputMessage.append(LINE_BREAK+LINE_BREAK+"No Problems Encountered."+LINE_BREAK);
		}
		
		return outputMessage.toString();
	}
	
	
}
