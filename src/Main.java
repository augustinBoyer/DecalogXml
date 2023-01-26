import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import com.opencsv.CSVWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.xpath.XPathExpressionException;
import static com.opencsv.ICSVParser.DEFAULT_ESCAPE_CHARACTER;
import static com.opencsv.ICSVParser.DEFAULT_QUOTE_CHARACTER;
import static com.opencsv.ICSVWriter.DEFAULT_LINE_END;

public class Main {

    static List<String> items;

    static List<String> files;
    static int filesIndex;
    static int indexSchema;
    static ArrayList<String> schs;
    static int test;
    static int index;
    static ArrayList<String> keys = new ArrayList<>();
    static Hashtable<String, String> desc;
    static ArrayList<Hashtable<String, Hashtable<String,String>>> allList = new ArrayList<>();

    static Datas datas;

     static final String SEPARATOR = " ### ";

    static String name = "C:\\Users\\Augustin\\Downloads\\reversibilite_muma_20221215\\reversibilite_muma_20221215\\entities\\JocondeItem\\";

    static int docSize;


    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException, InvalidFormatException {
        File folder = new File("C:\\Users\\Augustin\\IdeaProjects\\XmlToXls\\Test");
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {

                f.delete();

            }
        }
        test = 0;
        filesIndex = 0;
        datas = new Datas();
        items = new ArrayList<>();

        continueAfter();
    }

    public static void ifContinue() throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException, InvalidFormatException {
        keys = new ArrayList<>();
        desc = new Hashtable<>();
        allList = new ArrayList<>();
        test++;
        if(test < indexSchema && filesIndex < files.size()){
            continueAfter();
        }

    }




    public static void continueAfter() throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException, InvalidFormatException {

        try {
            if(schs != null && schs.get(test).equals("indexation_skm")){
                System.out.println();
            }
        File file = new File(name);

        files = Arrays.stream(file.list()).toList();

        Document document = Parse.ParseXml(name + files.get(filesIndex));
        if(document != null){
            NodeList nList = document.getElementsByTagName("document");

            Element el = (Element) nList.item(0);
            NodeList nl = el.getElementsByTagName("schema");

            ArrayList<String> docs = new ArrayList<>();
            schs = new ArrayList<>();

            docSize = 0;
            for (int i = 0; i < nl.getLength(); i++) {
                docSize++;
                Element sch = (Element) nl.item(i);

                for (int j = 0; j < nList.getLength(); j++) {

                    Element doc = (Element) nList.item(j);

                    if(i ==0){
                        docs.add(doc.getAttribute("id"));
                    }


                }
                schs.add(sch.getAttribute("name"));
            }

            for (int i = 0; i < docs.size(); i++) {

                Hashtable<String, Hashtable<String, String>> hashs0 = new Hashtable<>();
                Hashtable<String, String> arr0 = new Hashtable<>();

                NodeList nd1 = ((Element) nList.item(i)).getElementsByTagName("schema").item(test).getChildNodes();
                indexSchema = ((Element) nList.item(i)).getElementsByTagName("schema").getLength();

                index = 0;
                for(int j = 0; j < nd1.getLength(); j++){

                        if (nd1.item(j) != null) {

                            if (nd1.item(j).getNodeName() == "item") {
                                findItems(arr0, nd1.item(j).getChildNodes());
                            }
                            else if (!HasElement(nd1.item(j)) && nd1.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element elem = (Element) nd1.item(j);
                                arr0.put(elem.getNodeName(), elem.getTextContent());

                                if (!keys.contains(elem.getNodeName())) {
                                    keys.add(index, elem.getNodeName());
                                    index++;
                                }
                            } else if (nd1.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element elem = (Element) nd1.item(j);

                                iterate(arr0, elem);
                            }
                        }

                    if(j == nd1.getLength() - 1){
                        hashs0.put(docs.get(i), arr0);

                        allList.add(hashs0);

                    }
                }
            }

            merge();
            CreateExcel();
        }else{
            ifContinue();
        }


        }catch (FileNotFoundException e) {
            e.printStackTrace();
            Parse.getError(e);
        } catch (IOException e) {
            e.printStackTrace();
            Parse.getError(e);
        } catch (XPathExpressionException e) {
            Parse.getError(e);
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            Parse.getError(e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            Parse.getError(e);
            throw new RuntimeException(e);
        }finally{
            ifContinue();
        }
    }

    public static void findItems(Hashtable<String, String> hash, NodeList v)
    {
        Element ele = (Element)v;

        for (int i = 0; i < ele.getChildNodes().getLength(); i++)
        {
            if(v.item(i).getNodeName() == "item")
            {
                String s = "";
                Element el = (Element)v.item(i);
                NodeList nod = el.getChildNodes();
                for(int j = 0; j < nod.getLength();j++){

                    if(nod.item(j).getTextContent() != ""){
                        //if(datas.ContainsKey(s) != "") {
                            s += datas.ContainsKey(s);
                            s += SEPARATOR;
                        //}
                    }

                }
                var data = v.item(i).getParentNode().getNodeName() + SEPARATOR + v.item(i).getNodeName();


                if (!hash.containsKey(data))
                {
                    hash.put(data, s);
                }

                if (!keys.contains(data))
                {
                    keys.add(data);
                }

                findItems(hash, v.item(i).getChildNodes());
            }
            else
            {
                if(schs.get(test).equals("indexation_skm") && docSize == 38){
                    System.out.println();
                }

                if (v.item(i).hasChildNodes()) {
                    //String s = "";
                    Element el = (Element)v.item(i);
                    NodeList nod = el.getChildNodes();

                    var data0 = v.item(i).getParentNode().getParentNode().getNodeName() + SEPARATOR + v.item(i).getParentNode().getNodeName();
                    String str = "";

                    if(hash.get(data0) != null){
                        str = hash.get(data0);
                    }

                    if(hash.get(data0) == null){
                        str += "[";
                    }else if(v.item(i).getPreviousSibling() != null && v.item(i).getPreviousSibling().getPreviousSibling() == null){
                        str += "], [";
                    }


                    for(int j = 0; j < nod.getLength();j++){
                            //s = datas.ContainsKey(nod.item(j).getTextContent());
                        //}

                        String s = datas.ContainsKey(nod.item(j).getTextContent());
                         str +=  SEPARATOR + SEPARATOR + v.item(i).getNodeName() + " : " + s +  SEPARATOR + SEPARATOR;

                    }

                    if(!hash.containsKey(data0)){
                        hash.put(data0, str);
                    }else{
                        //String t = hash.get(data0) + str;
                        hash.remove(data0);
                        hash.put(data0, str);
                        System.out.println();
                    }

                    if (!keys.contains(data0))
                    {
                        keys.add(data0);
                    }
                }
            }

        }
    }

    public static void iterate(Hashtable<String, String> hash, Node node){
        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node currentode = list.item(i);

            if (currentode.getNodeName() == "item" && currentode.hasChildNodes()) {

                findItems(hash, currentode.getChildNodes());
            } else {
                if (!currentode.hasChildNodes()) {
                    if(currentode.getNodeType() == Node.ELEMENT_NODE){

                        var data = currentode.getParentNode().getNodeName() + SEPARATOR + currentode.getNodeName();

                        String s = currentode.getTextContent();

                        String str = "";

                        if(datas.ContainsKey(s) != ""){
                            str =datas.ContainsKey(s) + SEPARATOR + hash.get(data);
                        }


                        if (!hash.contains(data)) {
                            hash.put(data, str);
                        }

                        if (!keys.contains(data)) {
                            keys.add(data);
                        }
                    }

                }else{


                    String s = "";
                    Element el = (Element)list.item(i);
                    NodeList nod = el.getChildNodes();
                    for(int j = 0; j < nod.getLength();j++){

                        if(nod.item(j).getTextContent() != ""){

                            String str = datas.ContainsKey(nod.item(j).getTextContent());
                            if(str != ""){
                                s += str + SEPARATOR;
                            }


                            if(j == nod.getLength() - 1 && s.length() > 4){
                                s = s.substring(0, s.length() - 4);
                            }
                        }


                    }

                    var data = list.item(i).getParentNode().getNodeName() + SEPARATOR + list.item(i).getNodeName();

                    hash.put(data, s);

                    if (!keys.contains(data))
                    {
                        keys.add(data);
                    }
                }
            }
        }
    }

    public static void merge(){

        for(int i = 0; i < allList.size();i++){
            Hashtable<String, Hashtable<String, String>> hash = allList.get(i);

            Enumeration ks = hash.keys();

            while(ks.hasMoreElements()){

                String ks0 = ks.nextElement().toString();
                Enumeration kss = allList.get(i).get(ks0).keys();
                int k = 0;
                while (kss.hasMoreElements()){

                    String kss0 = kss.nextElement().toString();
                    //String s = allList.get(i).get(ks0).get().toString();

                    if(!keys.contains(kss0)){
                        keys.add(k, kss0);
                        k++;
                    }

                }

            }
        }
    }

    public static boolean HasElement(Node element) {
        NodeList list = element.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {

                return true;
            } else if (list.item(i).hasChildNodes()) {
                return HasElement(list.item(i));
            }
        }
        return false;
    }

    public static void CreateExcel() throws IOException, InvalidFormatException, XPathExpressionException, ParserConfigurationException, SAXException {

        if(Parse.indexError > 0){
            PrintWriter writer = new PrintWriter("error.txt" + Parse.indexError, "UTF-8");

            writer.println(Parse.errors);
            writer.close();
        }

        String n = "C:\\Users\\Augustin\\IdeaProjects\\XmlToXls\\Test" + "\\" + schs.get(test) + ".csv";

        Files.createDirectories(Paths.get("C:\\Users\\Augustin\\IdeaProjects\\XmlToXls\\Test"));

        if(schs.get(test).equals("indexation_skm")){
            System.out.println();
        }

        FileWriter fw = new FileWriter(n, true);


            CSVWriter writer = new CSVWriter(fw, ';', DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER,  DEFAULT_LINE_END);


            String [] startFile = new String[keys.size() + 2];
            for (int j = 0; j < allList.size(); j++) {
                Enumeration ks = allList.get(j).keys();

                if(j == 0){

                    startFile[0] = "";
                    for(int k = 0; k < keys.size(); k++){
                        startFile[k+1] = keys.get(k);
                        items.add(keys.get(k));
                    }
                }
                while(ks.hasMoreElements()){
                    String ks0 = ks.nextElement().toString();

                    String[] str = new String[keys.size() + 2];
                    str[0] = ks0;
                    for(int k = 0; k < keys.size(); k++){
                        String kss0 = allList.get(j).get(ks0).get(keys.get(k));

                        if(kss0 != null && kss0.length() > 0 && kss0.charAt(0) == '['){
                            kss0 += "]";
                        }

                            str[k + 1] = kss0;



                        if(k == keys.size() - 1){
                            String end = '\n' + "";
                            str[keys.size() + 1] = end;
                            if(j == 0 && filesIndex == 0){
                                writer.writeNext(startFile);
                            }

                                writer.writeNext(str);



                            if(j == allList.size() - 1 ){

                                try{
                                    writer.close();
                                    if(test < indexSchema && filesIndex < files.size()){
                                        ifContinue();
                                    }

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Parse.getError(e);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Parse.getError(e);
                                } catch (XPathExpressionException e) {
                                    Parse.getError(e);
                                    throw new RuntimeException(e);
                                } catch (ParserConfigurationException e) {
                                    Parse.getError(e);
                                    throw new RuntimeException(e);
                                } catch (SAXException e) {
                                    Parse.getError(e);
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                }
            }

            filesIndex++;


            if(test < indexSchema || filesIndex < files.size()){
                test = -1;
                ifContinue();
            }
    }
}