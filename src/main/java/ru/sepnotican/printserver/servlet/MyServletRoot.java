package ru.sepnotican.printserver.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyServletRoot extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MyServletRoot.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("GET / call from IP: " + req.getRemoteAddr());

        StringBuilder content = new StringBuilder();
        try {
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("static/index.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            while (br.ready())
                content.append(br.readLine());

        } catch (Exception e) {
            logger.error("Unable to load index.html. Error: " + e.getMessage());
            throw e;
        }

        resp.getWriter().println(content.toString());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
