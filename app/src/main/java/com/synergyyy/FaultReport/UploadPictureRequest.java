package com.synergyyy.FaultReport;

public class UploadPictureRequest {
    private String frId;
    private StringBuilder image;
    private String name=null;
    private String contactNo=null;
    private String  signature;
    private String rank=null;
    private String division=null;


    public UploadPictureRequest(String frId, StringBuilder image, String name, String contactNo, String rank, String division, String signature) {
        this.frId = frId;
        this.image = image;
        this.name = name;
        this.contactNo = contactNo;
        this.rank = rank;
        this.division = division;
        this.signature = signature;
    }
}
