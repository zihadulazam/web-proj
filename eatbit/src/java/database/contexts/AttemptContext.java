/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Restaurant;
import database.User;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe che contiene il contesto in cui un utente crea un ristorante o lo
 * reclama come proprietario. Contiene lo user, il ristorante e un timestamp per
 * evitare che il contesto venga esaminato da più admin, vi è inoltre una flag
 * per segnalare se questo contesto riguarda una creazione (0) o un reclamo(1),
 * o è una creazione dove il creatore dice di essere anche il prorietario (2).
 *
 * @author jacopo
 */
public class AttemptContext implements Serializable
{

    private User user;
    private Restaurant restaurant;
    private int isClaim;
    private String usertextclaim;

    public int getIsClaim()
    {
        return isClaim;
    }

    public void setIsClaim(int isClaim)
    {
        this.isClaim = isClaim;
    }

    public AttemptContext(User user, Restaurant restaurant, Timestamp timestamp, int isClaim)
    {
        this.user = user;
        this.restaurant = restaurant;
        this.isClaim = isClaim;
    }

    public AttemptContext()
    {
    }

    public String getUsertextclaim()
    {
        return usertextclaim;
    }

    public void setUsertextclaim(String usertextclaim)
    {
        this.usertextclaim = usertextclaim;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }
}
