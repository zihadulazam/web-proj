package database;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 *Oggetto che rispecchia la tabella Photos nel database.
 * Il path della foto è relativo.
 * @author jacopo
 */
public class Photo implements Serializable
{

    private int id;
    private String name;
    private String description;
    private String path;
    private int id_restaurant;
    private int id_owner;

    public Photo(int id, String name, String description, String path, int id_restaurant, int id_owner)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
        this.id_restaurant = id_restaurant;
        this.id_owner = id_owner;
    }

    public Photo()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public int getId_restaurant()
    {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant)
    {
        this.id_restaurant = id_restaurant;
    }

    public int getId_owner()
    {
        return id_owner;
    }

    public void setId_owner(int id_owner)
    {
        this.id_owner = id_owner;
    }

}
