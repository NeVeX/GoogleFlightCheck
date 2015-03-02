package com.mark.model.google.request;


public class GoogleFlightRequest
{
    private String refundable;

    private Passengers passengers;

    private String solutions;

    private String saleCountry;

    private Slice[] slice;

    private String maxPrice;

    public String getRefundable ()
    {
        return refundable;
    }

    public void setRefundable (String refundable)
    {
        this.refundable = refundable;
    }

    public Passengers getPassengers ()
    {
        return passengers;
    }

    public void setPassengers (Passengers passengers)
    {
        this.passengers = passengers;
    }

    public String getSolutions ()
    {
        return solutions;
    }

    public void setSolutions (String solutions)
    {
        this.solutions = solutions;
    }

    public String getSaleCountry ()
    {
        return saleCountry;
    }

    public void setSaleCountry (String saleCountry)
    {
        this.saleCountry = saleCountry;
    }

    public Slice[] getSlice ()
    {
        return slice;
    }

    public void setSlice (Slice[] slice)
    {
        this.slice = slice;
    }

    public String getMaxPrice ()
    {
        return maxPrice;
    }

    public void setMaxPrice (String maxPrice)
    {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [refundable = "+refundable+", passengers = "+passengers+", solutions = "+solutions+", saleCountry = "+saleCountry+", slice = "+slice+", maxPrice = "+maxPrice+"]";
    }
}
			
			