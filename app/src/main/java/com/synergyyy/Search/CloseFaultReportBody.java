package com.synergyyy.Search;

import java.util.ArrayList;
import java.util.List;

public class CloseFaultReportBody {
    private List<String> remarks = null;
    String frId, status;
    private Building building;
    private Location location;
    private ArrayList<AttendedBy> attendedBy;
    private String username;

    public CloseFaultReportBody(List<String> remarks, String frId, String status,
                                Building building, Location location, ArrayList<AttendedBy> attendedBy,String username) {
        this.remarks = remarks;
        this.frId = frId;
        this.status = status;
        this.building = building;
        this.location = location;
        this.attendedBy = attendedBy;
        this.username=username;
    }

    public List<String> getRemarks() {
        return remarks;
    }
}
