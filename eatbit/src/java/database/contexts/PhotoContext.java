/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.contexts;

import database.Photo;
import database.User;
import java.io.Serializable;

/**
 *
 * @author jacopo
 */
public class PhotoContext implements Serializable
{

    private User user;
    private Photo photo;

    public PhotoContext(User user, Photo photo)
    {
        this.user = user;
        this.photo = photo;
    }

    public PhotoContext()
    {
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Photo getPhoto()
    {
        return photo;
    }

    public void setPhoto(Photo photo)
    {
        this.photo = photo;
    }

}
