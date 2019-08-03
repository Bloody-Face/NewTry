import java.sql.*;

public class DB {

    public static Connection getConnect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:sqlite:g://my.db");
    }

    public static void myCreate() throws SQLException {
        final String create = "CREATE TABLE if not exists CURRENCY_RATE(date TEXT PRIMARY KEY, value TEXT)";
        Connection co = getConnect();
        Statement statement = co.createStatement();
        statement.execute(create);
        statement.close();
        co.close();
    }
    public static void mySelect() throws SQLException {
        final String select = "SELECT date, value FROM CURRENCY_RATE ORDER BY date";
        Connection co = getConnect();
        Statement stmt = co.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        while(rs.next()){
            String myDate = rs.getString("Date");
            String myValue = rs.getString("Value");
            System.err.println(myDate+"\t|\t"+myValue);
        }
        rs.close();
        stmt.close();
        co.close();


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
