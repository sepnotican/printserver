package ru.sepnotican.printserver.servlet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class MyServletRoot {

    private static final Logger logger = Logger.getLogger(MyServletRoot.class);

    @RequestMapping("/")
    protected String doGetRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("GET / call from IP: " + req.getRemoteAddr());
        return "index.html";

    }
}
