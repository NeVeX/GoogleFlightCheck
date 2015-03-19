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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mark.dal.IApplicationDAL;
import com.mark.dal.IFlightInfoDAL;
import com.mark.model.dal.ApplicationState;
import com.mark.util.converter.DateConverter;

@Repository
public class ApplicationStateDALImpl implements IApplicationDAL {

	private static final String APPLICATION_STATE_TABLE = "APPLICATION_STATE";
	private static final String DATE = "DATE";
	private static final String FLIGHT_API_COUNT = "FLIGHT_API_COUNT";
	private DatastoreService dataStore;
	
	@PostConstruct
	public void setup()
	{
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
	@Override
	public ApplicationState getApplicationState() {
		// get today's date
		LocalDate todayDate = new LocalDate();
		ApplicationState state = this.findApplicationState(todayDate);
		if ( state != null)
		{
			System.out.println("Found application state with apiCount ["+state.getFlightApiCount()+"]");
			return state;
		}
		return null;
	}
	
	private ApplicationState findApplicationState(LocalDate dt)
	{
		System.out.println("Searching for application state for date: "+dt);
		Filter dateCompare = new FilterPredicate(DATE, FilterOperator.EQUAL, dt.toDate());
		Key ancestorKey = KeyFactory.createKey(IFlightInfoDAL.FLIGHT_ANCESTOR_KIND, IFlightInfoDAL.FLIGHT_ANCESTOR_ID);
		Query q = new Query(APPLICATION_STATE_TABLE).setAncestor(ancestorKey).setFilter(dateCompare);
		System.out.println("Query: "+q.toString());
		Entity en = dataStore.prepare(q).asSingleEntity();
		if ( en != null )
		{
			return createApplicationStateFromEntity(en);
		}
		return null;
	}
	
	@Override
	public boolean saveApplicationState(ApplicationState newState) {
		System.out.println("Saving Application State: "+newState.toString());
		ApplicationState savedState = this.findApplicationState(newState.getDate());
		if ( savedState == null)
		{
			Key ancestorKey = KeyFactory.createKey(IFlightInfoDAL.FLIGHT_ANCESTOR_KIND, IFlightInfoDAL.FLIGHT_ANCESTOR_ID);
			// create a new one
			Entity en = new Entity(APPLICATION_STATE_TABLE, ancestorKey);
			poplulteEntityWithData(en, newState);
			return dataStore.put(en) != null ? true : false;
		}
		else
		{
			newState.setKey(savedState.getKey());
			// update the previous guy
			return updateApplicationState(newState);
		}
	}
	
	private boolean updateApplicationState(ApplicationState state) {
		try {
			Entity en = dataStore.get(state.getKey());
			poplulteEntityWithData(en, state);
			return dataStore.put(en) != null ? true : false; // re-save
		} catch (EntityNotFoundException e) {
			System.err.println("Could not find application state using key ["+state.getKey()+"]");
		}
		return false;
	}

	private Entity poplulteEntityWithData(Entity en, ApplicationState state)
	{
		// see if there is an existing state
		en.setProperty(DATE, state.getDate().toDate());
		en.setProperty(FLIGHT_API_COUNT, state.getFlightApiCount());
		return en;
	}

	@Override
	public List<ApplicationState> getAllApplicationStates() {
		System.out.println("Getting all application states");
		Key ancestorKey = KeyFactory.createKey(IFlightInfoDAL.FLIGHT_ANCESTOR_KIND, IFlightInfoDAL.FLIGHT_ANCESTOR_ID);
		Query q = new Query(APPLICATION_STATE_TABLE).setAncestor(ancestorKey).addSort(DATE, SortDirection.DESCENDING);
		System.out.println("Query: "+q.toString());
		List<ApplicationState> allApplicationStates = new ArrayList<ApplicationState>();
		for (Entity en : dataStore.prepare(q).asIterable(FetchOptions.Builder.withLimit(100)))
		{
			if( en != null )
			{
				ApplicationState as = this.createApplicationStateFromEntity(en);
				System.out.println("Found Application State Search: "+as.toString());
				allApplicationStates.add(as);
			}
		}
		return allApplicationStates;
	}
	
	private ApplicationState createApplicationStateFromEntity(Entity en)
	{
		ApplicationState appState = new ApplicationState();
		Long apiCount = (Long)en.getProperty(FLIGHT_API_COUNT);
		appState.setFlightApiCount(apiCount != null ? apiCount : 0);
		Date stateDate = (Date) en.getProperty(DATE);
		appState.setDate(LocalDate.fromDateFields(stateDate));
		appState.setKey(en.getKey());
		return appState;
	}
	

}
