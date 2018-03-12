package ru.sepnotican.printserver.servlet;

import com.google.gson.Gson;
import ru.sepnotican.printserver.PrintingHandler;
import ru.sepnotican.printserver.WrongAddressFormatException;
import ru.sepnotican.printserver.entity.PrintMode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;

public class PrintServlet extends HttpServlet {

    PrintingHandler printingHandler;

    public PrintServlet() {
        this.printingHandler = PrintingHandler.getInstance(); //todo inject
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(PrintingHandler.getInstance().getPrinterListInJson());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String printType = req.getHeader("printType");
        String printerName = req.getHeader("printerAddress");

        DataInputStream printDataInputStream = new DataInputStream(req.getInputStream());

        byte[] printData = new byte[req.getContentLength()];
        printDataInputStream.readFully(printData);


        if (printData.length > 0) {
            try {
                if (printType.equalsIgnoreCase(String.valueOf(PrintMode.ZPLSOCKET))) {
                    printingHandler.printZPL(printerName, printData);
                } else if (printType.equalsIgnoreCase(String.valueOf(PrintMode.PDFLOCAL))) {
                    printingHandler.printPDF(printerName, printData);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print(new ResponseMessage(resp.getStatus(), true
                            , "Wrong print type. ").toJson());
                }

            } catch (WrongAddressFormatException e) {
                //todo logging
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(new ResponseMessage(resp.getStatus(), true
                        , "Wrong address format. ").toJson());
            } catch (Exception e) {
                //todo logging
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, e.getMessage()).toJson());
            }
        }

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
