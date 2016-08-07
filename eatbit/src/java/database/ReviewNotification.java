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
public class ReviewNotification implements Serializable
{
    private int id;
    private int  id_user;
    private Review review;
    String restaurant_name;
    private Timestamp creation;

    public ReviewNotification()
    {
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setId_user(int id_user)
    {
        this.id_user = id_user;
    }

    public void setReview(Review review)
    {
        this.review = review;
    }

    public void setRestaurant_name(String restaurant_name)
    {
        this.restaurant_name = restaurant_name;
    }

    public void setCreation(Timestamp creation)
    {
        this.creation = creation;
    }

    public int getId()
    {
        return id;
    }

    public int getId_user()
    {
        return id_user;
    }

    public Review getReview()
    {
        return review;
    }

    public String getRestaurant_name()
    {
        return restaurant_name;
    }

    public Timestamp getCreation()
    {
        return creation;
    }
}
