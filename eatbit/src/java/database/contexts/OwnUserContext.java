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
    private ArrayList<ReviewContext> reviewContext;
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

    public ArrayList<ReviewContext> getReviewContext()
    {
        return reviewContext;
    }

    public void setReviewContext(ArrayList<ReviewContext> reviewContext)
    {
        this.reviewContext = reviewContext;
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
