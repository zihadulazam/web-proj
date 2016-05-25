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
public class User
{

    private int id;
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private String avatar_path;
    private int reviews_counter;
    private int reviews_positive;
    private int reviews_negative;
    private int type;

    
    public User()
    {
    }

    public int getReviews_negative()
    {
        return reviews_negative;
    }

    public void setReviews_negative(int reviews_negative)
    {
        this.reviews_negative = reviews_negative;
    }

    public String getAvatar_path()
    {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path)
    {
        this.avatar_path = avatar_path;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
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

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getReviews_counter()
    {
        return reviews_counter;
    }

    public void setReviews_counter(int reviews_counter)
    {
        this.reviews_counter = reviews_counter;
    }

    public int getReviews_positive()
    {
        return reviews_positive;
    }

    public void setReviews_positive(int reviews_positive)
    {
        this.reviews_positive = reviews_positive;
    }

}
