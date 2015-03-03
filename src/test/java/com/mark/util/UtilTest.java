package com.mark.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.appengine.repackaged.com.google.common.reflect.TypeToken;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.request.GoogleFlightRequestDetail;
import com.mark.model.google.request.Slice;
import com.mark.service.impl.FlightServiceImpl;

public class UtilTest {

	@Test
	public void testPropertiesLoad()
	{
		String url = FlightProperties.getProperty("google.flight.api.baseUrl");
		assertTrue(url != null && url.length() > 0);
		String apiKey = FlightProperties.getProperty("google.flight.api.key");
		assertTrue(apiKey != null && apiKey.length() > 0);
	}
	
	@Test
	public void testRequestParams()
	{
		GoogleFlightRequest gfr = new FlightServiceImpl().createRequest();
		Gson gson = new Gson();
		String s = gson.toJson(gfr);//, new TypeToken<List<Slice>>() {}.getType());
		assertTrue(s != null && s.length() > 0);
	}
	
}
