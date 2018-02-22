package ru.sepnotican.printserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sepnotican.printserver.PrintService;
import ru.sepnotican.printserver.controller.restreflect.PrintRequest;
import ru.sepnotican.printserver.controller.restreflect.Printer;

@RestController
public class MainRestController {

    @Autowired
    private PrintService printService;

    @GetMapping("/echo")
    public String getEcho(){
        return "Echo from printserver";
    }

    @GetMapping("/example")
    public PrintRequest getExample(){
        PrintRequest printRequest = new PrintRequest();
        Printer printer = new Printer();
        printer.setAddress("192.168.0.1");
        printer.setPort(6100);
        printer.setUNCName("\\\\hostname123\\printername (or null for ip print)");
        printRequest.setDataZPL(getExampleZPL());
        printRequest.setPrinter(printer);

        return printRequest;
    }

    @PostMapping("/print")
    public String postPrint(@RequestBody PrintRequest printRequest){
        printService.print(printRequest);
        return "done";
    }


    private String getExampleZPL(){
            return "^XA\n" +
                    "^FO 0,10\n" +
                    "^GB632,0,2^FS\n" +
                    "^FO0,25\n" +
                    "^FB632,1,0,C,0\n" +
                    "^ASN,70,70\n" +
                    "^FDEXAMPLE^FS\n" +
                    "^FO0,100\n" +
                    "^GB632,0,2^FS\n" +
                    "^FO0,120\n" +
                    "^FB632,1,0,C,0\n" +
                    "^ASN,60,60\n" +
                    "^FDExAmPle^FS\n" +
                    "^FO0,180\n" +
                    "^FB632,1,0,C,0\n" +
                    "^ASN,60,60\n" +
                    "^FDWild^FS\n" +
                    "^FO0,240\n" +
                    "^GB632,0,2^FS\n" +
                    "^FO120,260\n" +
                    "^BY2\n" +
                    "^BCN,70,N,N,N\n" +
                    "^FDBARCODE128^FS - \n" +
                    "^XZ";
    }

}
