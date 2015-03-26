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
import com.mark.constant.DALConstants;
import com.mark.dal.IAdminDAL;
import com.mark.dal.IFlightResultDAL;
import com.mark.model.ApplicationState;
import com.mark.util.converter.DateConverter;

@Repository
public class AdminDALImpl implements IAdminDAL {

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
		Date todayDate = new LocalDate().toDate();
		ApplicationState state = this.findApplicationState(todayDate);
		if ( state != null)
		{
			System.out.println("Found application state with apiCount ["+state.getFlightApiCount()+"]");
			return state;
		}
		return null;
	}
	
	private ApplicationState findApplicationState(Date dt)
	{
		System.out.println("Searching for application state for date: "+dt);
		Filter dateCompare = new FilterPredicate(DATE, FilterOperator.EQUAL, dt);
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
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
			Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
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
		en.setProperty(DATE, state.getDate());
		en.setProperty(FLIGHT_API_COUNT, state.getFlightApiCount());
		return en;
	}

	@Override
	public List<ApplicationState> getAllApplicationStates() {
		System.out.println("Getting all application states");
		Key ancestorKey = KeyFactory.createKey(DALConstants.ANCESTOR_FOR_ALL, DALConstants.ANCESTOR_ID_FOR_ALL);
		Query q = new Query(APPLICATION_STATE_TABLE).setAncestor(ancestorKey).addSort(DATE, SortDirection.DESCENDING);
		System.out.println("Query: "+q.toString());
		List<ApplicationState> allApplicationStates = new ArrayList<ApplicationState>();
		for (Entity en : dataStore.prepare(q).asIterable())
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
		appState.setDate(stateDate);
		appState.setKey(en.getKey());
		return appState;
	}
	

}
