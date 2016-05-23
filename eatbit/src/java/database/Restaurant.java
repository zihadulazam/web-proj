/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author jacopo
 */
public class Restaurant {
    private int id;
    private String name;
    private String description;
    private String web_site_url;
    private int global_value;
    private int id_owner;
    private int id_creator;
    private int id_price_range;
    private int reviews_counter;
    private boolean validated;

    public Restaurant(int id, String name, String description, String web_site_url, int global_value, int id_owner, int id_creator, int id_price_range, int reviews_counter, boolean validated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.web_site_url = web_site_url;
        this.global_value = global_value;
        this.id_owner = id_owner;
        this.id_creator = id_creator;
        this.id_price_range = id_price_range;
        this.reviews_counter = reviews_counter;
        this.validated = validated;
    }

    public int getId_creator() {
        return id_creator;
    }

    public void setId_creator(int id_creator) {
        this.id_creator = id_creator;
    }

    Restaurant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWeb_site_url() {
        return web_site_url;
    }

    public void setWeb_site_url(String web_site_url) {
        this.web_site_url = web_site_url;
    }

    public int getGlobal_value() {
        return global_value;
    }

    public void setGlobal_value(int global_value) {
        this.global_value = global_value;
    }

    public int getId_owner() {
        return id_owner;
    }

    public void setId_owner(int id_owner) {
        this.id_owner = id_owner;
    }

    public int getId_price_range() {
        return id_price_range;
    }

    public void setId_price_range(int id_price_range) {
        this.id_price_range = id_price_range;
    }

    public int getReviews_counter() {
        return reviews_counter;
    }

    public void setReviews_counter(int reviews_counter) {
        this.reviews_counter = reviews_counter;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
    
    
    
}
