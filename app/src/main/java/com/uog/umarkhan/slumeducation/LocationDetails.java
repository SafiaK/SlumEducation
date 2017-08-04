package com.uog.umarkhan.slumeducation;

import java.io.Serializable;

/**
 * Created by Umar Khan on 6/6/2017.
 */

public class LocationDetails implements Serializable{

    String id, name,location,description,imageUrl,noOfChildren,phone,volunteerName,volunteerPhone;
    Double longitude, latitude;

    public LocationDetails() {
    }

    public LocationDetails(String id,String name, String location, String description,String imageUrl, String noOfChildren, String phone,String volunteerName,String volunteerPhone) {
        this.id=id;
        this.name = name;
        this.location=location;
        this.description = description;
        this.imageUrl=imageUrl;
        this.noOfChildren = noOfChildren;
        this.phone=phone;
        this.volunteerName=volunteerName;
        this.volunteerPhone=volunteerPhone;


    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getVolunteerPhone() {
        return volunteerPhone;
    }

    public void setVolunteerPhone(String volunteerPhone) {
        this.volunteerPhone = volunteerPhone;
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
