package com.synergyyy.SearchTasks;

public class DeleteImageRequest {
    String image;
    String frId;
    String type;

    public DeleteImageRequest(String image, String frId, String type) {
        this.image = image;
        this.frId = frId;
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public String getFrId() {
        return frId;
    }

    public String getType() {
        return type;
    }
}
