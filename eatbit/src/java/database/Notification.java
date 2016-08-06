/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;

/**
 *
 * @author jacopo
 */
public class Notification implements Serializable
{
    /*public final static int REPLY_ACCEPTED= 0;
    public final static int REPLY_DENIED= 1;
    public final static int CREATION_REQ_ACCEPTED= 2;
    public final static int CLAIM_REQ_ACCEPTED= 3;
    public final static int CREATION_CLAIM_REQ_ACCEPTED= 4;
    public final static int CREATION_REQ_DENIED= 5;
    public final static int CLAIM_REQ_DENIED= 6;
    public final static int CREATION_CLAIM_REQ_DENIED= 7;
    public final static int REPLY_TO_USER_REVIEW= 8;
    public final static int REVIEW_TO__USER_RESTAURANT= 9;
    public final static int REMOVED_USER_PHOTO= 10;
    public final static int REMOVED_USER_REVIEW= 11;*/
    private int id;
    private int user_id;
    private String description;
    
    public Notification(int user_id, String description)
    {
        this.user_id = user_id;
        this.description = description;
    }

    public Notification()
    {
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
