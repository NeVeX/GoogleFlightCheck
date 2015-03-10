package com.mark.util.client.mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mark.exception.FlightException;
import com.mark.model.google.request.GoogleFlightRequest;
import com.mark.model.google.response.GoogleFlightResponse;
import com.mark.model.google.response.Leg;
import com.mark.model.google.response.ResponseSlice;
import com.mark.model.google.response.Segment;
import com.mark.model.google.response.Trip;
import com.mark.model.google.response.TripOption;
import com.mark.util.client.type.resteasy.IRestEasyGoogleFlightApiClient;
import com.mark.util.converter.JsonConverter;


public class GoogleFlightClientMocked implements IRestEasyGoogleFlightApiClient
{
	private final static String testDataFile =  "data/example_flight_response.json";
	private final static Random random = new Random();
	
	@Override
	public GoogleFlightResponse postForFlightInfo(GoogleFlightRequest gRequest) {
		return createDynamicMockedData(gRequest);
	}
	
	private GoogleFlightResponse createDynamicMockedData(GoogleFlightRequest request)
	{
		GoogleFlightResponse gfr = new GoogleFlightResponse();
		Trip trip = new Trip();
		List<TripOption> tripOptions = new ArrayList<TripOption>();
		int tripOptionsCount = random.nextInt(10) + 1; // at least have one
		for (int i = 0; i < tripOptionsCount; i++)
		{
			TripOption to = new TripOption();
			int price = random.nextInt(1000)+150;
			to.setSaleTotal("USD"+price);
			List<ResponseSlice> slices = new ArrayList<ResponseSlice>();
			ResponseSlice rs = new ResponseSlice();
			List<Segment> segments = new ArrayList<Segment>();
			Segment s = new Segment();
			List<Leg> legs = new ArrayList<Leg>();
			int numberOfLegs = random.nextInt(5) + 1;
			for ( int j = 0; j < numberOfLegs; j++)
			{
				Leg l = new Leg();
				l.setDuration(random.nextInt(300)+30);
				legs.add(l);
			}
			s.setLeg(legs);
			segments.add(s);
			rs.setSegment(segments);
			slices.add(rs);
			to.setSlice(slices);
			tripOptions.add(to);
		}
		trip.setTripOption(tripOptions);
		gfr.setTrips(trip);
		return gfr;
		
		
	}

	@Override
	public GoogleFlightResponse postForFlightInfo(String key, GoogleFlightRequest gRequest) {
		return this.postForFlightInfo(gRequest);
	}
}

