package com.mark.util.converter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.mark.exception.FlightException;

public class JsonConverter {

	public static <T> String getJsonFromObject(T obj, Class<T> clazz)
	{
		if ( obj == null )
		{
			throw new FlightException("Cannot convert null Object of class ["+clazz+"] into JSON");
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz");
			
		}
		try
		{
			Gson gson = new Gson();
			return gson.toJson(obj, clazz);
		}
		catch(Exception e)
		{
			throw new FlightException("Could not convert obj type ["+obj.getClass().getName()+"] into JSON");
		}
	}
	
	
	public static <T> T getObjectFromJson(InputStream in, Class<T> clazz)
	{
		if ( in == null )
		{
			throw new FlightException("Cannot convert null Inputstream of JSON to class ["+clazz+"]");
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz");
			
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
			throw new FlightException("Cannot convert null reader of JSON to class ["+clazz+"]");
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz");
			
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
			throw new FlightException("Cannot convert null JSON to class ["+clazz+"]");
		}
		if ( clazz == null)
		{
			throw new FlightException("Cannot convert JSON to null clazz");
			
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
