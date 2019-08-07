import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

public class Parser {
    public static class Item {
        private String date;
        private String charCode;
        private String value;

        // Пустой конструктор
        private Item() {
        }
        // Конструктор для инициализации всех полей
        public Item(String date, String value,String charCode) {
            this.date = date;
            this.charCode = charCode;
            this.value = value;
        }
        public void setDate(String date){
            this.date = date;
        }
        public void setCharCode(String charCode){
            this.charCode = charCode;
        }
        public void setValue(String value){
            this.value = value;
        }
        public String getDate(){
            return this.date;
        }
        public String getCharCode(){
           return this.charCode;
        }
        public String getValue(){
            return this.value;
        }
    }
    static Element getChildElement(Element root, String childName) {
        NodeList children = root.getElementsByTagName(childName);
        return (Element) children.item(0);
    }
    public static Item getCurrencyExchange(String charCode) {
        Item res = new Item();
        res.setCharCode(charCode);
        try {
            URL connect = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(connect.openStream());
            res.setDate( document.getDocumentElement().getAttribute("Date"));

            NodeList items = document.getElementsByTagName("Valute");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String charCodeValue = getChildElement(item, "CharCode").getTextContent();
                if (charCode.equals(charCodeValue)) {
                    res.setValue(getChildElement(item, "Value").getTextContent());
                    return res;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


}
