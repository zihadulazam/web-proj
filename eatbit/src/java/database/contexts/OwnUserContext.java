/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Photo;
import database.PhotoNotification;
import database.Restaurant;
import database.ReviewNotification;
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
    private ArrayList<PhotoNotification> photo_notifications;
    private ArrayList<ReviewNotification> review_notifications;

    public OwnUserContext()
    {
    }

    public ArrayList<PhotoNotification> getPhoto_notifications()
    {
        return photo_notifications;
    }

    public void setPhoto_notifications(ArrayList<PhotoNotification> photo_notifications)
    {
        this.photo_notifications = photo_notifications;
    }

    public ArrayList<ReviewNotification> getReview_notifications()
    {
        return review_notifications;
    }

    public void setReview_notifications(ArrayList<ReviewNotification> review_notifications)
    {
        this.review_notifications = review_notifications;
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
}
