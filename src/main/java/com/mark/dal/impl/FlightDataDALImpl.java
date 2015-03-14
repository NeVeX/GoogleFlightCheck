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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.dal.IFlightDataDAL;
import com.mark.model.FlightData;
import com.mark.model.dal.FlightSavedSearch;
import com.mark.util.converter.DateConverter;

@Repository
public class FlightDataDALImpl implements IFlightDataDAL {
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
	public List<FlightData> getAllFlightData() {
		System.out.println("Getting all saved Flight Data");
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Query q = new Query(FLIGHT_DATA_TABLE).setAncestor(ancestorKey).addSort(DEPARTURE_DATE, SortDirection.DESCENDING);
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

	@Override
	public FlightData findFlightData(FlightSavedSearch savedSearch) {
		Key key = savedSearch.getKey();
		LocalDate searchDate = new LocalDate(); // get today's date
		Filter keyCompare = new FilterPredicate(SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, key.getId());
		Filter searchDateCompare = new FilterPredicate(SEARCH_DATE, FilterOperator.EQUAL, searchDate.toDate());
		Filter allCompares = CompositeFilterOperator.and(keyCompare, searchDateCompare);
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Query q = new Query(FLIGHT_DATA_TABLE).setAncestor(ancestorKey).setFilter(allCompares);
		System.out.println("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if (entity != null)
		{
			System.out.println("Found a match for flight data search for key ["+key.getId()+"] and date ["+searchDate+"]");
			FlightData fd =createFlightDataFromEntity(entity);
			fd.setExistingSearch(true);
			return fd;
		}
		System.out.println("No match found for flight saved data for key ["+key.getId()+"] and date ["+searchDate+"]");	
		return null;
	}

	@Override
	public boolean saveFlightData(FlightData fd) {
		fd.setDateSearched(new LocalDate());
		System.out.println("Saving Flight Data: "+fd.toString());
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		Entity en = new Entity(FLIGHT_DATA_TABLE, ancestorKey);
		en.setProperty(DEPARTURE_DATE,fd.getDepartureDate().toDate());
		en.setProperty(RETURN_DATE, fd.getReturnDate() != null ? fd.getReturnDate().toDate() : null);
		en.setProperty(DESTINATION,fd.getDestination());
		en.setProperty(ORIGIN,fd.getOrigin());
		en.setProperty(LOWEST_PRICE,fd.getLowestPrice());
		en.setProperty(SHORTEST_TIME_PRICE,fd.getShortestTimePrice());
		en.setProperty(SAVED_SEARCH_KEY_ID, fd.getKey().getId());
		en.setProperty(SEARCH_DATE, fd.getDateSearched().toDate());
		en.setProperty(LOWEST_PRICE_DURATION, fd.getLowestPriceTripDuration());
		en.setProperty(SHORTEST_PRICE_DURATION, fd.getShortestTimePriceTripDuration());
		return dataStore.put(en) != null ? true : false;
	}
	
	private FlightData createFlightDataFromEntity(Entity entity) {
		FlightData fd = new FlightData();
		Date departureDate = (Date)entity.getProperty(DEPARTURE_DATE);
		Date returnDate = (Date)entity.getProperty(RETURN_DATE);
		Date searchDate = (Date)entity.getProperty(SEARCH_DATE);
		fd.setDepartureDate(LocalDate.fromDateFields(departureDate));
		if ( returnDate != null )
		{
			fd.setReturnDate(LocalDate.fromDateFields(returnDate));
		}
		fd.setDestination((String)entity.getProperty(DESTINATION));
		fd.setOrigin((String)entity.getProperty(ORIGIN));
		fd.setKey(entity.getKey());
		fd.setDateSearched(LocalDate.fromDateFields(searchDate));
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
	public List<FlightSavedSearch> getFlightDataThatNeedsUpdating(List<FlightSavedSearch> savedSearches) 
	{
		Date todaysDate = new LocalDate().toDate();
		System.out.println("Getting all Flight Data that needs updating - records with no search date for today");
		List<FlightSavedSearch> searchesToUpdate = new ArrayList<FlightSavedSearch>();
		Filter searchDateCompare = new FilterPredicate(SEARCH_DATE, FilterOperator.EQUAL, todaysDate);
		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
		for (FlightSavedSearch fss : savedSearches)
		{
			Filter searchKeyCompare = new FilterPredicate(SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, fss.getKey().getId());
			Filter allCompares = CompositeFilterOperator.and(searchKeyCompare, searchDateCompare);
			Query q = new Query(FLIGHT_DATA_TABLE).setAncestor(ancestorKey).setFilter(allCompares);
			if ( dataStore.prepare(q).asSingleEntity() == null )
			{
				System.out.println("Found Flight Data that needs updating for Search: "+fss.toString());
				searchesToUpdate.add(fss);
			}
		}
		return searchesToUpdate;
	}

}
