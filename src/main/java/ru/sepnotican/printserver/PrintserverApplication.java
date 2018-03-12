package ru.sepnotican.printserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import ru.sepnotican.printserver.servlet.MyServletHey;
import ru.sepnotican.printserver.servlet.MyServletRoot;
import ru.sepnotican.printserver.servlet.PrintServlet;

public class PrintserverApplication {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(MyServletRoot.class, "/*");
		handler.addServletWithMapping(MyServletHey.class, "/hey/*");
		handler.addServletWithMapping(PrintServlet.class, "/print");

		server.start();
		server.join();

	}
}
