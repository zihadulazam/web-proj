package database.contexts;

import database.Photo;
import database.User;
import java.io.Serializable;

/**
 *Contiene il contesto di una foto, cioè l'utente che l'ha uploadata, il nome
 * del ristorante relativo alla foto e la foto stessa.
 * @author jacopo
 */
public class PhotoContext implements Serializable
{

    private User user;
    private Photo photo;
    private String restaurantName;

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

    /**
     * @return the restaurantName
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * @param restaurantName the restaurantName to set
     */
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

}
