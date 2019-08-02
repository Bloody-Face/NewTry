import org.w3c.dom.Document;
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

public class Test1 {
    //private static Connection connection;
    public static Connection getConnect(){
        try{
            return DriverManager.getConnection("jdbc:sqlite:g://my.db");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        for(;;) {

            myInsert();
            mySelect();
            Thread.sleep(12*60*60*1000);
        }

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
    static void myInsert(){
        final String sql = "INSERT OR IGNORE INTO CURRENCY_RATE (date, value) VALUES (?, ?)";
        String[] info = MyFun("USD");
        final String insert = "INSERT OR IGNORE INTO CURRENCY_RATE (date, value) VALUES ('"+info[0]+"', '"+ info[1] + "')";
        try (   Connection co = getConnect();
                //PreparedStatement stmt = co.prepareStatement(sql);
                Statement stmt = co.createStatement();
         )
        {
            stmt.executeUpdate(insert);
            //System.err.println(info[0]+"  "+info[1]);
            //stmt.setObject(1, info[0]);
            //stmt.setObject(2, info[1]);
            System.err.println("Complete");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    static String[] MyFun(String CharCode){
        String[] res = {"Date","-", CharCode};
        try {
            URL connect = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(connect.openStream());
            res[0] = document.getDocumentElement().getAttribute("Date");
            Node root = document.getDocumentElement();
            NodeList lisps = root.getChildNodes();
            for (int i = 0; i < lisps.getLength(); i++) {
                Node lisp = lisps.item(i);
                if (lisp.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = lisp.getChildNodes();
                    for(int j = 0; j < bookProps.getLength(); j++) {
                        if(bookProps.item(j).getNodeName() == "CharCode" && CharCode.equals(bookProps.item(j).getChildNodes().item(0).getTextContent())){
                            for(int z = 0; z <bookProps.getLength(); z++)
                                if(bookProps.item(z).getNodeName() == "Value"){
                                    res[1] = bookProps.item(z).getChildNodes().item(0).getTextContent();
                                    return res;
                            }
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return res;
    }
}
