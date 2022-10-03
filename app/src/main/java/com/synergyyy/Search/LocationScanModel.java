package com.synergyyy.Search;

public class LocationScanModel {
    private String frId,locationCode;

    private GeoLocation geoLocation;

    public LocationScanModel(String frId, String locationCode, GeoLocation geoLocation) {
        this.frId = frId;
        this.locationCode = locationCode;
        this.geoLocation = geoLocation;
    }
}
