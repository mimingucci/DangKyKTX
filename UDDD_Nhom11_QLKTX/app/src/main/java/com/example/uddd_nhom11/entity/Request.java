
package com.example.uddd_nhom11.entity;

public class Request {
    String username, roomnumber, area, kyhoc, namhoc;
    int requesttype, requeststatus;

    public Request(String username, String roomnumber, String area, String kyhoc, String namhoc, int requesttype, int requeststatus) {
        this.username = username;
        this.roomnumber = roomnumber;
        this.area = area;
        this.kyhoc = kyhoc;
        this.namhoc = namhoc;
        this.requesttype = requesttype;
        this.requeststatus = requeststatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public int getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(int requesttype) {
        this.requesttype = requesttype;
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

    public int getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(int requeststatus) {
        this.requeststatus = requeststatus;
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
