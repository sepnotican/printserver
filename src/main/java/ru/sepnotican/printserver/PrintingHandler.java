package ru.sepnotican.printserver;


import com.google.gson.Gson;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrintingHandler {

    private static final PrintingHandler instance = new PrintingHandler();

    private PrintingHandler() {
    }

    public static PrintingHandler getInstance() {
        return instance;
    }

    public void printZPL(String address, byte[] data, String charset) throws WrongAddressFormatException, IOException {

        String[] addressSplitted = address.split(":");
        if (addressSplitted.length != 2)
            throw new WrongAddressFormatException("Wrong address format! Address = " + address);

        try (Socket socket = new Socket(addressSplitted[0], Integer.parseInt(addressSplitted[1]));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream())) {
            byte[] b = new String(data, "UTF-8").getBytes(charset);
            bufferedOutputStream.write(b);
            bufferedOutputStream.flush();
        }

    }

    public static PrintService getPrinterServiceByName(String name) {
        return PrinterList.printerMap.get(name);
    }

    public void printPDF(String printerName, byte[] printData) throws PrintException, WrongPrinterNameException {

        DocFlavor docType = DocFlavor.BYTE_ARRAY.PDF;

        PrintService printService = getPrinterServiceByName(printerName);

        if (printService == null) {
            throw new WrongPrinterNameException("That printer is not installed or not suppoted PDF format. Name = " + printerName);
        }

        DocAttributeSet attribute = new HashDocAttributeSet();
        attribute.add(OrientationRequested.LANDSCAPE);
        attribute.add(MediaSizeName.ISO_A4);

        DocPrintJob docPrintJob = printService.createPrintJob();
        Doc toBePrinted = new SimpleDoc(printData, docType, attribute);

        PrintRequestAttributeSet atr = new HashPrintRequestAttributeSet();
        atr.addAll(attribute);

        docPrintJob.print(toBePrinted, atr);
    }

    public String getPrinterListInJson() {
        return PrinterList.toJson();
    }

    private static class PrinterList {
        private static Set<String> printersNamesSet = new HashSet<>();

        private static Map<String, PrintService> printerMap = new HashMap<>();

        private static DocFlavor flavor = DocFlavor.BYTE_ARRAY.PDF;
        private static HashAttributeSet attribs = new HashAttributeSet();

        static {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, attribs);
            for (PrintService aPrinter : printServices) {
                printersNamesSet.add(aPrinter.getName());
                printerMap.put(aPrinter.getName(), aPrinter);
            }
        }

        public static String toJson() {
            Gson gson = new Gson();
            return gson.toJson(printersNamesSet);
        }
    }

}
