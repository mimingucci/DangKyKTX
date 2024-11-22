package com.example.uddd_nhom11.entity;

public class Room {
    String roomnumber, area, floor;
    long roomprice;
    int roomtype;

    public Room() {}

    public Room(String roomnumber, String area, String floor, long roomprice, int roomtype) {
        this.roomnumber = roomnumber;
        this.area = area;
        this.floor = floor;
        this.roomprice = roomprice;
        this.roomtype = roomtype;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public long getRoomprice() {
        return roomprice;
    }

    public int getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(int roomtype) {
        this.roomtype = roomtype;
    }

    public void setRoomprice(long roomprice) {
        this.roomprice = roomprice;
    }


}
