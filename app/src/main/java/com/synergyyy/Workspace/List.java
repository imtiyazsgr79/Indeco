package com.synergyyy.Workspace;

public class List {
    String status;
    String contactNo;
    String frId;
    String name;
    List image;
    String type;
    long id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getImage() {
        return image;
    }

    public void setImage(List image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List(String status, String contactNo, String frId, String name, List image, String type, long id) {
        this.status = status;
        this.contactNo = contactNo;
        this.frId = frId;
        this.name = name;
        this.image = image;
        this.type = type;
        this.id = id;
    }
}
