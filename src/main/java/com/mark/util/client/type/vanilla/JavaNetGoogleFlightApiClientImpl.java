package com.mark.util.client.type.vanilla;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.mark.exception.FlightException;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.client.IGoogleFlightApiClient;
import com.mark.util.client.mock.GoogleFlightClientMocked;
import com.mark.util.client.type.resteasy.RestEasyClient;
import com.mark.util.converter.JsonConverter;

@Service
public class JavaNetGoogleFlightApiClientImpl implements IGoogleFlightApiClient {
	private static final Logger log = Logger.getLogger(JavaNetGoogleFlightApiClientImpl.class.getName());
	private static String FLIGHT_URL = FlightProperties.GOOGLE_FLIGHT_API_BASE_URL + FlightProperties.GOOGLE_FLIGHT_API_BASE_URI;
	
	@Override
	public GoogleFlightResponse postForFlightInfo(GoogleFlightRequest gRequest) 
	{
		if (FlightProperties.IN_DEBUG_MODE)
		{
			log.info("In debug mode - for the rest easy client, a mocked client will be used instead of calling the real API");
			return new GoogleFlightClientMocked().postForFlightInfo(FlightProperties.GOOGLE_FLIGHT_API_KEY, gRequest);
		}
		log.info("Sending 'POST' request to Google Flight URL ["+FLIGHT_URL+"]\n     with data ["+gRequest+"]");
		try
		{
			URL obj = new URL(FLIGHT_URL+"?key="+FlightProperties.GOOGLE_FLIGHT_API_KEY);
			// even though this will be a https connection, we can only use http here (URL Fetch on GAE will know to use https)
			HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
			// set up the connection
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoInput(true); // to be able to POST
			con.setDoOutput(true); // to get results
			con.setUseCaches(false);
			con.setReadTimeout(60000); // set it big
			con.setConnectTimeout(60000); // set it big
			String jsonString = JsonConverter.getJsonFromObject(gRequest, GoogleFlightRequest.class); // convert what we need in the POST 
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(jsonString); // write to the stream
			wr.flush();
			wr.close(); // send it off
			int responseCode = con.getResponseCode(); // wait to get the response
			GoogleFlightResponse gfr = JsonConverter.getObjectFromJson(con.getInputStream(), GoogleFlightResponse.class); // deserialize the response
			log.info("Google Flight API returned with data");
			return gfr;
		}
		catch(Exception e)
		{
			throw new FlightException("There were problems trying to connect and get a response from the Google Flight API", e);
		}
		
	}
}
