package com.mark.dal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

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
import com.mark.constant.DALConstants;
import com.mark.dal.IFlightResultDAL;
import com.mark.model.FlightResult;
import com.mark.model.FlightSearchResult;
import com.mark.model.FlightSavedSearch;
import com.mark.util.converter.DateConverter;

@Repository
public class FlightResultDAL implements IFlightResultDAL {
	private DatastoreService dataStore;

	private static final Logger log = Logger.getLogger(FlightResultDAL.class.getName()); 
	
	@PostConstruct
	public void setup()
	{
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
//	@Override
//	public List<FlightResult> getAllSavedFlightResults() {
//		log.info("Getting all saved Flight Data");
//		Key ancestorKey = KeyFactory.createKey(FLIGHT_ANCESTOR_KIND, FLIGHT_ANCESTOR_ID);
//		Query q = new Query(FLIGHT_DATA_TABLE).setAncestor(ancestorKey).addSort(DEPARTURE_DATE, SortDirection.DESCENDING);
//		log.info("Query: "+q.toString());
//		List<FlightResult> allFlightResults = new ArrayList<FlightSearchResult>();
//		for(Entity en : dataStore.prepare(q).asIterable())
//		{
//			if( en != null)
//			{
//				FlightSearchResult fd = this.createFlightResultFromEntity(en);
//				log.info("Found Flight Data: "+fd.toString());
//				allFlightData.add(fd);;
//			}
//		}
//		return allFlightData;
//	}
//	
	@Override
	public List<FlightResult> getFlightSearchResultHistory(FlightSavedSearch savedSearch) {
		log.info("Getting all saved Flight Data for search: "+savedSearch);
		if ( savedSearch.getOrigin().equals("XXX"))
		{
			log.info("Returning mocked Flight Data test search: ");
			return this.createMockedFlightData(savedSearch);
		}
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Filter keyCompare = new FilterPredicate(DALConstants.COLUMN_SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, savedSearch.getKey().getId());
		Query q = new Query(DALConstants.TABLE_FLIGHT_RESULT).setAncestor(ancestorKey).setFilter(keyCompare).addSort(DALConstants.COLUMN_SEARCH_DATE, SortDirection.DESCENDING);
		log.info("Query: "+q.toString());
		List<FlightResult> allFlightData = new ArrayList<FlightResult>();
		for(Entity en : dataStore.prepare(q).asIterable())
		{
			if( en != null)
			{
				FlightResult fd = this.createFlightResultFromEntity(en);
				log.info("Found Flight Data: "+fd.toString());
				allFlightData.add(fd);;
			}
		}
		return allFlightData;
	}

	@Override
	public FlightSearchResult getFlightResultForToday(FlightSavedSearch savedSearch) {
		Key key = savedSearch.getKey();
		LocalDate searchDate = new LocalDate(); // get today's date
		Filter keyCompare = new FilterPredicate(DALConstants.COLUMN_SAVED_SEARCH_KEY_ID, FilterOperator.EQUAL, key.getId());
		Filter searchDateCompare = new FilterPredicate(DALConstants.COLUMN_SEARCH_DATE, FilterOperator.EQUAL, searchDate.toDate());
		Filter allCompares = CompositeFilterOperator.and(keyCompare, searchDateCompare);
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Query q = new Query(DALConstants.TABLE_FLIGHT_RESULT).setAncestor(ancestorKey).setFilter(allCompares);
		log.info("Query: "+q.toString());
		Entity entity = dataStore.prepare(q).asSingleEntity();
		if (entity != null)
		{
			log.info("Found a match for flight data search for key ["+key.getId()+"] and date ["+searchDate+"]");
			return this.createFlightSearchResultFromEntity(savedSearch, entity);
		}
		log.info("No match found for flight saved data for key ["+key.getId()+"] and date ["+searchDate+"]");	
		return null;
	}

	private FlightSearchResult createFlightSearchResultFromEntity(FlightSavedSearch savedSearch, Entity entity) {
		FlightResult flightResult = createFlightResultFromEntity(entity);
		FlightSearchResult fsr = new FlightSearchResult(flightResult, savedSearch);
		return fsr;
	}

	@Override
	public boolean saveFlightSearchResult(FlightSearchResult fd) {
		log.info("Saving Flight Data: "+fd.toString());
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Entity en = new Entity(DALConstants.TABLE_FLIGHT_RESULT, ancestorKey);
		en.setProperty(DALConstants.COLUMN_LOWEST_PRICE,fd.getLowestPrice());
		en.setProperty(DALConstants.COLUMN_SHORTEST_TIME_PRICE,fd.getShortestTimePrice());
		en.setProperty(DALConstants.COLUMN_SAVED_SEARCH_KEY_ID, fd.getFlightSearch().getKey().getId());
		en.setProperty(DALConstants.COLUMN_SEARCH_DATE, fd.getDateSearched());
		en.setProperty(DALConstants.COLUMN_LOWEST_PRICE_DURATION, fd.getLowestPriceTripDuration());
		en.setProperty(DALConstants.COLUMN_SHORTEST_PRICE_DURATION, fd.getShortestTimePriceTripDuration());
		return dataStore.put(en) != null ? true : false;
	}
	
	private FlightResult createFlightResultFromEntity(Entity entity) {
		Date searchDate = (Date)entity.getProperty(DALConstants.COLUMN_SEARCH_DATE);
		FlightResult fd = new FlightResult(searchDate);
		fd.setLowestPriceTripDuration((Long) entity.getProperty(DALConstants.COLUMN_LOWEST_PRICE_DURATION));
		fd.setShortestTimePriceTripDuration((Long) entity.getProperty(DALConstants.COLUMN_SHORTEST_PRICE_DURATION));
		Double lowestPrice = (Double) entity.getProperty(DALConstants.COLUMN_LOWEST_PRICE);
		Double shortestPrice = (Double) entity.getProperty(DALConstants.COLUMN_SHORTEST_TIME_PRICE);
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
	
	
	/*
	 * 
	 * 
	 * MOCK DATA CREATION BELOW
	 * 
	 */

	private Random rand = new Random();
	private List<FlightResult> createMockedFlightData(FlightSavedSearch savedSearch) {
		LocalDate ld = new LocalDate();
		
		List<FlightResult> list = new ArrayList<FlightResult>();
		for (int i = 0; i < 50; i++)
		{
			LocalDate date = ld.minusDays(i);
			int priceOne = rand.nextInt(500) + 250;
			int priceTwo = rand.nextInt(500) + 250;
			Float lowestPrice;
			Float shortestPrice;
			if ( priceOne < priceTwo)
			{
				lowestPrice = Float.valueOf(priceOne);
				shortestPrice = Float.valueOf(priceTwo);
			}
			else
			{
				lowestPrice = Float.valueOf(priceTwo);
				shortestPrice = Float.valueOf(priceOne);
			}
			FlightResult fd = new FlightResult(DateConverter.toDate(date));
			fd.setLowestPrice(lowestPrice);
			fd.setShortestTimePrice(shortestPrice);
			list.add(fd);
		}
		return list;
	}

}
