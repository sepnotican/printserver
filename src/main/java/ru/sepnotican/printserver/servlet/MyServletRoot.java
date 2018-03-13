package ru.sepnotican.printserver.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyServletRoot extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ClassLoader cl = MyServletRoot.class.getClassLoader();
        // We look for a file, as ClassLoader.getResource() is not
        // designed to look for directories (we resolve the directory later)
        URL f = cl.getResource("static/index.html");
        if (f == null) {
            throw new RuntimeException("Unable to find resource directory");
        }

        String content;
        try (BufferedInputStream buf = (BufferedInputStream) f.getContent();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf))) {
            content = new String(Files.readAllBytes(Paths.get(f.toURI())));

        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to find resource");
        }

        resp.getWriter().println(content);
        resp.setStatus(HttpServletResponse.SC_OK);
    }


}
