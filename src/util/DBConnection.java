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
            Logger.log(e, Logger.WARNING);
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
}
