package com.mark.dal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
import com.mark.dal.IFlightSearchDAL;
import com.mark.model.FlightData;
import com.mark.model.FlightSearch;
import com.mark.model.dal.ApplicationState;
import com.mark.util.converter.DateConverter;

@Repository
public class FlightSearchDALImpl implements IFlightSearchDAL {

	private static final String FLIGHT_SEARCH_TABLE = "FlightSavedSearch";
	
	private DatastoreService dataStore;
	
	@PostConstruct
	public void setup()
	{
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
	@Override
	public FlightSearch save(String origin, String destination, Date departureDate, Date returnDate) {
		FlightSearch fss = new FlightSearch();
		fss.setDepartureDate(departureDate);
		fss.setReturnDate(returnDate);
		fss.setDestination(destination);
		fss.setOrigin(origin);
		fss.setExistingSearch(false);
		System.out.println("Saving flight search: "+fss.toString());
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Entity en = new Entity(FLIGHT_SEARCH_TABLE, ancestorKey);
		en.setProperty(DEPARTURE_DATE, departureDate);
		en.setProperty(RETURN_DATE, returnDate);
		en.setProperty(DESTINATION,destination);
		en.setProperty(ORIGIN,origin);
		Key key = dataStore.put(en);
		fss.setKey(key);
		System.out.println("Saved flight search: "+fss.toString());
		return fss;
	}

	@Override
	public FlightSearch find(String origin, String destination, Date departureDate, Date returnDate) {
		System.out.println("Searching for Saved Flight Search ["+origin+", "+destination+", "+departureDate+", "+returnDate+"]");
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Filter originCompare = new FilterPredicate(ORIGIN, FilterOperator.EQUAL, origin);
		Filter destinationCompare = new FilterPredicate(DESTINATION, FilterOperator.EQUAL, destination);
		Filter departureDateCompare = new FilterPredicate(DEPARTURE_DATE, FilterOperator.EQUAL, departureDate);
		Filter returnDateCompare = new FilterPredicate(RETURN_DATE, FilterOperator.EQUAL, returnDate);
		Filter allCompares = CompositeFilterOperator.and(originCompare, destinationCompare, departureDateCompare, returnDateCompare);
		Query q = new Query(FLIGHT_SEARCH_TABLE).setAncestor(ancestorKey).setFilter(allCompares);
		System.out.println("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if ( entity != null)
		{
			System.out.println("Found a match for search");	
			FlightSearch fss = createFlightSavedSearchFromEntity(entity);
			fss.setExistingSearch(true);
			return fss;
		}
		System.out.println("No match found for search");	
		return null;
	}

	private FlightSearch createFlightSavedSearchFromEntity(Entity entity) {
		FlightSearch fss = new FlightSearch();
		Date departureDate = (Date)entity.getProperty(DEPARTURE_DATE);
		Date returnDate = (Date)entity.getProperty(RETURN_DATE);
		fss.setDepartureDate(departureDate);
		fss.setReturnDate(returnDate);
		fss.setDestination(((String)entity.getProperty(DESTINATION)));
		fss.setOrigin(((String)entity.getProperty(ORIGIN)));
		fss.setKey(entity.getKey());
		return fss;	
	}

	@Override
	public List<FlightSearch> getAllFlightSavedSearches(boolean includeFutureDatesOnly) {
		System.out.println("Getting all Flight saved searches");
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Query q = new Query(FLIGHT_SEARCH_TABLE).setAncestor(ancestorKey).addSort(DEPARTURE_DATE, SortDirection.DESCENDING);
		if ( includeFutureDatesOnly )
		{
			Date todaysDate = new LocalDate().toDate();
			Filter futureFilter = new FilterPredicate(DEPARTURE_DATE, FilterOperator.GREATER_THAN_OR_EQUAL, todaysDate);
			q.setFilter(futureFilter);
		}
		System.out.println("Query: "+q.toString());
		List<FlightSearch> allFlightSavedSearchs = new ArrayList<FlightSearch>();
		for(Entity en : dataStore.prepare(q).asIterable(FetchOptions.Builder.withLimit(10)))
		{
			if( en != null)
			{
				FlightSearch fss = this.createFlightSavedSearchFromEntity(en);
				System.out.println("Found Flight Saved Search: "+fss.toString());
				allFlightSavedSearchs.add(fss);;
			}
		}
		return allFlightSavedSearchs;
	}

}
