package com.onedongua.abutton.model;

public class LocationData {
    private double latitude;
    private double longitude;
    private double altitude;

    public LocationData() {
    }

    public LocationData(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
