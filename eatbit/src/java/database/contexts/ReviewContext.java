/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Reply;
import database.Review;
import database.User;
import java.io.Serializable;

/**
 * Contesto di una review (usato per review segnalate), contiene lo user
 * creatore della review e la review in questione.
 *
 * @author jacopo
 */
public class ReviewContext implements Serializable
{

    private User user;
    private Review review;
    private Reply reply;

    public ReviewContext()
    {
    }

    public Reply getReply()
    {
        return reply;
    }

    public void setReply(Reply reply)
    {
        this.reply = reply;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Review getReview()
    {
        return review;
    }

    public void setReview(Review review)
    {
        this.review = review;
    }
}
