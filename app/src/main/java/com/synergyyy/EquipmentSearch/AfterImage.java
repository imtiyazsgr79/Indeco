package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class AfterImage {

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

        public AfterImage(String image, String name, String contactNo, int id) {
            this.image = image;
            this.name = name;
            this.id = id;
            this.contactNo = contactNo;
        }

    }
