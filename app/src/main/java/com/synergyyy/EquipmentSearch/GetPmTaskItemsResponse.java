package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.synergyyy.Search.fmm;

import java.util.List;


public class GetPmTaskItemsResponse {

    public String tech_signature;
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("beforeImage")
    @Expose
    public BeforeImage beforeImage;
    @SerializedName("afterImage")
    @Expose
    public AfterImage afterImage;
    @SerializedName("equipment")
    @Expose
    public Equipment equipment;
    @SerializedName("schedule")
    @Expose
    public Schedule schedule;
    @SerializedName("scheduleDate")
    @Expose
    public Long scheduleDate;
    @SerializedName("endDate")
    @Expose
    public Object endDate;
    @SerializedName("remarks")
    @Expose
    public List<String> remarks = null;
    @SerializedName("taskNumber")
    @Expose
    public String taskNumber;
    @SerializedName("completedDate")
    @Expose
    public Long completedDate;
    @SerializedName("completedBy")
    @Expose
    public String completedBy;
    @SerializedName("completedTime")
    @Expose
    public String completedTime;

    public Acknowledger acknowledger;

    @SerializedName("dueDate")
    @Expose
    public Object dueDate;

    private fmm fmm;

    public GetPmTaskItemsResponse(Long id, String status, BeforeImage beforeImage, AfterImage afterImage, Equipment equipment,
                                  Schedule schedule, Long scheduleDate, Object endDate, List<String> remarks,
                                  String taskNumber, Long completedDate, String completedBy, String completedTime,
                                  Acknowledger acknowledger, Object dueDate,fmm fmm) {
        this.fmm=fmm;
        this.id = id;
        this.status = status;
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
        this.equipment = equipment;
        this.schedule = schedule;
        this.scheduleDate = scheduleDate;
        this.endDate = endDate;
        this.remarks = remarks;
        this.taskNumber = taskNumber;
        this.completedDate = completedDate;
        this.completedBy = completedBy;
        this.completedTime = completedTime;
        this.acknowledger = acknowledger;
        this.dueDate = dueDate;
    }

    public fmm getFmm() {
        return fmm;
    }

    public void setFmm(fmm fmm) {
        this.fmm = fmm;
    }

    public String getTech_signature() {
        return tech_signature;
    }

    public void setTech_signature(String tech_signature) {
        this.tech_signature = tech_signature;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public BeforeImage getBeforeImage() {
        return beforeImage;
    }

    public AfterImage getAfterImage() {
        return afterImage;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getScheduleDate() {
        return scheduleDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public Acknowledger getAcknowledger() {
        return acknowledger;
    }
}

