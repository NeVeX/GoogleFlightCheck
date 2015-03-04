package com.mark.dal.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.mark.dal.FlightDAL;

@Repository
public class FlightDALImpl implements FlightDAL {

	@Override
	public boolean save(String origin, String destination, String date) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity en = new Entity("FlightSearch");
		en.setProperty("DATE",date);
		en.setProperty("DESTINATION",destination);
		en.setProperty("ORIGIN",origin);
		Key key = datastore.put(en);
		return true;// createFlightInputInformation(en);
	}

	@Override
	public boolean search(String origin, String destination, String date) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter originCompare = new FilterPredicate("ORIGIN", FilterOperator.EQUAL, origin);
		Filter destinationCompare = new FilterPredicate("DESTINATION", FilterOperator.EQUAL, destination);
		Filter dateCompare = new FilterPredicate("DATE", FilterOperator.EQUAL, date);
		Filter allCompares = CompositeFilterOperator.and(originCompare, destinationCompare, dateCompare);
		Query q = new Query("FlightSearch").setFilter(allCompares);
		System.out.println("Searching for data: "+q.toString());
		List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
		if ( list != null && list.size() > 0)
		{
			System.out.println("Found a match for search");	
			return true;//createFlightInputInformation(list.get(0));
		}
		System.out.println("No match found for search");	
		return false;
	}
	
//	private FlightInputInformation createFlightInputInformation(Entity entity)
//	{
//		FlightInputInformation fii = new FlightInputInformation();
//		fii.setDate((String)entity.getProperty("ORIGIN"));
//		fii.setDestination((String)entity.getProperty("DESTINATION"));
//		fii.setOrigin((String)entity.getProperty("DATE"));
//		return fii;
//	}
	
	

}
