/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Timestamp;

/**
 *
 * @author jacopo
 */
public class Reply
{

    private int id;
    private String description;
    private Timestamp date_creation;
    private int id_review;
    private int id_owner;
    private Timestamp date_validation;
    private int id_validator;
    private boolean validated;

    public Reply()
    {
    }

    public Reply(int id, String description, Timestamp date_creation, int id_review, int id_owner, Timestamp date_validation, int id_validator, boolean validated)
    {
        this.id = id;
        this.description = description;
        this.date_creation = date_creation;
        this.id_review = id_review;
        this.id_owner = id_owner;
        this.date_validation = date_validation;
        this.id_validator = id_validator;
        this.validated = validated;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Timestamp getDate_creation()
    {
        return date_creation;
    }

    public void setDate_creation(Timestamp date_creation)
    {
        this.date_creation = date_creation;
    }

    public int getId_review()
    {
        return id_review;
    }

    public void setId_review(int id_review)
    {
        this.id_review = id_review;
    }

    public int getId_owner()
    {
        return id_owner;
    }

    public void setId_owner(int id_owner)
    {
        this.id_owner = id_owner;
    }

    public Timestamp getDate_validation()
    {
        return date_validation;
    }

    public void setDate_validation(Timestamp date_validation)
    {
        this.date_validation = date_validation;
    }

    public int getId_validator()
    {
        return id_validator;
    }

    public void setId_validator(int id_validator)
    {
        this.id_validator = id_validator;
    }

    public boolean isValidated()
    {
        return validated;
    }

    public void setValidated(boolean validated)
    {
        this.validated = validated;
    }

}
