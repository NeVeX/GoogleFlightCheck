//package com.mark;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.Context;
//
//import org.jboss.resteasy.core.Dispatcher;
//
//import com.mark.controller.FlightController;
//
//public class FlightApplication extends Application {
//	private Set<Object> singletons = new HashSet<Object>();
//	 
////    public FlightApplication(@Context Dispatcher dispatcher) {
////        singletons.add(new FlightController(dispatcher.getDefaultContextObjects()));
////    }
//    
//    public FlightApplication() {
//    	singletons.add(new FlightController());
//    }
// 
//    @Override
//    public Set<Object> getSingletons() {
//        return singletons;
//    }
//}