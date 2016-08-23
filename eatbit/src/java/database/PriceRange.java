package database;

import java.io.Serializable;

/**
 *Oggetto che rispecchia la tabella PRICE_RANGES, indica la fascia di prezzo in cui
 * si trova un ristorante, min e max riguardano la fascia di prezzo e non il min
 * e max settato dal ristoratore alla creazione o modifica del ristorante.
 * @author jacopo
 */
public class PriceRange implements Serializable
{
    private String name;
    private double min;
    private double max;

    public PriceRange()
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

    public double getMin()
    {
        return min;
    }

    public void setMin(double min)
    {
        this.min = min;
    }

    public double getMax()
    {
        return max;
    }

    public void setMax(double max)
    {
        this.max = max;
    }
    
    
}
