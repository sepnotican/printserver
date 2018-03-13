package ru.sepnotican.printserver.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyServletRoot extends HttpServlet {

    private static final Logger logger = Logger.getLogger(MyServletRoot.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("GET / call from IP: " + req.getRemoteAddr());

        ClassLoader cl = MyServletRoot.class.getClassLoader();
        URL f = cl.getResource("static/index.html");
        if (f == null) {
            logger.error("Unable to find resource directory");
            throw new RuntimeException("Unable to find resource directory");
        }

        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(f.toURI())));
        } catch (URISyntaxException e) {
            logger.error("Unable to find load index.html");
            throw new RuntimeException("Unable to find load index.html");
        }

        resp.getWriter().println(content);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
