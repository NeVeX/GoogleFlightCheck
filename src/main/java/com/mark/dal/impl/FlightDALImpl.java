package com.mark.dal.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.dal.FlightDAL;
import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;

@Repository
public class FlightDALImpl implements FlightDAL {

	private static final String FLIGHT_SEARCH_TABLE = "FlightSavedSearch";
	private static final String DATE = "DATE";
	private static final String DESTINATION = "DESTINATION";
	private static final String ORIGIN = "ORIGIN";
	private static final String KEY = "KEY";
	private static final String SAVED_SEARCH_KEY = "SAVED_SEARCH_KEY";
	private static final String FLIGHT_DATA_TABLE = "FlightData";
	private static final String LOWEST_PRICE = "LOWEST_PRICE";
	private static final String SHORTEST_TIME_PRICE = "SHORTEST_TIME";
	
	
	@Override
	public FlightSavedSearch save(String origin, String destination, String date) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity en = new Entity(FLIGHT_SEARCH_TABLE);
		en.setProperty(DATE,date);
		en.setProperty(DESTINATION,destination);
		en.setProperty(ORIGIN,origin);
		Key key = datastore.put(en);
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDate(date);
		fss.setDestination(destination);
		fss.setKey(key);
		fss.setOrigin(origin);
		fss.setExistingSearch(false);
		return fss;
	}

	@Override
	public FlightSavedSearch find(String origin, String destination, String date) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter originCompare = new FilterPredicate(ORIGIN, FilterOperator.EQUAL, origin);
		Filter destinationCompare = new FilterPredicate(DESTINATION, FilterOperator.EQUAL, destination);
		Filter dateCompare = new FilterPredicate(DATE, FilterOperator.EQUAL, date);
		Filter allCompares = CompositeFilterOperator.and(originCompare, destinationCompare, dateCompare);
		Query q = new Query(FLIGHT_SEARCH_TABLE).setFilter(allCompares);
		System.out.println("Searching for data: "+q.toString());
		List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
		if ( list != null && list.size() > 0)
		{
			System.out.println("Found a match for search");	
			return createFlightSavedSearchFromEntity(list.get(0));
		}
		System.out.println("No match found for search");	
		return null;
	}

	private FlightSavedSearch createFlightSavedSearchFromEntity(Entity entity) {
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDate((String)entity.getProperty(DATE));
		fss.setDestination(((String)entity.getProperty(DESTINATION)));
		fss.setOrigin(((String)entity.getProperty(ORIGIN)));
		fss.setExistingSearch(true);
		fss.setKey((Key)entity.getProperty(KEY));
		return fss;	
	}

	@Override
	public FlightData findFlightData(FlightSavedSearch savedSearch) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = savedSearch.getKey();
		Filter keyCompare = new FilterPredicate(SAVED_SEARCH_KEY, FilterOperator.EQUAL, key.getId());
		Query q = new Query(FLIGHT_SEARCH_TABLE).setFilter(keyCompare).addSort("DATE", SortDirection.DESCENDING);
		List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
		if (list != null && list.size() > 0)
		{
			System.out.println("Found a match for flight data search for key ["+key.getId()+"]");
			return createFlightDataFromEntity(list.get(0), savedSearch);
			
		}
		System.out.println("No match found for flight saved data for key ["+key.getId()+"]");	
		return null;
	}

	
	
	private FlightData createFlightDataFromEntity(Entity entity, FlightSavedSearch fss) {
		FlightData fd = new FlightData();
		fd.setDate(fss.getDate());
		fd.setDestination(fss.getDestination());
		fd.setOrigin(fss.getOrigin());
		
		return fd;
	}

	@Override
	public boolean saveFlightData(FlightData fd) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity en = new Entity(FLIGHT_DATA_TABLE);
		en.setProperty(DATE,fd.getDate());
		en.setProperty(DESTINATION,fd.getDestination());
		en.setProperty(ORIGIN,fd.getOrigin());
		en.setProperty(LOWEST_PRICE,fd.getLowestPrice());
		en.setProperty(SHORTEST_TIME_PRICE,fd.getShortestTimePrice());
		en.setProperty(SAVED_SEARCH_KEY, fd.getKey());
		Key key = datastore.put(en);
		return true;
	}
	
	
	
	
	

}
