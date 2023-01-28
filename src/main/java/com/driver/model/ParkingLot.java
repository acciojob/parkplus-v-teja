package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String Address;

    @OneToMany(mappedBy = "parkingLot",cascade = CascadeType.ALL)
    private List<Spot> spotList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public void setSpotList(List<Spot> spotList) {
        this.spotList = spotList;
    }

    //all args constructor
    public ParkingLot(int id, String name, String address, List<Spot> spotList) {
        this.id = id;
        this.name = name;
        Address = address;
        this.spotList = spotList;
    }

    //no args constructor
    public ParkingLot(){
    }

    public ParkingLot(String name, String address) {
        this.name = name;
        Address = address;
    }

    public ParkingLot(String name, String address, List<Spot> spotList) {
        this.name = name;
        Address = address;
        this.spotList = spotList;
    }
}
