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
public class Review implements Serializable
{

    private int id;
    private int global_value;
    private int food;
    private int service;
    private int value_for_money;
    private int atmosphere;
    private String name;
    private String description;
    private Timestamp date_creation;
    private int id_restaurant;
    private int id_creator;
    private int id_photo;
    private int likes;
    private int dislikes;

    public int getDislikes()
    {
        return dislikes;
    }

    public void setDislikes(int dislikes)
    {
        this.dislikes = dislikes;
    }

    
    public int getFood()
    {
        return food;
    }

    public void setFood(int food)
    {
        this.food = food;
    }

    public Review()
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

    public int getGlobal_value()
    {
        return global_value;
    }

    public void setGlobal_value(int global_value)
    {
        this.global_value = global_value;
    }

    public int getService()
    {
        return service;
    }

    public void setService(int service)
    {
        this.service = service;
    }

    public int getValue_for_money()
    {
        return value_for_money;
    }

    public void setValue_for_money(int value_for_money)
    {
        this.value_for_money = value_for_money;
    }

    public int getAtmosphere()
    {
        return atmosphere;
    }

    public void setAtmosphere(int atmosphere)
    {
        this.atmosphere = atmosphere;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Timestamp getDate_creation()
    {
        return date_creation;
    }

    public void setDate_creation(Timestamp date_creation)
    {
        this.date_creation = date_creation;
    }

    public int getId_restaurant()
    {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant)
    {
        this.id_restaurant = id_restaurant;
    }

    public int getId_creator()
    {
        return id_creator;
    }

    public void setId_creator(int id_creator)
    {
        this.id_creator = id_creator;
    }

    public int getId_photo()
    {
        return id_photo;
    }

    public void setId_photo(int id_photo)
    {
        this.id_photo = id_photo;
    }

    public int getLikes()
    {
        return likes;
    }

    public void setLikes(int likes)
    {
        this.likes = likes;
    }

}
