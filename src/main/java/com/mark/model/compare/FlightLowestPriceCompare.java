package com.mark.model.compare;

import java.util.Comparator;

import com.mark.model.FlightParsedData;

public class FlightLowestPriceCompare implements Comparator<FlightParsedData> {

	@Override
	public int compare(FlightParsedData f1, FlightParsedData f2) {
		if ( f1.getPrice() < f2.getPrice())
		{
			return -1; // f1 is less than f2
		}
		return 1;
	}


}
