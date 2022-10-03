package com.synergyyy.SearchTasks;

public class DeleteTaskImageRequest {

    long taskId;
    String image;
    String type;
    int id;

    public DeleteTaskImageRequest(long taskId, String image, String type, int id) {
        this.taskId = taskId;
        this.image = image;
        this.type = type;
        this.id = id;
    }
}
