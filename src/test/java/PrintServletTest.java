import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.sepnotican.printserver.PrintingHandler;
import ru.sepnotican.printserver.WrongPrinterNameException;
import ru.sepnotican.printserver.servlet.MyServletRoot;
import ru.sepnotican.printserver.servlet.PrintServlet;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class PrintServletTest {
    Server server;
    Gson gson;

    @Before
    public void init() throws Exception {

        gson = new Gson();

        server = new Server(8899);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(MyServletRoot.class, "/*");
        handler.addServletWithMapping(PrintServlet.class, "/print");

        server.start();

    }

    @After
    public void after() throws Exception {
        server.stop();
    }

    @Test
    public void optionsPrint() throws Exception {
        mockPrintingHandler();

        URL obj = new URL("http://localhost:8899/print");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("OPTIONS");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //validate json
        gson.fromJson(response.toString(), HashSet.class);

    }

    @Test
    public void postPrint() throws Exception {
        mockPrintingHandler();

        URL obj = new URL("http://localhost:8899/print");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("printerAddress", "QWERTY");
        con.setRequestProperty("printType", "PDFLOCAL");
        con.setRequestProperty("Content-Type", "application/octet-stream; charset=utf-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.getOutputStream().write(0);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Assert.assertEquals(200, con.getResponseCode());
        Assert.assertEquals("", response.toString());

    }

    @Test
    public void postPrintWrongType() throws Exception {
        mockPrintingHandler();

        URL obj = new URL("http://localhost:8899/print");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("printerAddress", "QWERTY");
        con.setRequestProperty("printType", "blabla");
        con.setRequestProperty("Content-Type", "application/octet-stream; charset=utf-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.getOutputStream().write(0);

        Assert.assertEquals(400, con.getResponseCode());
    }

    @Test
    public void postPrintWrongName() throws Exception {
        PrintingHandler printingHandler = mockPrintingHandler();
        Mockito.doThrow(WrongPrinterNameException.class)
                .when(printingHandler)
                .printPDF("NAME", new byte[]{0});


        URL obj = new URL("http://localhost:8899/print");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("printerAddress", "NAME");
        con.setRequestProperty("printType", "PDFLOCAL");
        con.setRequestProperty("Content-Type", "application/octet-stream; charset=utf-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.getOutputStream().write(0);

        Assert.assertEquals(400, con.getResponseCode());
    }

    @Test
    public void postPrintWrongEncoding() throws Exception {
        PrintingHandler printingHandler = mockPrintingHandler();
        Mockito.doThrow(UnsupportedEncodingException.class)
                .when(printingHandler)
                .printZPL("NAME", new byte[]{0}, "bla");


        URL obj = new URL("http://localhost:8899/print");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("printerAddress", "NAME");
        con.setRequestProperty("printType", "ZPLSOCKET");
        con.setRequestProperty("Content-Type", "application/octet-stream; charset=bla");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.getOutputStream().write(0);

        Assert.assertEquals(400, con.getResponseCode());
    }

    private PrintingHandler mockPrintingHandler() throws ServletException {
        PrintingHandler printingHandler = Mockito.mock(PrintingHandler.class);

        Handler handler = server.getHandler();
        ServletHandler servletHandler = (ServletHandler) handler;
        PrintServlet printServlet = null;
        for (ServletHolder servletHolder : servletHandler.getServlets()) {
            if (servletHolder.getHeldClass().equals(PrintServlet.class)) {
                ((PrintServlet) servletHolder.getServlet()).setPrintingHandler(printingHandler);
            }
        }
        return printingHandler;
    }

}
