package com.synergyyy.EquipmentSearch;

import com.synergyyy.Search.fmm;

import java.util.List;

public class GetUpdatePmTaskRequest {
    private fmm fmm;
    String status;
    List<String> remarks;
    Long completedTime;
    Long completedDate;
    int taskId;
    String tech_signature;
    Acknowledger acknowledger;


    public GetUpdatePmTaskRequest(String tech_signature, String status, List<String> remarks,
                                  Long completedTime, Long completedDate, int taskId, Acknowledger acknowledger, fmm fmm) {
        this.fmm = fmm;
        this.tech_signature = tech_signature;
        this.status = status;
        this.remarks = remarks;
        this.completedTime = completedTime;
        this.completedDate = completedDate;
        this.taskId = taskId;
        this.acknowledger = acknowledger;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public Long getCompletedTime() {
        return completedTime;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public Acknowledger getAcknowledger() {
        return acknowledger;
    }
}

class Acknowledger {
    String rank;
    String signature;
    String name;

    public Acknowledger(String rank, String signature, String name) {
        this.rank = rank;
        this.signature = signature;
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }
}
