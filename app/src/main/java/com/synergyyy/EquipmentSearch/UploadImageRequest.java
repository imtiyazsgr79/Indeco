package com.synergyyy.EquipmentSearch;

public class UploadImageRequest {
    private String taskId;
    private String image;
    private String contactNo;
    private String rank;
    private String division;
    private String name;
    private String type;

    public UploadImageRequest(String taskId, String image, String contactNo, String rank, String division, String name, String type) {
        this.taskId = taskId;
        this.image = image;
        this.contactNo = contactNo;
        this.rank = rank;
        this.division = division;
        this.name = name;
        this.type = type;
    }
}
