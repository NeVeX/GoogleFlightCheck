package com.mark.dal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.comparator.CompositeFileComparator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.constant.DALConstants;
import com.mark.dal.IFlightSearchDAL;
import com.mark.exception.FlightException;
import com.mark.model.ApplicationState;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightInputSearch;
import com.mark.model.FlightSavedSearch;
import com.mark.util.converter.DateConverter;

@Repository
public class FlightSearchDALImpl implements IFlightSearchDAL {

	
	private DatastoreService dataStore;
	
	@PostConstruct
	public void setup()
	{
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	// TODO: don' return a flight search, use the passed in object to populate
	@Override
	public FlightSavedSearch saveFlightSearch(FlightInputSearch fs) {
		System.out.println("Saving flight search: "+fs.toString());
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Entity en = new Entity(DALConstants.TABLE_FLIGHT_SEARCH, ancestorKey);
		en.setProperty(DALConstants.COLUMN_DEPARTURE_DATE, fs.getDepartureDate());
		en.setProperty(DALConstants.COLUMN_RETURN_DATE, fs.getReturnDate());
		en.setProperty(DALConstants.COLUMN_DESTINATION,fs.getDestination());
		en.setProperty(DALConstants.COLUMN_ORIGIN,fs.getOrigin());
		Key key = dataStore.put(en);
		System.out.println("Saved flight search: "+fs.toString());
		return new FlightSavedSearch(key, fs);
	}


	@Override
	public FlightSavedSearch getFlightSavedSearch(FlightInputSearch fis) {
		System.out.println("Searching for Saved Flight Search ["+fis+"]");
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Filter originCompare = new FilterPredicate(DALConstants.COLUMN_ORIGIN, FilterOperator.EQUAL, fis.getOrigin());
		Filter destinationCompare = new FilterPredicate(DALConstants.COLUMN_DESTINATION, FilterOperator.EQUAL, fis.getDestination());
		Filter departureDateCompare = new FilterPredicate(DALConstants.COLUMN_DEPARTURE_DATE, FilterOperator.EQUAL, fis.getDepartureDate());
		Filter returnDateCompare = new FilterPredicate(DALConstants.COLUMN_RETURN_DATE, FilterOperator.EQUAL, fis.getReturnDate());
		Filter allCompares = CompositeFilterOperator.and(originCompare, destinationCompare, departureDateCompare, returnDateCompare);
		Query q = new Query(DALConstants.TABLE_FLIGHT_SEARCH).setAncestor(ancestorKey).setFilter(allCompares);
		System.out.println("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if ( entity != null)
		{
			System.out.println("Found a match for search ["+fis+"]");	
			FlightSavedSearch fss = createFlightSavedSearchFromEntity(entity);
			return fss;
		}
		System.out.println("No match found for search ["+fis+"]");	
		return null;
	}

	private FlightSavedSearch createFlightSavedSearchFromEntity(Entity entity) {
		FlightSavedSearch fs = new FlightSavedSearch(entity.getKey(), this.createFlightInputSearchFromEntity(entity));
		Boolean flightsExist = (Boolean)entity.getProperty(DALConstants.COLUMN_FLIGHT_OPTION_EXISTS);
		fs.setFlightOptionsExists(flightsExist == null ? true : flightsExist.booleanValue());
		return fs;	
	}
	
	private FlightInputSearch createFlightInputSearchFromEntity(Entity entity) {
		FlightInputSearch fs = new FlightInputSearch();
		Date departureDate = (Date)entity.getProperty(DALConstants.COLUMN_DEPARTURE_DATE);
		Date returnDate = (Date)entity.getProperty(DALConstants.COLUMN_RETURN_DATE);
		fs.setDepartureDate(departureDate);
		fs.setReturnDate(returnDate);
		fs.setDestination(((String)entity.getProperty(DALConstants.COLUMN_DESTINATION)));
		fs.setOrigin(((String)entity.getProperty(DALConstants.COLUMN_ORIGIN)));
		return fs;	
	}
	

	@Override
	public List<FlightSavedSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly) {
		System.out.println("Getting all Flight saved searches");
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Query q = new Query(DALConstants.TABLE_FLIGHT_SEARCH).setAncestor(ancestorKey).addSort(DALConstants.COLUMN_DEPARTURE_DATE, SortDirection.DESCENDING);
		Filter flightsThatExist = new FilterPredicate(DALConstants.COLUMN_FLIGHT_OPTION_EXISTS, FilterOperator.NOT_EQUAL, false);
		if ( includeFutureDatesOnly )
		{
			Date todaysDate = new LocalDate().toDate();
			Filter futureFilter = new FilterPredicate(DALConstants.COLUMN_DEPARTURE_DATE, FilterOperator.GREATER_THAN_OR_EQUAL, todaysDate);
			q.setFilter(CompositeFilterOperator.and(flightsThatExist, futureFilter));
		}
		else
		{
			q.setFilter(flightsThatExist);
		}
		System.out.println("Query: "+q.toString());
		List<FlightSavedSearch> allFlightSavedSearchs = new ArrayList<FlightSavedSearch>();
		for(Entity en : dataStore.prepare(q).asIterable())
		{
			if( en != null)
			{
				FlightSavedSearch fss = this.createFlightSavedSearchFromEntity(en);
				System.out.println("Found Flight Saved Search: "+fss.toString());
				allFlightSavedSearchs.add(fss);;
			}
		}
		return allFlightSavedSearchs;
	}

	@Override
	public boolean updateFlightSavedSearch(FlightSavedSearch fss) {
		try {
			Entity en = dataStore.get(fss.getKey());
			en.setProperty(DALConstants.COLUMN_FLIGHT_OPTION_EXISTS, fss.getFlightOptionsExists());
			return dataStore.put(en) != null ? true : false; // re-save
		} catch (EntityNotFoundException e) {
			System.err.println("Could not find application state using key ["+fss.getKey()+"]");
		}
		return false;
	}
	
	@Override
	public List<FlightSavedSearch> getFlightSearchesThatNeedsTrackingForToday(List<FlightSavedSearch> savedSearches) 
	{
		Date todaysDate = new LocalDate().toDate();
		System.out.println("Getting all Flight Data that needs updating - records with no search date for today");
		List<FlightSavedSearch> searchesToUpdate = new ArrayList<FlightSavedSearch>();
		Filter searchDateCompare = new FilterPredicate(DALConstants.COLUMN_SEARCH_DATE, FilterOperator.EQUAL, todaysDate);
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		for (FlightSavedSearch fss : savedSearches)
		{
			Filter searchKeyCompare = new FilterPredicate(DALConstants.COLUMN_SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, fss.getKey().getId());
			Filter allCompares = CompositeFilterOperator.and(searchKeyCompare, searchDateCompare);
			Query q = new Query(DALConstants.TABLE_FLIGHT_RESULT).setAncestor(ancestorKey).setFilter(allCompares);
			if ( dataStore.prepare(q).asSingleEntity() == null )
			{
				System.out.println("Found Flight Data that needs updating for Search: "+fss.toString());
				searchesToUpdate.add(fss);
			}
		}
		return searchesToUpdate;
	}


	

}
