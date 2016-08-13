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
    private int id_user;
    private Photo photo;
    private String user_name;
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

    public int getId_user()
    {
        return id_user;
    }

    public void setId_user(int id_user)
    {
        this.id_user = id_user;
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
     * @return the user_name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @param user_name the user_name to set
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
    
    
}
