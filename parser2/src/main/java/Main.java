import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException{
        while (true) {
            try{
                Parser.Item item = Parser.getCurrencyExchange("USD");
                DB.myInsert(item.getDate(), item.getValue());
                DB.mySelect();
                TimeUnit.HOURS.sleep(12);
            } catch (Exception e) {
                e.printStackTrace();
                TimeUnit.MINUTES.sleep(5);
            }
        }

    }





}

