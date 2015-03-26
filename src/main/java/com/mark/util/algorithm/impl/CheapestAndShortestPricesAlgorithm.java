package com.mark.util.algorithm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mark.exception.FlightException;
import com.mark.model.FlightSavedSearch;
import com.mark.model.algorithm.AlgorithmResult;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.model.google.response.Leg;
import com.mark.model.google.response.ResponseSlice;
import com.mark.model.google.response.Segment;
import com.mark.model.google.response.Trip;
import com.mark.model.google.response.TripOption;
import com.mark.util.algorithm.IFlightAlgorithm;

@Component
public class CheapestAndShortestPricesAlgorithm implements IFlightAlgorithm {

	@Override
	public AlgorithmResult execute(FlightSavedSearch flightSearch, GoogleFlightResponse googleFlightResponse) {
		List<FlightDataHolder> parsedList = this.parseGoogleFlightResponseIntoFlightInfo(flightSearch, googleFlightResponse);
		if ( parsedList != null )
		{
			return this.determineFlightInfoFromApiData(flightSearch, parsedList);
		}
		return null;
	}

	
	private AlgorithmResult determineFlightInfoFromApiData(FlightSavedSearch flightSearch, List<FlightDataHolder> listOfParsedData) {
		AlgorithmResult finalResult = new AlgorithmResult();
		Collections.sort(listOfParsedData, new FlightLowestPriceCompare());
		FlightDataHolder fpdCheap = listOfParsedData.get(0);
		finalResult.setLowestPrice(fpdCheap.price);
		finalResult.setLowestPriceTripDuration(new Long(fpdCheap.tripLength));
		Collections.sort(listOfParsedData, new FlightLowestPriceForShortestTimeCompare());
		FlightDataHolder fpdShortest = listOfParsedData.get(0);
		finalResult.setShortestTimePrice(fpdShortest.price);
		finalResult.setShortestTimePriceTripDuration(new Long(fpdShortest.tripLength));
		return finalResult;
	}
	
	private List<FlightDataHolder> parseGoogleFlightResponseIntoFlightInfo(FlightSavedSearch fss, GoogleFlightResponse response) {
		List<FlightDataHolder> listOfData = new ArrayList<FlightDataHolder>();
		if (response == null || response.getTrips() == null || response.getTrips().getTripOption() == null) {
			throw new FlightException("No flight information returned for search query");
		}
		Trip trip = response.getTrips();
		for (TripOption t: trip.getTripOption()) {
			float price = Float.valueOf(t.getSaleTotal().substring(3,
			t.getSaleTotal().length()));
			int stops = 0;
			boolean foundFlightData = false;
			int tripLength = 0;
			if (t.getSlice() != null) {
				for (ResponseSlice rs: t.getSlice()) {
					if (rs.getSegment() != null) {
						for (Segment seg: rs.getSegment()) {
							if (seg.getLeg() != null) {
								for (Leg leg: seg.getLeg()) {
									stops++;
									tripLength += leg.getDuration() != null ? leg.getDuration() : 0;
									foundFlightData = true;
								}
							}
						}
					}
				}
			}
			if (foundFlightData) {
				// create and add this result
				listOfData.add(new FlightDataHolder(price, stops, tripLength));
			} else {
				System.out.println("Info: Did not find flight data in Trip Options.");
			}
		}
		return listOfData;
	}


	
	/**
	 * Private class to hold data as it's parsed through
	 * @author NeVeX
	 *
	 */
	private class FlightDataHolder
	{
		private Float price;
		private Integer numberOfStops;
		private Integer tripLength;
	
		public FlightDataHolder(Float price, Integer numberOfStops, Integer tripLength) {
			super();
			this.price = price;
			this.numberOfStops = numberOfStops;
			this.tripLength = tripLength;
		}
			
	}
	
	private class FlightLowestPriceCompare implements Comparator<FlightDataHolder> {

		@Override
		public int compare(FlightDataHolder f1, FlightDataHolder f2) {
			if ( f1.price < f2.price)
			{
				return -1; // f1 is less than f2
			}
			return 1;
		}
	}
	
	public class FlightLowestPriceForShortestTimeCompare implements Comparator<FlightDataHolder> {

		@Override
		public int compare(FlightDataHolder f1, FlightDataHolder f2) {
			if ( f1.tripLength == f2.tripLength )
			{
				if ( f1.price < f2.price)
				{
					return -1;
				}
				return 1;
			}
			if ( f1.tripLength < f2.tripLength)
			{
				return -1;
			}
			return 1;
		}
	}
	
	
}
