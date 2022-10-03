package com.synergyyy.SearchTasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadTaskImageResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("contactNo")
    @Expose
    public String contactNo;
    @SerializedName("frId")
    @Expose
    public Object frId;
    @SerializedName("division")
    @Expose
    public String division;
    @SerializedName("rank")
    @Expose
    public String rank;
    @SerializedName("signature")
    @Expose
    public Object signature;
    @SerializedName("taskId")
    @Expose
    public Integer taskId;

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public Object getFrId() {
        return frId;
    }

    public String getDivision() {
        return division;
    }

    public String getRank() {
        return rank;
    }

    public Object getSignature() {
        return signature;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public UploadTaskImageResponse(Integer id, String type, String image, String name, String contactNo, Object frId, String division, String rank, Object signature, Integer taskId) {
        this.id = id;
        this.type = type;
        this.image = image;
        this.name = name;
        this.contactNo = contactNo;
        this.frId = frId;
        this.division = division;
        this.rank = rank;
        this.signature = signature;
        this.taskId = taskId;
    }
}
