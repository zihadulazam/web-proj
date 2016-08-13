/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author jacopo
 */
public class PhotoNotification implements Serializable
{
    private int id;
    private User user;
    private Photo photo;
    private String restaurant_name;
    private Timestamp creation;

    public PhotoNotification()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Photo getPhoto()
    {
        return photo;
    }

    public void setPhoto(Photo photo)
    {
        this.photo = photo;
    }

    public String getRestaurant_name()
    {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name)
    {
        this.restaurant_name = restaurant_name;
    }

    public Timestamp getCreation()
    {
        return creation;
    }

    public void setCreation(Timestamp creation)
    {
        this.creation = creation;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    
    
}
