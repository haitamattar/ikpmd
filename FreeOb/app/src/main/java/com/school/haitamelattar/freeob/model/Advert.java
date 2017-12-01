package com.school.haitamelattar.freeob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Advert implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("advert_description")
    @Expose
    private String advert_description;
    @SerializedName("category_name")
    @Expose
    private String category_name;
    @SerializedName("advert_name")
    @Expose
    private String advert_name;
    @SerializedName("image")
    @Expose
    private String encoded_image;

    public Advert(String id, String user_id, String name, String description, String username, String categoryName, String image) {
        this.id = id;
        this.user_id = user_id;
        this.advert_name = name;
        this.advert_description = description;
        this.user_name = username;
        this.category_name = categoryName;
        this.encoded_image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return advert_name;
    }

    public void setName(String name) {
        this.advert_name = name;
    }

    public String getDescription() {
        return advert_description;
    }

    public void setDescription(String description) {
        this.advert_description = description;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String categoryName) {
        this.category_name = categoryName;
    }

    public String getImage() {
        return encoded_image;
    }
}
