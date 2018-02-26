package ru.sepnotican.printserver.entity;

public class Printer {
    private String address;
    private PrintMode printMode;

    public Printer() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PrintMode getPrintMode() {
        return printMode;
    }

    public void setPrintMode(PrintMode printMode) {
        this.printMode = printMode;
    }

    @Override
    public String toString() {
        return "Printer{" +
                "address='" + address + '\'' +
                ", printMode=" + printMode +
                '}';
    }
}