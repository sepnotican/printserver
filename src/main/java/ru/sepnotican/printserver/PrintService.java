package ru.sepnotican.printserver;


import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PrintService {


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

    public void printPDF(String printerName, byte[] printData) throws IOException, PrinterException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(printData);

        ByteBuffer bb = ByteBuffer.wrap(printData);

        PDFFile pdfFile;
        try {
            pdfFile = new PDFFile(bb);
        } catch (Exception e) {
            //todo logging
            bb.clear();
            throw e;
        }

        PDFPrintPage pages = new PDFPrintPage(pdfFile);

        PrinterJob pjob = PrinterJob.getPrinterJob();

//        PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
//        Paper paper = new Paper();
//        paper.setSize(pf.getWidth(), pf.getHeight());
//        paper.setImageableArea(0, 0, pf.getWidth(), pf.getHeight());
//        pf.setPaper(paper);

//        Book book = new Book();
//        book.append(pages, pf, pdfFile.getNumPages());
//        pjob.setPageable(book);
        pjob.setPrintable(pages);
        pjob.print();

        bb.clear();

    }
}
