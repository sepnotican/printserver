package ru.sepnotican.printserver;

import ru.sepnotican.printserver.entity.PrintRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintService {


    public void print(PrintRequest printRequest) throws WrongAddressFormatException {

        printBySocket(printRequest);

    }

    private void printBySocket(PrintRequest printRequest) throws WrongAddressFormatException {

        String[] addressSplitted = printRequest.getPrinter().getAddress().split(":");
        if (addressSplitted.length != 2)
            throw new WrongAddressFormatException("Wrong address format!");

        try (Socket socket = new Socket(addressSplitted[0], Integer.parseInt(addressSplitted[1]));
             PrintWriter pw = new PrintWriter(socket.getOutputStream())) {
            byte[] bytes = printRequest.getDataZPL().getBytes();
            for (byte aByte : bytes) {
                pw.print(aByte);
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace(); //todo logging
        }

    }
}
