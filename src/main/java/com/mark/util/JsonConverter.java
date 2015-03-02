package com.mark.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.google.appengine.repackaged.com.google.gson.Gson; //TODO: Understand this warning
import com.mark.exception.FlightException;
import com.mark.model.google.response.GoogleFlightResponse;

public class JsonConverter {

	public static <T> T getObjectFromJson(InputStream in, Class<T> clazz)
	{
		if ( in == null )
		{
			throw new FlightException("Cannot convert null Inputstream of JSON to class ["+clazz+"]", null);
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz", null);
			
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FlightException("Could not attach a reader to inputstream [%s]", e, in);
		}
		T object = getObjectFromJson(reader, clazz);
		try
		{
			reader.close();
		}
		catch(Exception e) {
			// quiet!
		}
		return object;
	}
	
	public static <T> T getObjectFromJson(Reader reader, Class<T> clazz)
	{
		if ( reader == null )
		{
			throw new FlightException("Cannot convert null reader of JSON to class ["+clazz+"]", null);
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz", null);
			
		}
		Gson gson = new Gson();
		try
		{
			T parsedObject = gson.fromJson(reader, clazz);
			return parsedObject;
		}
		catch(Exception e)
		{
			throw new FlightException("Could not extract object of class [%s] from reader stream.", e, clazz);
		}
	}

	public static <T> T convertJsonToObject(String json, Class<T> clazz) 
	{
		if ( json == null )
		{
			throw new FlightException("Cannot convert null JSON to class ["+clazz+"]", null);
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz", null);
			
		}
		Gson gson = new Gson();
		try
		{
			T parsedObject = gson.fromJson(json, clazz);
			return parsedObject;
		}
		catch(Exception e)
		{
			throw new FlightException("Could not extract object of class [%s] from String", e, clazz);
		}
	}
	
	
	
}
