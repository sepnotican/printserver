package com;

import com.google.gson.Gson;
import ru.sepnotican.printserver.entity.PrintMode;
import ru.sepnotican.printserver.entity.PrintRequest;
import ru.sepnotican.printserver.entity.Printer;

public class MakeJson {
    public static void main(String[] args) {
        Printer printer = new Printer();
        printer.setAddress("192.168.0.1:6100");
        printer.setPrintMode(PrintMode.SOCKET);
        PrintRequest printRequest = new PrintRequest(printer, "an zpl data");
        Gson gson = new Gson();
        System.out.println(gson.toJson(printRequest));
    }
}
