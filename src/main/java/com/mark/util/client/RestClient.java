package com.mark.util.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import com.mark.exception.FlightException;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.client.mock.GoogleFlightClientMocked;
import com.mark.util.client.type.IGoogleFlightClient;
import com.mark.util.client.type.IRestClient;
import com.mark.util.converter.JsonConverter;

public class RestClient {
	
	private static ResteasyClient client;
	private static Map<String, IRestClient> mockedClients = new HashMap<String, IRestClient>()
	{{
		put(IGoogleFlightClient.class.getName(), new GoogleFlightClientMocked());
	}};
	
	private static boolean debugMode = Boolean.valueOf(FlightProperties.getProperty("debugMode"));
	static
	{
		ClientConnectionManager cm = new BasicClientConnectionManager();
		HttpClient httpClient = new DefaultHttpClient(cm);
		HttpEngineForGAE engine = new HttpEngineForGAE(httpClient);
		client = new ResteasyClientBuilder().httpEngine(engine).build();
		client.register(new RestClientFilter());
	}

	public static <T extends IRestClient> T getClient(String baseUrl, Class<T> clazz)
	{
		if ( baseUrl == null || baseUrl.length() == 0 )
		{
			throw new FlightException("BaseUrl to for client cannot be empty/null", null);
		}
		if ( clazz == null)
		{
			throw new FlightException("Class to use for REST client creation cannot be null", null);
		}
		if ( debugMode)
		{
			System.out.println("In debug mode - determining if can return mocked client");
			if ( mockedClients.containsKey(clazz.getName()))
			{
				System.out.println("Returning a Mocked Client for class ["+clazz+"] instead of real implementation");
				return (T) mockedClients.get(clazz.getName()); // return the defined mocked client
			}
			System.out.println("In debug mode but did not find any mocked clients for class ["+clazz+"]. Proceeding as normal to get real client");
		}
		System.out.println("Creating REST client for class ["+clazz+"]");
        ResteasyWebTarget target = client.target(baseUrl);
        T proxyClient = target.proxy(clazz);
		System.out.println("REST client created for baseUrl ["+baseUrl+"] with class ["+clazz+"]");
        return proxyClient;
	}
	
	private static class RestClientFilter implements ClientRequestFilter
	{
		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			requestContext.getHeaders().add("Content-Type", "application/json");
		}
		
	}
	
	
}
