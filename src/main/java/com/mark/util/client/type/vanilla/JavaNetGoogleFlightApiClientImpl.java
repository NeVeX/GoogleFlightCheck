package com.mark.util.client.type.vanilla;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.mark.exception.FlightException;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.client.IGoogleFlightApiClient;
import com.mark.util.client.mock.GoogleFlightClientMocked;
import com.mark.util.converter.JsonConverter;

@Service
public class JavaNetGoogleFlightApiClientImpl implements IGoogleFlightApiClient {

	private static String FLIGHT_URL = FlightProperties.GOOGLE_FLIGHT_API_BASE_URL + FlightProperties.GOOGLE_FLIGHT_API_BASE_URI;
	
	@Override
	public GoogleFlightResponse postForFlightInfo(GoogleFlightRequest gRequest) 
	{
		if (FlightProperties.IN_DEBUG_MODE)
		{
			System.out.println("In debug mode - for the rest easy client, a mocked client will be used instead of calling the real API");
			return new GoogleFlightClientMocked().postForFlightInfo(FlightProperties.GOOGLE_FLIGHT_API_KEY, gRequest);
		}
		System.out.println("Sending 'POST' request to Google Flight URL ["+FLIGHT_URL+"]\n     with data ["+gRequest+"]");
		try
		{
			//TODO: TIDY UP THIS MESS
			
			URL obj = new URL(FLIGHT_URL+"?key="+FlightProperties.GOOGLE_FLIGHT_API_KEY);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection(); // even though this will be a https connection, we can only use http here (URL Fetch on GAE will know to use https)
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
//			String urlParameters = URLEncoder.encode();
			// Send post request
			con.setDoInput (true);
			con.setDoOutput (true);
			con.setUseCaches (false);
			con.setReadTimeout(60000);
			con.setConnectTimeout(60000);
//			con.connect();
			String jsonString = JsonConverter.getJsonFromObject(gRequest, GoogleFlightRequest.class);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes(urlParameters);
			wr.writeBytes(jsonString);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			GoogleFlightResponse gfr = JsonConverter.getObjectFromJson(con.getInputStream(), GoogleFlightResponse.class);
			System.out.println("Google Flight API returned with data");
			return gfr;
		}
		catch(Exception e)
		{
			throw new FlightException("There were problems trying to connect and get a response from the Google Flight API", e);
		}
		
	}
}
