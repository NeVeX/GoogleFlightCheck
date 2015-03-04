package com.mark.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mark.exception.FlightException;
import com.mark.model.google.request.DepartureTime;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.request.Passengers;
import com.mark.model.google.request.Slice;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.service.IFlightService;
import com.mark.util.FlightProperties;
import com.mark.util.JsonConverter;
import com.mark.util.client.RestClient;
import com.mark.util.client.type.IGoogleFlightClient;

@Service
public class FlightServiceImpl implements IFlightService {

	private String googleBaseUrl = FlightProperties.getProperty("google.flight.api.baseUrl");
	private static boolean debugMode;
	
	static
	{
		debugMode = Boolean.valueOf(FlightProperties.getProperty("debugMode"));
	}
	
	@Override
	public String sayHi(String name) {
		return "Hello "+name+"!";
	}

	// For now return the mock data from the google docs
	@Override
	public GoogleFlightResponse getFlights(String from, String to, String date) {
		if ( debugMode)
		{
			return getTestResponse(); // for debugging, just return mock data instead of using api calls
		}
		// get the client we need to call Google
		IGoogleFlightClient client = RestClient.getClient(googleBaseUrl, IGoogleFlightClient.class);
		GoogleFlightResponse response = client.postForFlightInfo(FlightProperties.getProperty("google.flight.api.key"), createRequest(from, to, date));
		if (response != null)
		{
			return response;
		}
		throw new FlightException("Could not get flight information from google", null);
	}
	
	private GoogleFlightResponse getTestResponse()
	{
		String testDataLocation = "data/example_flight_response.json";
		try( InputStream in = this.getClass().getClassLoader().getResourceAsStream(testDataLocation))
		{
			return JsonConverter.getObjectFromJson(in, GoogleFlightResponse.class);
		}
		catch (Exception e) {
			throw new FlightException("Could not load test data in inputstream from location [%s]", null, testDataLocation);
		}	
	}
	
	public GoogleFlightRequest createRequest(String from, String to, String date)
	{
		GoogleFlightRequest request = new GoogleFlightRequest();
		GoogleFlightRequestDetail gfr = new GoogleFlightRequestDetail();
		Passengers p = new Passengers();
		p.setAdultCount(1);
		p.setChildCount(1);
		gfr.setPassengers(p);
		List<Slice> slices = new ArrayList<>();
		Slice s = new Slice();
		s.setOrigin(from);
		s.setDestination(to);
		s.setDate(date);
		DepartureTime dt = new DepartureTime();
		dt.setEarliestTime("05:00");
		dt.setLatestTime("23:00");
		s.setPermittedDepartureTime(dt);
		slices.add(s);
		gfr.setSlice(slices);
		gfr.setSolutions(10);
		request.setRequest(gfr);
		return request;
	}
}
