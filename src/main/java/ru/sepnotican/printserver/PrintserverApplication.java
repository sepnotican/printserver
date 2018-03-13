package ru.sepnotican.printserver;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import ru.sepnotican.printserver.servlet.MyServletRoot;
import ru.sepnotican.printserver.servlet.PrintServlet;

public class PrintserverApplication {

	private static final Logger logger = Logger.getLogger(PrintserverApplication.class);

	public static void main(String[] args) throws Exception {

		Server server = new Server(8899);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(MyServletRoot.class, "/*");
		handler.addServletWithMapping(PrintServlet.class, "/print");

		server.start();
		logger.info("Server stareted.");
		server.join();

	}
}
