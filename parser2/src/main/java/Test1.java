import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Test1 {

    public static void main(String[] args) throws InterruptedException{
        while (true) {
            try{
                String[] info = getCurrencyExchange("USD");
                DB.myInsert(info[0],info[1]);
                DB.mySelect();
                TimeUnit.HOURS.sleep(12);
            } catch (Exception e) {
                e.printStackTrace();
                TimeUnit.MINUTES.sleep(5);
            }
        }

    }


    static Element getChildElement(Element root, String childName) {
        NodeList children = root.getElementsByTagName(childName);
        return (Element) children.item(0);
    }

    static String[] getCurrencyExchange(String charCode) {
        String[] res = {"Date", "-", charCode};
        try {
            URL connect = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(connect.openStream());
            res[0] = document.getDocumentElement().getAttribute("Date");

            NodeList items = document.getElementsByTagName("Valute");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String charCodeValue = getChildElement(item, "CharCode").getTextContent();
                if (charCode.equals(charCodeValue)) {
                    res[1] = getChildElement(item, "Value").getTextContent();
                    return res;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

