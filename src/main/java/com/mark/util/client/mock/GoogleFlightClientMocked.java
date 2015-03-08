package com.mark.util.client.mock;

import java.io.InputStream;

import com.mark.exception.FlightException;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.JsonConverter;
import com.mark.util.client.type.IGoogleFlightClient;


public class GoogleFlightClientMocked implements IGoogleFlightClient
{
	private final static String testDataFile =  "data/example_flight_response.json";
	@Override
	public GoogleFlightResponse postForFlightInfo(String key, GoogleFlightRequest gRequest) {
		try( InputStream in = this.getClass().getClassLoader().getResourceAsStream(testDataFile))
		{
			return JsonConverter.getObjectFromJson(in, GoogleFlightResponse.class);
		}
		catch (Exception e) {
			throw new FlightException("Could not load test data in inputstream from location [%s]", null, testDataFile);
		}	
	}
	
}

