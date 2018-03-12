package ru.sepnotican.printserver;


import com.google.gson.Gson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrintingHandler {

    private final static PrintingHandler instance = new PrintingHandler();

    private PrintingHandler() {
    }

    public static PrintingHandler getInstance() {
        return instance;
    }

    public void printZPL(String address, byte[] data) throws WrongAddressFormatException, IOException {
        printBySocket(address, data);
    }

    private void printBySocket(String address, byte[] data) throws WrongAddressFormatException, IOException {

        String[] addressSplitted = address.split(":");
        if (addressSplitted.length != 2)
            throw new WrongAddressFormatException("Wrong address format!");

        try (Socket socket = new Socket(addressSplitted[0], Integer.parseInt(addressSplitted[1]));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream())) {
            bufferedOutputStream.write(data); //byte[] b = zplData.getBytes("windows-1251");
            bufferedOutputStream.flush();
        }

    }

    public static PrintService getPrinterServiceByName(String name) {
        return PrinterList.printerMap.get(name);
    }

    public void printPDF(String printerName, byte[] printData) throws IOException, PrinterException {

        PDDocument pdDocument = PDDocument.load(printData);

        PrinterJob pjob = PrinterJob.getPrinterJob();

        pjob.setPrintService(getPrinterServiceByName(printerName));

        pjob.setPageable(new PDFPageable(pdDocument));

        pjob.print();

    }

    public String getPrinterListInJson() {
        return new PrinterList().toJson();
    }

    private static class PrinterList {
        private static Set<String> printersNamesSet = new HashSet<>();

        private static Map<String, PrintService> printerMap = new HashMap<>();

        private static DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
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
