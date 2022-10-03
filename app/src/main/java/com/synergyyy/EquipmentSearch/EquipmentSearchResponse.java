package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EquipmentSearchResponse {
    @SerializedName("taskId")
    @Expose
    public Integer taskId;
    @SerializedName("task_number")
    @Expose
    public String taskNumber;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("scheduleNumber")
    @Expose
    public String scheduleNumber;
    @SerializedName("briefDescription")
    @Expose
    public String briefDescription;
    @SerializedName("locationId")
    @Expose
    public Integer locationId;
    @SerializedName("locationName")
    @Expose
    public String locationName;
    @SerializedName("buildingId")
    @Expose
    public Integer buildingId;
    @SerializedName("buildingName")
    @Expose
    public String buildingName;
    @SerializedName("equipmentCode")
    @Expose
    public String equipmentCode;
    @SerializedName("equipmentName")
    @Expose
    public String equipmentName;
    @SerializedName("scheduleDate")
    @Expose
    public long scheduleDate;
    @SerializedName("completedBy")
    @Expose
    public String completedBy;
    @SerializedName("completedDate")
    @Expose
    public String completedDate;
    @SerializedName("acknowledgementTime")
    @Expose
    public String acknowledgementTime;
    @SerializedName("completedTime")
    @Expose
    public String completedTime;
    @SerializedName("remarks")
    @Expose
    public String remarks;
    @SerializedName("endDate")
    @Expose
    public String endDate;
    @SerializedName("dueDate")
    @Expose
    public String dueDate;
    @SerializedName("beforeImage")
    @Expose
    public String beforeImage;
    @SerializedName("afterImage")
    @Expose
    public String afterImage;


    public EquipmentSearchResponse(Integer taskId, String taskNumber, String status, String scheduleNumber, String briefDescription,
                              Integer locationId, String locationName, Integer buildingId, String buildingName, String equipmentCode,
                              String equipmentName, long scheduleDate, String completedBy, String completedDate, String acknowledgementTime,
                              String completedTime, String remarks, String endDate, String dueDate, String beforeImage, String afterImage) {
        this.taskId = taskId;
        this.taskNumber = taskNumber;
        this.status = status;
        this.scheduleNumber = scheduleNumber;
        this.briefDescription = briefDescription;
        this.locationId = locationId;
        this.locationName = locationName;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.equipmentCode = equipmentCode;
        this.equipmentName = equipmentName;
        this.scheduleDate = scheduleDate;
        this.completedBy = completedBy;
        this.completedDate = completedDate;
        this.acknowledgementTime = acknowledgementTime;
        this.completedTime = completedTime;
        this.remarks = remarks;
        this.endDate = endDate;
        this.dueDate = dueDate;
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public long getScheduleDate() {
        return scheduleDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public String getAcknowledgementTime() {
        return acknowledgementTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public String getAfterImage() {
        return afterImage;
    }
}


