package com.mark.dal.impl;

import java.util.List;

import org.joda.time.DateTime;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.mark.dal.IApplicationDAL;
import com.mark.model.dal.ApplicationState;
import com.mark.util.converter.DateConverter;

public class ApplicationStateDALImpl implements IApplicationDAL {

	private static final String APPLICATION_STATE_TABLE = "APPLICATION_STATE";
	private static final String DATE = "DATE";
	private static final String FLIGHT_API_COUNT = "FLIGHT_API_COUNT";
	
	@Override
	public ApplicationState getApplicationState() {
		// get today's date
		DateTime todayDate = new DateTime();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		String todayDateString = DateConverter.convertToString(todayDate);
		Filter dateCompare = new FilterPredicate(DATE, FilterOperator.EQUAL, todayDateString);
		Query q = new Query(APPLICATION_STATE_TABLE).setFilter(dateCompare);
		System.out.println("Searching for application state: "+q.toString());
		
		ApplicationState state = new ApplicationState();
		state.setDate(todayDate);		
		List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1));
		if ( list != null && list.size() == 0)
		{
			Integer apiCount = Integer.valueOf((String)list.get(0).getProperty(FLIGHT_API_COUNT));
			state.setFlightApiCount(apiCount != null ? apiCount : 0);
			System.out.println("Found application state with apiCount ["+state.getFlightApiCount()+"]");
		}
		return state;
	}
	
	@Override
	public boolean saveApplicationState(ApplicationState state) {
		System.out.println("Saving Application State to datestore");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity en = new Entity(APPLICATION_STATE_TABLE);
		en.setProperty(DATE, DateConverter.convertToString(state.getDate()));
		en.setProperty(FLIGHT_API_COUNT, state.getFlightApiCount());
		return datastore.put(en) != null ? true : false;
	}
	

}
