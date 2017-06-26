package util;

import java.sql.*;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection("jdbc:ucanaccess://highscores.accdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null)
            instance = new DBConnection();
        return instance;
    }

    public synchronized ResultSet query(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        statement.close();
        return rs;
    }

    public synchronized void update(String update) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(update);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized void close() throws SQLException {
        connection.close();
        instance = null;
    }

    public static void main(String[] args) throws Exception {
        try {
            ResultSet rs = DBConnection.getInstance().query("SELECT * FROM Highscores ORDER BY Score DESC LIMIT 10;");
            System.out.printf("%16s %8s %10s%n", "Nickname", "Score", "Datum");
            while (rs.next())
                System.out.printf("%16s %8d %10s%n", rs.getString(1), rs.getInt(2), rs.getDate(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.getInstance().close();
    }
}