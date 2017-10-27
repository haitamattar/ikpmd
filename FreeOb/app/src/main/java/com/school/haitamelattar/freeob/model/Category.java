package com.school.haitamelattar.freeob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jordy on 27-10-17.
 */

public class Category {

    @SerializedName("category_id")
    @Expose
    private Long id = null;
    @SerializedName("category_name")
    @Expose
    private String name;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
