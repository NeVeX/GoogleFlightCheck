package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import com.mark.exception.FlightException;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.util.JsonConverter;

@Service
public class FlightServiceImpl implements IFlightService {

	@Override
	public String sayHi(String name) {
		return "Hello "+name+"!";
	}

	// For now return the mock data from the google docs
	@Override
	public GoogleFlightResponse getFlights(String information) {
		if ( information != null && information.equals("test"))
		{
			return this.getTestResponse();
		}
		return null; // TODO: Implement this
	}
	
	private GoogleFlightResponse getTestResponse()
	{
		String testDataLocation = "data/example_flight_response.json";
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(testDataLocation);
		if ( in == null )
		{
			throw new FlightException("Could not load inputstream from location [%s]", null, testDataLocation);
		}
		return JsonConverter.getObjectFromJson(in, GoogleFlightResponse.class);
	}
	

	
}
