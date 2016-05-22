/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Timestamp;

/**
 *
 * @author jacopo
 */
public class Review {
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

    public Review(int id, int global_value, int food, int service, int value_for_money, int atmosphere, String name, String description, Timestamp date_creation, int id_restaurant, int id_creator, int id_photo, int likes) {
        this.id = id;
        this.global_value = global_value;
        this.food = food;
        this.service = service;
        this.value_for_money = value_for_money;
        this.atmosphere = atmosphere;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
        this.id_restaurant = id_restaurant;
        this.id_creator = id_creator;
        this.id_photo = id_photo;
        this.likes = likes;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public Review() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGlobal_value() {
        return global_value;
    }

    public void setGlobal_value(int global_value) {
        this.global_value = global_value;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getValue_for_money() {
        return value_for_money;
    }

    public void setValue_for_money(int value_for_money) {
        this.value_for_money = value_for_money;
    }

    public int getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(int atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Timestamp date_creation) {
        this.date_creation = date_creation;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public int getId_creator() {
        return id_creator;
    }

    public void setId_creator(int id_creator) {
        this.id_creator = id_creator;
    }

    public int getId_photo() {
        return id_photo;
    }

    public void setId_photo(int id_photo) {
        this.id_photo = id_photo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    
}
