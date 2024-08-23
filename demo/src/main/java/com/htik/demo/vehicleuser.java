package com.htik.demo;

import java.util.List;

public class vehicleuser {
    private String user_id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private List<vehicle> vehicles;


    public String getUserID() {
        return user_id;
    }

    public void setUserID(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}

