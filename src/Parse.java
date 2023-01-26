import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Parse {

    static String errors;

    static int indexError;

    public  static Document ParseXml(String name) throws ParserConfigurationException, IOException, SAXException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(name);
            document.getDocumentElement().normalize();

            return document;
        }catch(ParserConfigurationException exc){
            System.out.println(exc.getMessage());
            getError(exc);
            return null;
        }catch(SAXException exc){
            System.out.println(exc.getMessage());
            getError(exc);
            return null;
        }catch(IOException ex){
            System.out.println(ex.getMessage());
            getError(ex);
            return null;
        }

    }

    public static void getError(Exception ex){
        errors += ex.getMessage() + '\n';
        indexError++;
    }
}
