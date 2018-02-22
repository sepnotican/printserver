package ru.sepnotican.printserver.controller.restreflect;

public class PrintRequest {

    private Printer printer;
    private String dataZPL;

    public PrintRequest() {
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
}
