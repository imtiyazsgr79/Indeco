package com.synergyyy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Equipment {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("building")
    @Expose
    public Building building;
    @SerializedName("equipmentCode")
    @Expose
    public String equipmentCode;

    public Equipment(String name, Location location, Building building, String equipmentCode) {
        this.name = name;
        this.location = location;
        this.building = building;
        this.equipmentCode = equipmentCode;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Building getBuilding() {
        return building;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }
}
