package database;

import java.io.Serializable;
import java.sql.Time;

/**
 *Bean per gli orari di un ristorante, day è un numero da 1 a 7 che indica il giorno, con 1
 * che indica lunedì e 7 domenica.
 * @author jacopo
 */
public final class HoursRange implements Serializable
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
    
    /**
     * Restituisce l'ora di apertura in un formato del tipo 10:30
     * @return 
     */
    public String getFormattedStart_hour()
    {
        int resH=start_hour.getHours();
        int resM=start_hour.getMinutes();
        String stringH=(resH<10?("0"+resH):(""+resH));
        String stringM=(resM<10?("0"+resM):(""+resM));
        return stringH+":"+stringM;
    }
    
    /**
     *Restituisce l'ora di chiusura in un formato del tipo 10:30. 
     * @return 
     */
    public String getFormattedEnd_hour()
    {
        int resH=end_hour.getHours();
        int resM=end_hour.getMinutes();
        String stringH=(resH<10?("0"+resH):(""+resH));
        String stringM=(resM<10?("0"+resM):(""+resM));
        return stringH+":"+stringM;
    }
    
    /**
     * Restituisce l'orario rappresentato da questo oggeto, in un formato del tipo:
     * lun 10:30 - 15:50
     * @return 
     */
    public String getFormattedWeeklyHour()
    {
        String res;
        switch (day)
        {
            case 1:
                res="lun";
                break;
            case 2:
                res="mar";
                break;
            case 3:
                res="mer";
                break;
            case 4:
                res="gio";
                break;
            case 5:
                res="ven";
                break;
            case 6:
                res="sab";
                break;
            case 7:
                res="dom";
                break;
            default:
                res="UnknownDay";
                break;
        }
        return res+" "+ getFormattedStart_hour() + " - " + getFormattedEnd_hour();
    }
    
    /**
     * Restituisce il giorno di questo oggetto, con un formato del tipo
     * lun
     * mar
     * etc
     * 
     * @return 
     */
    public String getFormattedDay()
    {
        String res;
        switch (day)
        {
            case 1:
                res="lun";
                break;
            case 2:
                res="mar";
                break;
            case 3:
                res="mer";
                break;
            case 4:
                res="gio";
                break;
            case 5:
                res="ven";
                break;
            case 6:
                res="sab";
                break;
            case 7:
                res="dom";
                break;
            default:
                res="UnknownDay";
                break;
        }
        return res;
    }
    
    private boolean isDay(final int day )
    {
        return day>=0 && day <=7;
    }
    
    private boolean isHour(final int hour)
    {
        return hour>=0 && hour<=24;
    }
    
    private boolean isMinute(final int minute)
    {
        return minute>=0 && minute<=60;
    }
    
    /**
     * Costruttore per creare un oggetto di tipo HoursRange a partire da una stringa
     * con formato 
     * 110:3016:40
     * dove la prima cifra indica il giorno, e poi seguono 2 orari con ora:minuti.
     * Lancia eccezione se l'orario di apertura non risulterà prima dell'orario di chiusura
     * o se le ore fornite non sono convertibili in ore reali, per esempio:
     * 142:9314:40 darà un eccezione a causa di 42:92.
     * @param hour
     * @throws NumberFormatException 
     */
    public HoursRange(String hour) throws NumberFormatException
    {
        int tDay=hour.charAt(0)-'0';
        int hourOp=Integer.parseInt(hour.substring(1, 3));
        int minuteOp=Integer.parseInt(hour.substring(4, 6));
        int hourCl=Integer.parseInt(hour.substring(6, 8));
        int minuteCl=Integer.parseInt(hour.substring(9, 11));
        if(isDay(tDay)&&isHour(hourOp)&&isMinute(minuteOp)&&isHour(hourCl)
                &&isMinute(minuteCl))
        {
         setDay(tDay);
         start_hour=new Time(hourOp,minuteOp,0);
         end_hour=new Time(hourCl,minuteCl,0);
         if(!start_hour.before(end_hour))
             throw new NumberFormatException("start hour is not before end hour");
        }
        else
            throw new NumberFormatException("wrong day or hour or minutes range");
    }
}

