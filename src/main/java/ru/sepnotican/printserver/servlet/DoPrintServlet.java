package ru.sepnotican.printserver.servlet;

import com.google.gson.Gson;
import ru.sepnotican.printserver.PrintService;
import ru.sepnotican.printserver.WrongAddressFormatException;
import ru.sepnotican.printserver.entity.PrintRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class DoPrintServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Reader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder(req.getContentLength());
        while (reader.ready())
            stringBuilder.append((char) reader.read());
        Gson gson = new Gson();
        PrintRequest printRequest = gson.fromJson(stringBuilder.toString(), PrintRequest.class);

        if (printRequest != null) {
            try {
                new PrintService().print(printRequest); //todo inject
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
