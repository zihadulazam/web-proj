/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Coordinate;
import database.HoursRange;
import database.Photo;
import database.PriceRange;
import database.Restaurant;
import database.Review;
import database.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jacopo
 */
public class RestaurantContext implements Serializable
{
    private Restaurant restaurant;
    private User owner;
    private int globalPosition;
    private int cityPosition;
    private ArrayList<String> cuisines;
    private PriceRange priceRange;
    private ArrayList<HoursRange> hoursRanges;
    private ArrayList<Photo> photos;
    private ArrayList<Review> reviewsByNewest;
    private Coordinate coordinate;

    public RestaurantContext()
    {
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

    public int getGlobalPosition()
    {
        return globalPosition;
    }

    public void setGlobalPosition(int globalPosition)
    {
        this.globalPosition = globalPosition;
    }

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

    public ArrayList<Review> getReviewsByNewest()
    {
        return reviewsByNewest;
    }

    public void setReviewsByNewest(ArrayList<Review> reviewsByNewest)
    {
        this.reviewsByNewest = reviewsByNewest;
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate)
    {
        this.coordinate = coordinate;
    }
    
    
    
    
}
