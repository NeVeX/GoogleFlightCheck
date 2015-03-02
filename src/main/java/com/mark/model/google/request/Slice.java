package com.mark.model.google.request;

public class Slice
{
    private String alliance;

    private String[] prohibitedCarrier;

    private String maxStops;

    private String origin;

    private PermittedDepartureTime permittedDepartureTime;

    private String preferredCabin;

    private String maxConnectionDuration;

    private String date;

    private String kind;

    private String[] permittedCarrier;

    private String destination;

    public String getAlliance ()
    {
        return alliance;
    }

    public void setAlliance (String alliance)
    {
        this.alliance = alliance;
    }

    public String[] getProhibitedCarrier ()
    {
        return prohibitedCarrier;
    }

    public void setProhibitedCarrier (String[] prohibitedCarrier)
    {
        this.prohibitedCarrier = prohibitedCarrier;
    }

    public String getMaxStops ()
    {
        return maxStops;
    }

    public void setMaxStops (String maxStops)
    {
        this.maxStops = maxStops;
    }

    public String getOrigin ()
    {
        return origin;
    }

    public void setOrigin (String origin)
    {
        this.origin = origin;
    }

    public PermittedDepartureTime getPermittedDepartureTime ()
    {
        return permittedDepartureTime;
    }

    public void setPermittedDepartureTime (PermittedDepartureTime permittedDepartureTime)
    {
        this.permittedDepartureTime = permittedDepartureTime;
    }

    public String getPreferredCabin ()
    {
        return preferredCabin;
    }

    public void setPreferredCabin (String preferredCabin)
    {
        this.preferredCabin = preferredCabin;
    }

    public String getMaxConnectionDuration ()
    {
        return maxConnectionDuration;
    }

    public void setMaxConnectionDuration (String maxConnectionDuration)
    {
        this.maxConnectionDuration = maxConnectionDuration;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public String getKind ()
    {
        return kind;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }

    public String[] getPermittedCarrier ()
    {
        return permittedCarrier;
    }

    public void setPermittedCarrier (String[] permittedCarrier)
    {
        this.permittedCarrier = permittedCarrier;
    }

    public String getDestination ()
    {
        return destination;
    }

    public void setDestination (String destination)
    {
        this.destination = destination;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [alliance = "+alliance+", prohibitedCarrier = "+prohibitedCarrier+", maxStops = "+maxStops+", origin = "+origin+", permittedDepartureTime = "+permittedDepartureTime+", preferredCabin = "+preferredCabin+", maxConnectionDuration = "+maxConnectionDuration+", date = "+date+", kind = "+kind+", permittedCarrier = "+permittedCarrier+", destination = "+destination+"]";
    }
}