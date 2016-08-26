package database;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *Oggetto che rispecchia la tabella REVIEW_NOTIFICATIONS del database, rappresenta
 * la notifica di una recensione che il ristoratore riceve quando viene caricata
 * una recensione per il suo ristorante.
 * @author jacopo
 */
public class ReviewNotification implements Serializable
{
    private int id;
    private User  user;
    private Review review;
    private String restaurant_name;
    private Timestamp creation;

    public ReviewNotification()
    {
    }

    public void setId(int id)
    {
        this.id = id;
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
