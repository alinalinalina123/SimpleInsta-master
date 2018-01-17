package com.michalenko.olga.simpleinsta.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by mac-242 on 1/17/18.
 */
@IgnoreExtraProperties
public class Post  {


    private String imagePath;
    private String comment;
    private String owner;

    public Post() {

    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


}