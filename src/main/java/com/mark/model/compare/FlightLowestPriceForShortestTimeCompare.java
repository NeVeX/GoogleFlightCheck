package com.mark.model.compare;

import java.util.Comparator;

import com.mark.model.FlightParsedData;

public class FlightLowestPriceForShortestTimeCompare implements Comparator<FlightParsedData> {

	@Override
	public int compare(FlightParsedData f1, FlightParsedData f2) {
		if ( f1.getTripLength() == f2.getTripLength() )
		{
			if ( f1.getPrice() < f2.getPrice())
			{
				return -1;
			}
			return 1;
		}
		if ( f1.getTripLength() < f2.getTripLength())
		{
			return -1;
		}
		return 1;
	}
	
	

}
