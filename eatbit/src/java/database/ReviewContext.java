/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *Contesto di una review (usato per review segnalate), contiene lo user 
 * creatore della review e la review in questione.
 * @author jacopo
 */
public class ReviewContext {
    private User user;
    private Review review;

    public ReviewContext(User user, Review review) {
        this.user = user;
        this.review = review;
    }

    public ReviewContext() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
