package ru.sepnotican.printserver.servlet;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.sepnotican.printserver.PrintMode;
import ru.sepnotican.printserver.PrintingHandler;
import ru.sepnotican.printserver.WrongAddressFormatException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PrintServlet extends HttpServlet {

    PrintingHandler printingHandler;
    private static final Logger logger = Logger.getLogger(PrintServlet.class);

    public PrintServlet() {
        this.printingHandler = PrintingHandler.getInstance(); //todo inject
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("OPTIONS /print call from IP: " + req.getRemoteAddr());
        resp.getWriter().print(PrintingHandler.getInstance().getPrinterListInJson());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String printType = req.getHeader("printType");
        String printerName = req.getHeader("printerAddress");
        String charset = req.getCharacterEncoding();

        BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
        byte[] printData = new byte[req.getContentLength()];
        bis.read(printData, 0, req.getContentLength());

        logger.info("POST /print call from IP: " + req.getRemoteAddr() +
                "\nheader printType = " + printType +
                "\nheader printerAddress = " + printerName +
                "\ncontent length = " + printData.length +
                //add zpl data for zpl printmode
                (printType.equalsIgnoreCase(String.valueOf(PrintMode.ZPLSOCKET))
                        ? "\nZPLDATA =\n" + new String(printData).replace('\n', ' ') : ""));

        if (printData.length > 0) {
            try {
                if (printType.equalsIgnoreCase(String.valueOf(PrintMode.ZPLSOCKET))) {
                    printingHandler.printZPL(printerName, printData, charset);
                } else if (printType.equalsIgnoreCase(String.valueOf(PrintMode.PDFLOCAL))) {
                    printingHandler.printPDF(printerName, printData);
                } else {
                    final String message = "Wrong print type header's value. Got: " + printType;
                    logger.warn(message);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
                }

            } catch (UnsupportedEncodingException e) {
                final String message = "Wrong encoding. Got = " + charset;
                logger.error(message);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
            } catch (WrongAddressFormatException e) {
                final String message = "Wrong address format. Address = " + printerName;
                logger.error(message);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Internal error. Message = " + e.getMessage());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, e.getMessage()).toJson());
            }
        } else {
            final String message = "Request body is null length. Nothing to print.";
            logger.warn(message);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new ResponseMessage(resp.getStatus(), true, message).toJson());
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
