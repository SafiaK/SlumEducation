package com.example.umarkhan.slumeducation;

import java.io.Serializable;

/**
 * Created by Umar Khan on 6/6/2017.
 */

public class LocationDetails implements Serializable{

    String id, name,location,description,imageUrl,noOfChildren;
    Double longitude, latitude;

    public LocationDetails() {
    }

    public LocationDetails(String id,String name, String location, String description,String imageUrl, String noOfChildren) {
        this.id=id;
        this.name = name;
        this.location=location;
        this.description = description;
        this.imageUrl=imageUrl;
        this.noOfChildren = noOfChildren;

    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNoOfChildren() {
        return noOfChildren;
    }
}
