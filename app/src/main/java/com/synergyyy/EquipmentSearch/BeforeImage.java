package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeforeImage {

    @SerializedName("image")
    @Expose
    public String image;
    String name;
    String contactNo;
    int id;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public int getId() {
        return id;
    }

    public BeforeImage(String image, String name, String contactNo, int id) {
        this.image = image;
        this.name = name;
        this.contactNo = contactNo;
        this.id = id;
    }
}
