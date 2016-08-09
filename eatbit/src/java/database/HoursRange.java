/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;

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
    
    public String getFormattedStart_hour()
    {
        return Integer.toString(start_hour.getHours())+":"+start_hour.getMinutes();
    }
    
    public String getFormattedEnd_hour()
    {
        return Integer.toString(end_hour.getHours())+":"+end_hour.getMinutes();
    }
    
    public String getFormattedWeeklyHour()
    {
        String res;
        switch (day)
        {
            case 1:
                res="Monday";
                break;
            case 2:
                res="Tuesday";
                break;
            case 3:
                res="Wednesday";
                break;
            case 4:
                res="Thursday";
                break;
            case 5:
                res="Friday";
                break;
            case 6:
                res="Saturday";
                break;
            case 7:
                res="Sunday";
                break;
            default:
                res="UnknownDay";
                break;
        }
        return res+" "+ start_hour.getHours()+":"+start_hour.getMinutes()+" - "
                +end_hour.getHours()+":"+end_hour.getMinutes();
    }
}

