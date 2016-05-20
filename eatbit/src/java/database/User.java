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
public class User {
    private int id;
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private int reviews_counter;
    private int reviews_positive;

    public User(int id, String name, String surname, String nickname, String email, String password, int reviews_counter, int reviews_positive) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.reviews_counter = reviews_counter;
        this.reviews_positive = reviews_positive;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReviews_counter() {
        return reviews_counter;
    }

    public void setReviews_counter(int reviews_counter) {
        this.reviews_counter = reviews_counter;
    }

    public int getReviews_positive() {
        return reviews_positive;
    }

    public void setReviews_positive(int reviews_positive) {
        this.reviews_positive = reviews_positive;
    }
    
}
