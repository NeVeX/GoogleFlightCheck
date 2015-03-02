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
		// get the client we need to call Google
		IGoogleFlightClient client = RestClient.getClient(googleBaseUrl, IGoogleFlightClient.class);
		String stringResponse = client.postForFlightInfo(createRequest());
		if (stringResponse != null)
		{
			return JsonConverter.convertJsonToObject(stringResponse, GoogleFlightResponse.class);
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
	
	public GoogleFlightRequest createRequest()
	{
		GoogleFlightRequest gfr = new GoogleFlightRequest();
		Passengers p = new Passengers();
		p.setAdultCount(1);
		p.setChildCount(1);
		gfr.setPassengers(p);
		List<Slice> slices = new ArrayList<>();
		Slice s = new Slice();
		s.setOrigin("SFO");
		s.setDestination("LON");
		s.setDate("2015-09-19");
		DepartureTime dt = new DepartureTime();
		dt.setEarliestTime("05:00");
		dt.setLatestTime("23:00");
		s.setPermittedDepartureTime(dt);
		slices.add(s);
		gfr.setSlice(slices);
		return gfr;
	}
	
//		  "request": {
//		    "passengers": {
//		      "adultCount": 1,
//		      "childCount": 1
//		    },
//		    "slice": [
//		      {
//		        "origin": "OGG",
//		        "destination": "NCE",
//		        "date": "2014-09-19",
//		        "permittedDepartureTime":
//		        {
//		          "kind": "qpxexpress#timeOfDayRange",
//		          "earliestTime": "22:00",
//		          "latestTime": "23:00"
//		        }
//		      },
//		      {
//		        "origin": "NCE",
//		        "destination": "OGG",
//		        "date": "2014-09-28",
//		        "permittedDepartureTime":
//		        {
//		          "kind": "qpxexpress#timeOfDayRange",
//		          "earliestTime": "05:00",
//		          "latestTime": "12:00"
//		        }
//		      }
//		    ],
//		    "maxPrice": "USD5400",
//		    "solutions": 10
//		  }
//		}
	

	
}
