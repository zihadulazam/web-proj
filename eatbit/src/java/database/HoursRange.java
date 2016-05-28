/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import java.sql.Time;

/**
 *
 * @author jacopo
 */
public class HoursRange implements Serializable
{
    private int day;
    private Time start_hour;
    private Time end_hour;

    public HoursRange()
    {
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public Time getStart_hour()
    {
        return start_hour;
    }

    public void setStart_hour(Time start_hour)
    {
        this.start_hour = start_hour;
    }

    public Time getEnd_hour()
    {
        return end_hour;
    }

    public void setEnd_hour(Time end_hour)
    {
        this.end_hour = end_hour;
    }
    
    
}

