package com.mark.util.client.type.resteasy.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.client.IGoogleFlightApiClient;
import com.mark.util.client.mock.GoogleFlightClientMocked;
import com.mark.util.client.type.resteasy.IRestEasyGoogleFlightApiClient;
import com.mark.util.client.type.resteasy.RestEasyClient;

public class RestEasyGoogleFlightApiImpl implements IGoogleFlightApiClient {

	private IRestEasyGoogleFlightApiClient googleFlightApi;
	@PostConstruct
	public void init()
	{
		if (FlightProperties.IN_DEBUG_MODE)
		{
			System.out.println("In debug mode - for the rest easy client, a mocked client will be used instead of calling the real API");
			googleFlightApi = new GoogleFlightClientMocked();
		}
		else
		{
			// get the REAL client we need to call Google
			googleFlightApi = RestEasyClient.getClient(FlightProperties.GOOGLE_FLIGHT_API_BASE_URL, IRestEasyGoogleFlightApiClient.class);
		}
	}
	
	@Override
	public GoogleFlightResponse postForFlightInfo(GoogleFlightRequest gRequest) {
		return googleFlightApi.postForFlightInfo(FlightProperties.GOOGLE_FLIGHT_API_KEY, gRequest);
	}

}
