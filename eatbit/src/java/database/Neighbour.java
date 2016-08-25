package database;

import java.io.Serializable;

/**
 *Oggetto che contiene latitudine, longitudine e nome di un ristorante, usato per rappresentare
 * i ristoranti vicini  a quello ricercato per la geolocalizzazione.
 * @author jacopo
 */
public class Neighbour implements Serializable
{
    private String name;
    private double longitude;
    private double latitude;

    public Neighbour()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    
    
    
}
