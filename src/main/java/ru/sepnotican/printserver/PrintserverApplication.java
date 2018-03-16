package ru.sepnotican.printserver;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrintserverApplication {

    private static final Logger logger = Logger.getLogger(PrintserverApplication.class);

    public static void main(String[] args) throws Exception {


        SpringApplication.run(PrintserverApplication.class);

        logger.info("Server started.");


    }
}
