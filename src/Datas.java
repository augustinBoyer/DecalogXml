import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public class Datas {

    public Hashtable<String, Data> datas;

    public ArrayList<Links> links;


    public String path = "C:\\Users\\Augustin\\Downloads\\reversibilite_muma_20221215\\reversibilite_muma_20221215\\entities\\";

    public Datas() throws ParserConfigurationException, IOException, SAXException {
        datas = new Hashtable<>();

        links = new ArrayList<>();

        add("dc:title", "SKDescriptor");
        add("sKAuthority:principalAppellation", "SKAuthority");
        add("dc:title", "SKContainerModel");
        add("dc:title", "SKEvenement");
        add("dc:title", "SKInputMask");
        add("dc:title", "SKMMuseum");
        add("dc:title", "SKMMuseumDocumentSet");
        add("dc:creator", "SKMPreEntry");
        add("dc:title", "SKPortfolio");
        add("dc:title", "SKReportTemplate");
        add("dc:title", "SKThesaurus");
        add("dc:title", "SKWExportSelection");
        add("dc:title", "SKWExportTemplate");
        add("dc:title", "SKWMultiSourceExport");
        add("dc:title", "SKWStaticStructureElement");

        for(int i = 0; i < links.size(); i++){
            addTitles(links.get(i));
        }
    }

    public void add(String title, String pathOfFile){
        Links autorithy = new Links();
        autorithy.title = title;
        autorithy.path = path + pathOfFile;

        links.add(autorithy);
    }

    public String ContainsKey(String str0){
        String str = str0.trim();


        if(datas.containsKey(str)){

            str = datas.get(str).Texte.trim();
            str = str.replace("  ", "");
            str =  str.replaceAll("(\\r|\\n)", "");
        }else{
            str = str.replace("  ", "");
            str =  str.replaceAll("(\\r|\\n)", "");
        }

        return str;
    }

    public void addTitles(Links gen) throws ParserConfigurationException, IOException, SAXException {
        File dir = new File(gen.path);
        File[] xmlFiles = dir.listFiles();


        for(int i = 0; i <xmlFiles.length; i++){
            if(FilenameUtils.getExtension(xmlFiles[i].getAbsoluteFile().getName()).endsWith("xml")){
                Document document = Parse.ParseXml(xmlFiles[i].getAbsoluteFile().toString());
                NodeList nl = document.getElementsByTagName("document");

                for (int j = 0; j < nl.getLength(); ++j)
                {
                    NamedNodeMap attr = nl.item(j).getAttributes();
                    Element el = (Element) nl.item(j);
                    NodeList no = el.getElementsByTagName(gen.title);
                    if(attr != null){

                        Data da = new Data();
                        da.Data = attr.item(0).getTextContent();
                        da.Id = UUID.randomUUID().toString();
                        da.Texte = no.item(0).getTextContent();

                        datas.put(da.Data, da);

                    }
                }
            }

        }
    }
}
