package com.mark.service.impl;

import org.springframework.stereotype.Service;

import com.mark.service.IFlightService;

@Service
public class FlightServiceImpl implements IFlightService {

	@Override
	public String sayHi() {
		return "Hello!";
	}

	
}
