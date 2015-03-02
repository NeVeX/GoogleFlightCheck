package com.mark.model.google.request;

public class Passengers
{
    private String childCount;

    private String infantInLapCount;

    private String adultCount;

    private String seniorCount;

    private String infantInSeatCount;

    private String kind;

    public String getChildCount ()
    {
        return childCount;
    }

    public void setChildCount (String childCount)
    {
        this.childCount = childCount;
    }

    public String getInfantInLapCount ()
    {
        return infantInLapCount;
    }

    public void setInfantInLapCount (String infantInLapCount)
    {
        this.infantInLapCount = infantInLapCount;
    }

    public String getAdultCount ()
    {
        return adultCount;
    }

    public void setAdultCount (String adultCount)
    {
        this.adultCount = adultCount;
    }

    public String getSeniorCount ()
    {
        return seniorCount;
    }

    public void setSeniorCount (String seniorCount)
    {
        this.seniorCount = seniorCount;
    }

    public String getInfantInSeatCount ()
    {
        return infantInSeatCount;
    }

    public void setInfantInSeatCount (String infantInSeatCount)
    {
        this.infantInSeatCount = infantInSeatCount;
    }

    public String getKind ()
    {
        return kind;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [childCount = "+childCount+", infantInLapCount = "+infantInLapCount+", adultCount = "+adultCount+", seniorCount = "+seniorCount+", infantInSeatCount = "+infantInSeatCount+", kind = "+kind+"]";
    }
}