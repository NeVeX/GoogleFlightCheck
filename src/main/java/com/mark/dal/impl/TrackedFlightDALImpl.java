//package com.mark.dal.impl;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.stereotype.Repository;
//
//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.mark.dal.ITrackedFlightDAL;
//
//@Repository
//public class TrackedFlightDALImpl implements ITrackedFlightDAL{
//
//	private DatastoreService dataStore;
//	
//	@PostConstruct
//	public void setup()
//	{
//		dataStore = DatastoreServiceFactory.getDatastoreService();
//	}
//	
//	public void test()
//	{
//		// Get all flight searches that are tracked
//		
//	}
//	
//	
//}
