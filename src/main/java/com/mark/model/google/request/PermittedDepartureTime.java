package com.mark.model.google.request;

public class PermittedDepartureTime
{
    private String earliestTime;

    private String latestTime;

    private String kind;

    public String getEarliestTime ()
    {
        return earliestTime;
    }

    public void setEarliestTime (String earliestTime)
    {
        this.earliestTime = earliestTime;
    }

    public String getLatestTime ()
    {
        return latestTime;
    }

    public void setLatestTime (String latestTime)
    {
        this.latestTime = latestTime;
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
        return "ClassPojo [earliestTime = "+earliestTime+", latestTime = "+latestTime+", kind = "+kind+"]";
    }
}
			