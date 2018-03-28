import org.junit.Before;
import org.junit.Test;
import ru.sepnotican.printserver.PrintingHandler;
import ru.sepnotican.printserver.WrongAddressFormatException;
import ru.sepnotican.printserver.WrongPrinterNameException;

import javax.print.PrintException;
import java.io.IOException;


public class PrintingHandlerTest {

    PrintingHandler printingHandler;

    @Before
    public void init() {
        printingHandler = PrintingHandler.getInstance();
    }

    @Test(expected = WrongAddressFormatException.class)
    public void wrongPrinterAddressFormatTest() throws IOException, WrongAddressFormatException {
        printingHandler.printZPL("QWERTYUIOP", null, null);
    }

    @Test(expected = WrongPrinterNameException.class)
    public void wrongPrinterNameTest() throws PrintException, WrongPrinterNameException {
        printingHandler.printPDF("!@#$%^&*())", new byte[]{0});
    }

}
