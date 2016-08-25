package database.contexts;

import database.Reply;
import database.Review;
import database.User;
import java.io.Serializable;

/**
 * Contiene informazioni utili per mostrare una reply in attesa di conferma,
 * comprendente della relative review e dello user che l'ha fatta (la reply).
 *
 * @author jacopo
 */
public class ReplyContext implements Serializable
{

    private Review review;
    private Reply reply;
    private User user;

    public ReplyContext()
    {
    }

    public ReplyContext(Review review, Reply reply, User user)
    {
        this.review = review;
        this.reply = reply;
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

}
