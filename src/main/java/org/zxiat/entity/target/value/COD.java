package org.zxiat.entity.target.value;

import java.io.Serializable;

public class COD implements Serializable {

    private static final long serialVersionUID = 1L;
    private double lat;
    private double lon;

    public COD() {
    }

    public COD(double latitude, double longitude) {
        super();
        this.lat = latitude;
        this.lon = longitude;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double latitude) {
        this.lat = latitude;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double longitude) {
        this.lon = longitude;
    }

    @Override
    public String toString() {
        return lat + "N, " + lon + "E";
    }
}
