package com.mark.util.client;

import java.io.IOException;
import java.io.InputStream;

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
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.util.FlightProperties;
import com.mark.util.JsonConverter;
import com.mark.util.client.type.IRestClient;

public class RestClient {
	
	private static ResteasyClient client;
	
	static
	{
	//	client = new ResteasyClientBuilder().re.build();
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
		
		System.out.println("Creating REST client");
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
