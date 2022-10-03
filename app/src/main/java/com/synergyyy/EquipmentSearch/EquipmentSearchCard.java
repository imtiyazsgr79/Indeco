package com.synergyyy.EquipmentSearch;

public class EquipmentSearchCard {
    Long taskId;
    String taskNumber;
    String workspace;
    String status, buildingName, locationName;
    long scheduleDate;
    String afterImage;
    String beforeImage;
    String source;
    String equipName;

    public EquipmentSearchCard(Long taskId, String taskNumber, String workspace,
                               String status, String buildingName, String locationName, long scheduleDate,
                               String afterImage, String beforeImage, String source, String equipName) {
        this.taskId = taskId;
        this.taskNumber = taskNumber;
        this.workspace = workspace;
        this.status = status;
        this.buildingName = buildingName;
        this.locationName = locationName;
        this.scheduleDate = scheduleDate;
        this.afterImage = afterImage;
        this.beforeImage = beforeImage;
        this.source = source;
        this.equipName = equipName;
    }

    public String getStatus() {
        return status;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getLocationName() {
        return locationName;
    }

    public long getScheduleDate() {
        return scheduleDate;
    }

    public String getWorkspace() {
        return workspace;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getAfterImage() {
        return afterImage;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public String getSource() {
        return source;
    }

    public String getEquipName() {
        return equipName;
    }
}
