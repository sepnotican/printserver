package ru.sepnotican.printserver;

import ru.sepnotican.printserver.controller.restreflect.PrintRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintService {


    public void print(PrintRequest printRequest) {

        if (printRequest.getPrinter().getUNCName() == null)
            printBySocket(printRequest);

    }

    private void printBySocket(PrintRequest printRequest) {

        try (
            Socket socket = new Socket(printRequest.getPrinter().getAddress(), printRequest.getPrinter().getPort());
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
        ){
            byte[] bytes = printRequest.getDataZPL().getBytes();

            for (int i = 0; i < bytes.length; i++) {
                pw.print(bytes[i]);
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
