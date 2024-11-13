package com.example.quanlyktx.entity;

public class Rent {
    String username, roomnumber, roomarea, kyhoc, namhoc;

    public Rent() {}

    public Rent(String username, String roomnumber, String roomarea, String kyhoc, String namhoc) {
        this.username = username;
        this.roomnumber = roomnumber;
        this.roomarea = roomarea;
        this.kyhoc = kyhoc;
        this.namhoc = namhoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getRoomarea() {
        return roomarea;
    }

    public void setRoomarea(String roomarea) {
        this.roomarea = roomarea;
    }

    public String getKyhoc() {
        return kyhoc;
    }

    public void setKyhoc(String kyhoc) {
        this.kyhoc = kyhoc;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }
}