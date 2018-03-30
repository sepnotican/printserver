package ru.sepnotican.printserver.servlet;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.sepnotican.printserver.*;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PrintServlet extends HttpServlet {

    private static PrintingHandler printingHandler = PrintingHandler.getInstance();
    private static final Logger logger = Logger.getLogger(PrintServlet.class);

    public static void setPrintingHandler(PrintingHandler handler) {
        printingHandler = handler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        for (PrintService printService : PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.AUTOSENSE, null)) {
            resp.getWriter().println(" service : " + printService.getName() + '\n');
            for (DocFlavor flavor : printService.getSupportedDocFlavors()) {
                resp.getWriter().println(" flavour : " + flavor.toString() + '\n');
            }
        }

        resp.setContentType("text/plain");
        resp.getWriter().print(printingHandler.getPrinterListInJson());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("OPTIONS /print call from IP: " + req.getRemoteAddr());

        resp.setContentType("application/json");
        resp.getWriter().print(PrintingHandler.getInstance().getPrinterListInJson());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        String printType = req.getHeader("printType");
        String printerName = req.getHeader("printerAddress");
        String charset = req.getCharacterEncoding();

        ServletInputStream inputStream = req.getInputStream();
        byte[] printData = new byte[req.getContentLength()];

        byte b;
        int j = 0;
        while (!inputStream.isFinished()
                && j < req.getContentLength()) {
            if (!inputStream.isReady()) continue;
            b = (byte) inputStream.read();
            printData[j++] = b;
        }

        if (j < req.getContentLength()) {
            prepareBadRequestResponce(resp, "LENGTH WRONG!!! got = " + j + " await = " + req.getContentLength());
        }

        logger.info("POST /print call from IP: " + req.getRemoteAddr() +
                "\nheader printType = " + printType +
                "\nheader printerAddress = " + printerName +
                "\ncontent length = " + printData.length +
                //add zpl data for zpl printmode
                (printType.equalsIgnoreCase(String.valueOf(PrintMode.ZPLSOCKET))
                        ? "\nZPLDATA =\n" + new String(printData).replace('\n', ' ') : ""));

        if (printData.length == 0) {
            final String message = "Request body is null length. Nothing to print.";
            logger.warn(message);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
            return;
        }
        try {
            if (printType.equalsIgnoreCase(String.valueOf(PrintMode.ZPLSOCKET))) {
                if (charset == null) throw new UnsupportedEncodingException("Charset must be defined for ZPL printing");
                printingHandler.printZPL(printerName, printData, charset);
            } else if (printType.equalsIgnoreCase(String.valueOf(PrintMode.PDFLOCAL))) {
                printingHandler.printPDF(printerName, printData);
            } else {
                throw new WrongPrinterTypeException("Wrong print type header's value. Got: " + printType);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(new ResponseMessage(resp.getStatus(), false, "OK").toJson());

        } catch (UnsupportedEncodingException | WrongAddressFormatException
                | WrongPrinterNameException | WrongPrinterTypeException e) {
            prepareBadRequestResponce(resp, e);
        } catch (Exception e) {
            logger.error("Internal error. Message = " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, e.getMessage()).toJson());
        }


    }

    private void prepareBadRequestResponce(HttpServletResponse resp, Exception e) throws IOException {
        final String message = e.getClass().getName() + ':' + e.getMessage();
        prepareBadRequestResponce(resp, message);
    }

    private void prepareBadRequestResponce(HttpServletResponse resp, String message) throws IOException {
        logger.warn(message);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
    }

    private static class ResponseMessage {
        private int code;
        private boolean hasErrors;
        private String message;

        ResponseMessage(int code, boolean hasErrors, String message) {
            this.code = code;
            this.hasErrors = hasErrors;
            this.message = message;
        }

        public String toJson() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}
