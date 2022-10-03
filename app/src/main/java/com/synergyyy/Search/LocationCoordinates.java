package com.synergyyy.Search;

public class LocationCoordinates {
    double longitude,latitude;

    public LocationCoordinates() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocationCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
