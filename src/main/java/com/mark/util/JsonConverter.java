package com.mark.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.google.appengine.repackaged.com.google.gson.Gson; //TODO: Understand this warning
import com.mark.exception.FlightException;

public class JsonConverter {

	public static <T> T getObjectFromJson(InputStream in, Class<T> clazz)
	{
		Reader reader = null;
		try {
			reader = new InputStreamReader(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FlightException("Could not attach a reader to inputstream [%s]", e, in);
		}
		return getObjectFromJson(reader, clazz);
	}
	
	public static <T> T getObjectFromJson(Reader reader, Class<T> clazz)
	{
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
	
	
	
}
