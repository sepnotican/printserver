package ru.sepnotican.printserver.controller.restreflect;

public class Printer {
    private String address;
    private int port;
    private String UNCName;

    public Printer() {
    }


    public String getUNCName() {
        return UNCName;
    }

    public void setUNCName(String UNCName) {
        this.UNCName = UNCName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
