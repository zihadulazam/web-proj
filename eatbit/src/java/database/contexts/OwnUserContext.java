/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Notification;
import database.Photo;
import database.Restaurant;
import database.Review;
import database.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jacopo
 */
public class OwnUserContext implements Serializable
{
    private User user;
    private ArrayList<Photo> photos;
    private ArrayList<Review> reviews;
    private ArrayList<Restaurant> restaurant;
    private ArrayList<Notification> notification;

    public OwnUserContext()
    {
    }

    
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public ArrayList<Photo> getPhotos()
    {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos)
    {
        this.photos = photos;
    }

    public ArrayList<Review> getReviews()
    {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews)
    {
        this.reviews = reviews;
    }

    public ArrayList<Restaurant> getRestaurant()
    {
        return restaurant;
    }

    public void setRestaurant(ArrayList<Restaurant> restaurant)
    {
        this.restaurant = restaurant;
    }

    public ArrayList<Notification> getNotification()
    {
        return notification;
    }

    public void setNotification(ArrayList<Notification> notification)
    {
        this.notification = notification;
    }
    
    
    
}
