package com.mark.util.client;

import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;

public interface IGoogleFlightApiClient {

	public GoogleFlightResponse postForFlightInfo(GoogleFlightRequest gRequest);	
}
