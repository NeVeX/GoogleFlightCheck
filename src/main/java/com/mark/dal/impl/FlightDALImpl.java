package com.mark.dal.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.dal.FlightDAL;
import com.mark.model.FlightData;
import com.mark.model.dal.ApplicationState;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.util.converter.DateConverter;

@Repository
public class FlightDALImpl implements FlightDAL {

	private static final String FLIGHT_SEARCH_TABLE = "FlightSavedSearch";
	private static final String DATE = "DATE";
	private static final String DESTINATION = "DESTINATION";
	private static final String ORIGIN = "ORIGIN";
	private static final String SAVED_SEARCH_KEY_ID = "SAVED_SEARCH_KEY_ID";
	private static final String FLIGHT_DATA_TABLE = "FlightData";
	private static final String LOWEST_PRICE = "LOWEST_PRICE";
	private static final String SHORTEST_TIME_PRICE = "SHORTEST_TIME";
	private static final String SEARCH_DATE = "SEARCH_DATE";
	private static final String SHORTEST_PRICE_DURATION = "SHORTEST_PRICE_DURATION";
	private static final String LOWEST_PRICE_DURATION = "LOWEST_PRICE_DURATION";
	
	
	private DatastoreService dataStore;
	
	@PostConstruct
	public void setup()
	{
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
	@Override
	public FlightSavedSearch save(String origin, String destination, DateTime date) {
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDate(date);
		fss.setDestination(destination);
		fss.setOrigin(origin);
		fss.setExistingSearch(false);
		System.out.println("Saving flight search: "+fss.toString());
		Entity en = new Entity(FLIGHT_SEARCH_TABLE);
		en.setProperty(DATE,DateConverter.convertToString(date));
		en.setProperty(DESTINATION,destination);
		en.setProperty(ORIGIN,origin);
		Key key = dataStore.put(en);
		fss.setKey(key);
		return fss;
	}

	@Override
	public FlightSavedSearch find(String origin, String destination, DateTime date) {
		System.out.println("Searching for Saved Flight Search ["+origin+", "+destination+", "+date+"]");
		Filter originCompare = new FilterPredicate(ORIGIN, FilterOperator.EQUAL, origin);
		Filter destinationCompare = new FilterPredicate(DESTINATION, FilterOperator.EQUAL, destination);
		Filter dateCompare = new FilterPredicate(DATE, FilterOperator.EQUAL, DateConverter.convertToString(date));
		Filter allCompares = CompositeFilterOperator.and(originCompare, destinationCompare, dateCompare);
		Query q = new Query(FLIGHT_SEARCH_TABLE).setFilter(allCompares);
		System.out.println("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if ( entity != null)
		{
			System.out.println("Found a match for search");	
			FlightSavedSearch fss = createFlightSavedSearchFromEntity(entity);
			fss.setExistingSearch(true);
			return fss;
		}
		System.out.println("No match found for search");	
		return null;
	}

	private FlightSavedSearch createFlightSavedSearchFromEntity(Entity entity) {
		FlightSavedSearch fss = new FlightSavedSearch();
		fss.setDate(DateConverter.convertToDateTime((String)entity.getProperty(DATE)));
		fss.setDestination(((String)entity.getProperty(DESTINATION)));
		fss.setOrigin(((String)entity.getProperty(ORIGIN)));
		fss.setExistingSearch(true);
		fss.setKey(entity.getKey());
		return fss;	
	}

	@Override
	public FlightData findFlightData(FlightSavedSearch savedSearch) {
		Key key = savedSearch.getKey();
		String searchDate = DateConverter.convertToString(new DateTime()); // get today's string
		Filter keyCompare = new FilterPredicate(SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, key.getId());
		Filter searchDateCompare = new FilterPredicate(SEARCH_DATE, FilterOperator.EQUAL, searchDate);
		Filter allCompares = CompositeFilterOperator.and(keyCompare, searchDateCompare);
		Query q = new Query(FLIGHT_DATA_TABLE).setFilter(allCompares);
		System.out.println("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if (entity != null)
		{
			System.out.println("Found a match for flight data search for key ["+key.getId()+"] and date ["+searchDate+"]");
			return createFlightDataFromEntity(entity);
		}
		System.out.println("No match found for flight saved data for key ["+key.getId()+"]");	
		return null;
	}

	private FlightData createFlightDataFromEntity(Entity entity) {
		FlightData fd = new FlightData();
		fd.setDate(DateConverter.convertToDateTime((String)entity.getProperty(DATE)));
		fd.setDestination((String)entity.getProperty(DESTINATION));
		fd.setOrigin((String)entity.getProperty(ORIGIN));
		fd.setKey(entity.getKey());
		fd.setDateSearched(DateConverter.convertToDateTime((String)entity.getProperty(SEARCH_DATE)));
		fd.setLowestPriceTripDuration((Long) entity.getProperty(LOWEST_PRICE_DURATION));
		fd.setShortestTimePriceTripDuration((Long) entity.getProperty(SHORTEST_PRICE_DURATION));
		Double lowestPrice = (Double) entity.getProperty(LOWEST_PRICE);
		Double shortestPrice = (Double) entity.getProperty(SHORTEST_TIME_PRICE);
		if ( lowestPrice != null )
		{
			fd.setLowestPrice(new Float(lowestPrice));
		}
		if ( shortestPrice != null)
		{
			fd.setShortestTimePrice(new Float(shortestPrice));
		}
		return fd;
	}

	@Override
	public boolean saveFlightData(FlightData fd) {
		System.out.println("Saving Flight Data: "+fd.toString());
		String searchDate = DateConverter.convertToString(new DateTime());
		Entity en = new Entity(FLIGHT_DATA_TABLE);
		en.setProperty(DATE,DateConverter.convertToString(fd.getDate()));
		en.setProperty(DESTINATION,fd.getDestination());
		en.setProperty(ORIGIN,fd.getOrigin());
		en.setProperty(LOWEST_PRICE,fd.getLowestPrice());
		en.setProperty(SHORTEST_TIME_PRICE,fd.getShortestTimePrice());
		en.setProperty(SAVED_SEARCH_KEY_ID, fd.getKey().getId());
		en.setProperty(SEARCH_DATE, searchDate);
		en.setProperty(LOWEST_PRICE_DURATION, fd.getLowestPriceTripDuration());
		en.setProperty(SHORTEST_PRICE_DURATION, fd.getShortestTimePriceTripDuration());
		return dataStore.put(en) != null ? true : false;
	}

	@Override
	public List<FlightSavedSearch> getAllFlightSavedSearches() {
		System.out.println("Getting all Flight saved searches");
		Query q = new Query(FLIGHT_SEARCH_TABLE).addSort(DATE, SortDirection.DESCENDING);
		System.out.println("Query: "+q.toString());
		List<FlightSavedSearch> allFlightSavedSearchs = new ArrayList<FlightSavedSearch>();
		for(Entity en : dataStore.prepare(q).asIterable(FetchOptions.Builder.withLimit(10)))
		{
			if( en != null)
			{
				FlightSavedSearch fss = this.createFlightDataFromEntity(en);
				System.out.println("Found Flight Saved Search: "+fss.toString());
				allFlightSavedSearchs.add(fss);;
			}
		}
		return allFlightSavedSearchs;
	}

	@Override
	public List<FlightData> getAllFlightData() {
		System.out.println("Getting all saved Flight Data");
		Query q = new Query(FLIGHT_DATA_TABLE).addSort(DATE, SortDirection.DESCENDING);
		System.out.println("Query: "+q.toString());
		List<FlightData> allFlightData = new ArrayList<FlightData>();
		for(Entity en : dataStore.prepare(q).asIterable(FetchOptions.Builder.withLimit(10)))
		{
			if( en != null)
			{
				FlightData fd = this.createFlightDataFromEntity(en);
				System.out.println("Found Flight Data: "+fd.toString());
				allFlightData.add(fd);;
			}
		}
		return allFlightData;
	}
}
