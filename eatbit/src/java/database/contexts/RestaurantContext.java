package database.contexts;

import database.Coordinate;
import database.HoursRange;
import database.Photo;
import database.PriceRange;
import database.Restaurant;
import database.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *Contenitore delle informazioni di un ristorante, cioè il ristorante, il proprietario
 * se esiste, la sua posizione in classifica nella sua città, le cucine, il range di prezzi, 
 * gli orari, le foto, le recensioni e le coordinate.
 * @author jacopo
 */
public class RestaurantContext implements Serializable
{
    private Restaurant restaurant;
    private User owner;
    private int cityPosition;
    private ArrayList<String> cuisines;
    private PriceRange priceRange;
    private ArrayList<HoursRange> hoursRanges;
    private ArrayList<Photo> photos;
    private ArrayList<ReviewContext> reviewsContextsByNewest;
    private Coordinate coordinate;

    public RestaurantContext()
    {
    }

    public ArrayList<ReviewContext> getReviewsContextsByNewest()
    {
        return reviewsContextsByNewest;
    }

    public void setReviewsContextsByNewest(ArrayList<ReviewContext> reviewsContextsByNewest)
    {
        this.reviewsContextsByNewest = reviewsContextsByNewest;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    /*public int getGlobalPosition()
    {
        return globalPosition;
    }

    public void setGlobalPosition(int globalPosition)
    {
        this.globalPosition = globalPosition;
    }*/

    public int getCityPosition()
    {
        return cityPosition;
    }

    public void setCityPosition(int cityPosition)
    {
        this.cityPosition = cityPosition;
    }

    public ArrayList<String> getCuisines()
    {
        return cuisines;
    }

    public void setCuisines(ArrayList<String> cuisines)
    {
        this.cuisines = cuisines;
    }

    public PriceRange getPriceRange()
    {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange)
    {
        this.priceRange = priceRange;
    }

    public ArrayList<HoursRange> getHoursRanges()
    {
        return hoursRanges;
    }

    public void setHoursRanges(ArrayList<HoursRange> hoursRanges)
    {
        this.hoursRanges = hoursRanges;
    }

    public ArrayList<Photo> getPhotos()
    {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos)
    {
        this.photos = photos;
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate)
    {
        this.coordinate = coordinate;
    }
    
    /**
     * Restituisce un array di 4 interi contenente le medie dei voti sulle recensioni,
     * [media cibo, media servizio, media atmosfera, media prezzo]
     * se non ci sono recensioni viene tutto settato parti al restaurant global value
     * @return un array di 4 interi, le 4 medie
     */
    public int[] getAverages()
    {
        int[] res= {0,0,0,0};
        if(reviewsContextsByNewest.isEmpty())
            res[0]=res[1]=res[2]=res[3]=restaurant.getGlobal_value();
        else
        {
            for(ReviewContext rev: reviewsContextsByNewest)
            {
                res[0]+=rev.getReview().getFood();
                res[1]+=rev.getReview().getService();
                res[2]+=rev.getReview().getAtmosphere();
                res[3]+=rev.getReview().getValue_for_money();
            }
            res[0]/=restaurant.getReviews_counter();
            res[1]/=restaurant.getReviews_counter();
            res[2]/=restaurant.getReviews_counter();
            res[3]/=restaurant.getReviews_counter();
        }
        return res;
    }
    
    
}
