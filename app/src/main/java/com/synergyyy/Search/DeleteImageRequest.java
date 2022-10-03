package com.synergyyy.Search;

public class DeleteImageRequest {
    String image;
    String frid;
    String type;

    public DeleteImageRequest(String image, String frid, String type) {
        this.image = image;
        this.frid = frid;
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public String getFrid() {
        return frid;
    }

    public String getType() {
        return type;
    }
}
