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
public class Notification
{

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

}
