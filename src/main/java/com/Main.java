import javax.print.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        System.out.println("...");
        PrintService printService5[] = PrintServiceLookup.lookupPrintServices(flavor,null);
        PrintService ps = null;
        for (int i = 0; i < printService5.length; i++) {
            if (printService5[i].getName().equalsIgnoreCase("IT_1C_HP")){
                System.out.println(printService5[i].getName());
                ps = printService5[i];
            }
            if (ps != null){
//                String data = getZPL();
                FileInputStream fis = new FileInputStream("data.pdf");

//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());
                Doc doc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);

                DocPrintJob job = ps.createPrintJob();
                DocFlavor[] supportedDocFlavors = ps.getSupportedDocFlavors();
                for (int i1 = 0; i1 < supportedDocFlavors.length; i1++) {
                    System.out.println(supportedDocFlavors[i1]);
                }
                try {
                    job.print(doc, null);
                    System.out.println("print");
                } catch (PrintException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        }

        static String getZPL(){
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
