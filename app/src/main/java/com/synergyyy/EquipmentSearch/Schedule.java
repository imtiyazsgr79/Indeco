package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("briefDescription")
    @Expose
    public String briefDescription;
    @SerializedName("scheduleNumber")
    @Expose
    public String scheduleNumber;

    public Schedule(String briefDescription, String scheduleNumber) {
        this.briefDescription = briefDescription;
        this.scheduleNumber = scheduleNumber;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }
}
