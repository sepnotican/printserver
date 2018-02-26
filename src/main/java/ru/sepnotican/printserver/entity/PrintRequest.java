package ru.sepnotican.printserver.entity;

public class PrintRequest {

    private Printer printer;
    private String dataZPL;

    public PrintRequest() {
    }

    public PrintRequest(Printer printer, String dataZPL) {
        this.printer = printer;
        this.dataZPL = dataZPL;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public String getDataZPL() {
        return dataZPL;
    }

    public void setDataZPL(String dataZPL) {
        this.dataZPL = dataZPL;
    }

    @Override
    public String toString() {
        return "PrintRequest{" +
                "printer=" + printer +
                ", dataZPL='" + dataZPL + '\'' +
                '}';
    }
}
