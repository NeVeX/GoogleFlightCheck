package com.mark.util.client.type;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestBody;

import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.util.FlightProperties;

public interface IGoogleFlightClient extends IRestClient {

	static final String key = FlightProperties.getProperty("google.flight.api.key");
	
	@POST
	@Path("qpxExpress/v1/trips/search?key=AIzaSyBsuoWA1U29zDCg1th3Wp9lCv3Ah8caufs") //TODO : inject the key
	@Produces("application/json")
	@Consumes("application/json")
	String postForFlightInfo(GoogleFlightRequest gRequest);
}
