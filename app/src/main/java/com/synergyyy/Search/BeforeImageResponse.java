package com.synergyyy.Search;

public class BeforeImageResponse {
    long id;
    String type,image,name,contactNo,frId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public BeforeImageResponse(long id, String type, String image, String name, String contactNo, String frId) {
        this.id = id;
        this.type = type;
        this.image = image;
        this.name = name;
        this.contactNo = contactNo;
        this.frId = frId;
    }

}
