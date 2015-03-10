package com.mark.util.client.type.resteasy;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.Body;
import org.springframework.web.bind.annotation.RequestBody;

import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.client.IGoogleFlightApiClient;

public interface IRestEasyGoogleFlightApiClient extends IGoogleFlightApiClient, IRestEasyType {
	
	@POST
	@Path("/qpxExpress/v1/trips/search") //TODO : inject the key
	@Produces("application/json")
	@Consumes("application/json")
	GoogleFlightResponse postForFlightInfo(@QueryParam("key") String key, @RequestBody GoogleFlightRequest gRequest);
	
}

