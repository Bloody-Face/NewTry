import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class Test1 {

    public static void main(String[] args) throws InterruptedException, SQLException {
        while (true) {
        BD myBD = new BD();
        String[] info = getCurrencyExchange("USD");
        myBD.myInsert(info[0],info[1]);
        myBD.mySelect();
        TimeUnit.HOURS.sleep(12);
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

class BD{

    public static Connection getConnect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:sqlite:g://my.db");
    }

    static void myCreate(){
        final String create = "CREATE TABLE if not exists CURRENCY_RATE(date TEXT PRIMARY KEY, value TEXT)";
        try(  Connection co = getConnect();
              Statement statement = co.createStatement();
        ){
            statement.execute(create);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

    }
    static void mySelect(){
        final String select = "SELECT date, value FROM CURRENCY_RATE ORDER BY date";
        try (   Connection co = getConnect();
                Statement stmt = co.createStatement();
                ResultSet rs = stmt.executeQuery(select);)
        {
            while(rs.next()){
                String myDate = rs.getString("Date");
                String myValue = rs.getString("Value");
                System.err.println(myDate+"\t|\t"+myValue);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void myInsert(Object date, Object value) throws SQLException {
        final String sql = "INSERT OR IGNORE INTO CURRENCY_RATE (date, value) VALUES (?, ?)";

        try (
                Connection c = getConnect();
                PreparedStatement stmt = c.prepareStatement(sql)
        ) {
            stmt.setObject(1, date);
            stmt.setObject(2, value);
            stmt.execute();
            //System.err.println("Complete: " + date + "\t" + value);
        }
    }

}
