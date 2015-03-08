package com.mark.dal.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.dal.IApplicationDAL;
import com.mark.model.dal.ApplicationState;
import com.mark.util.converter.DateConverter;

@Repository
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
		Entity result = datastore.prepare(q).asSingleEntity();
		if ( result != null)
		{
			ApplicationState state = this.createApplicationStateFromEntity(result);
			System.out.println("Found application state with apiCount ["+state.getFlightApiCount()+"]");
		}
		return null;
	}
	
	@Override
	public boolean saveApplicationState(ApplicationState state) {
		System.out.println("Saving Application State to datestore - Flight API Count is ["+state.getFlightApiCount()+"]");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity en = new Entity(APPLICATION_STATE_TABLE);
		en.setProperty(DATE, DateConverter.convertToString(state.getDate()));
		en.setProperty(FLIGHT_API_COUNT, state.getFlightApiCount());
		
		return datastore.put(en) != null ? true : false;
	}

	@Override
	public List<ApplicationState> getAllApplicationStates() {
		System.out.println("Getting all application states");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(APPLICATION_STATE_TABLE).addSort(DATE, SortDirection.DESCENDING);
		Iterable<Entity> iter = datastore.prepare(q).asIterable();
		List<ApplicationState> allApplicationStates = new ArrayList<ApplicationState>();
		while (iter.iterator().hasNext())
		{
			Entity en = iter.iterator().next();
			if( en != null )
			{
				allApplicationStates.add(this.createApplicationStateFromEntity(en));
			}
		}
		return null;
	}
	
	private ApplicationState createApplicationStateFromEntity(Entity en)
	{
		ApplicationState appState = new ApplicationState();
		Long apiCount = (Long)en.getProperty(FLIGHT_API_COUNT);
		appState.setFlightApiCount(apiCount != null ? apiCount : 0);
		appState.setDate(DateConverter.convertToDateTime((String)en.getProperty(DATE)));
		return appState;
	}
	

}
