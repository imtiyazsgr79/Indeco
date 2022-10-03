package com.synergyyy.Search;


import java.util.ArrayList;
import java.util.List;

public class EditFaultReportRequest {
    private fmm fmm;
    private String acknowledgerCode;
    private AcknowledgedBy acknowledgedBy = null;
    private String frId;
    private String technicianSignature;
    private Building building;

    private Location location;

    private String requestorName;

    private Department department;

    private String requestorContactNo;

    private String locationDesc;

    private FaultCategory faultCategory;

    private String faultCategoryDesc;

    private Priority priority;

    private MaintGrp maintGrp;

    private Division division;

    private String observation;

    private String diagnosis;

    private String actionTaken;

    private CostCenter costCenter = null;

    private String status;

    private Equipment equipment = null;

    private List<String> remarks = null;

    private ArrayList<AttendedBy> attendedBy;

    public EditFaultReportRequest(String technicianSignature, AcknowledgedBy acknowledgedBy, String acknowledgerCode, String frId, Building building, Location location, String reqtorName,
                                  Department department, String reqtorContactNo, String locOtherDesc,
                                  FaultCategory faultCategory, String faultCategoryDesc, Priority priority,
                                  MaintGrp maintGrp, Division division, String observe, String diagnosis, String actionTaken,
                                  CostCenter costCenter, String status, Equipment equipment,
                                  List<String> remarks, ArrayList<AttendedBy> attendedBy,fmm fmm) {
        this.fmm=fmm;
       this.technicianSignature=technicianSignature;
                this.acknowledgedBy = acknowledgedBy;
        this.acknowledgerCode = acknowledgerCode;
        this.frId = frId;
        this.building = building;
        this.location = location;
        this.requestorName = reqtorName;
        this.department = department;
        this.requestorContactNo = reqtorContactNo;
        this.locationDesc = locOtherDesc;
        this.faultCategory = faultCategory;
        this.faultCategoryDesc = faultCategoryDesc;
        this.priority = priority;
        this.maintGrp = maintGrp;
        this.division = division;
        this.observation = observe;
        this.diagnosis = diagnosis;
        this.actionTaken = actionTaken;
        this.costCenter = costCenter;
        this.status = status;
        this.equipment = equipment;
        this.remarks = remarks;
        this.attendedBy = attendedBy;
    }

    public com.synergyyy.Search.fmm getFmm() {
        return fmm;
    }

    public void setFmm(com.synergyyy.Search.fmm fmm) {
        this.fmm = fmm;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public void setBldgId(Building bldgId) {
        this.building = bldgId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setRequestorContactNo(String requestorContactNo) {
        this.requestorContactNo = requestorContactNo;
    }


    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public void setFaultCategory(FaultCategory faultCategory) {
        this.faultCategory = faultCategory;
    }

    public void setFaultCategoryName(String faultCategoryName) {
        this.faultCategoryDesc = faultCategoryName;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setMaintGrp(MaintGrp maintGrp) {
        this.maintGrp = maintGrp;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public void setAttendedBy(ArrayList<AttendedBy> attendedBy) {
        this.attendedBy = attendedBy;
    }

    public String getFrId() {
        return frId;
    }

    public Building getBldgId() {
        return building;
    }

    public Location getLocation() {
        return location;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public Department getDepartment() {
        return department;
    }

    public String getRequestorContactNo() {
        return requestorContactNo;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public FaultCategory getFaultCategory() {
        return faultCategory;
    }

    public String getFaultCategoryName() {
        return faultCategoryDesc;
    }

    public Priority getPriority() {
        return priority;
    }

    public MaintGrp getMaintGrp() {
        return maintGrp;
    }

    public Division getDivision() {
        return division;
    }

    public String getObservation() {
        return observation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public CostCenter getCostCenter() {
        return costCenter;
    }

    public String getStatus() {
        return status;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public ArrayList<AttendedBy> getAttendedBy() {
        return attendedBy;
    }

}

class AcknowledgedBy {
    String name, rank, signature;

    public AcknowledgedBy(String name, String rank, String signature) {
        this.name = name;
        this.rank = rank;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

class FaultCategory {
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class CostCenter {


    private Integer id = null;

    public CostCenter(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
    }
}

class Location {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
    }
}


class Building {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
    }
}


class Division {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
    }
}


class Priority {
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [  id = " + id + "]";
    }
}


class Department {


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [  id = " + id + "]";
    }
}


class MaintGrp {
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
    }
}

class AttendedBy {
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class Equipment {
    Integer id;

    public Equipment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}