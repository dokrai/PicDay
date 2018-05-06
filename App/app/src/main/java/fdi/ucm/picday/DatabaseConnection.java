package fdi.ucm.picday;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    Connection conn;

    final String dbName = "picday_db";
    final String url = "jdbc:mysql://localhost/"+dbName;
    final String driver = "com.mysql.jdbc.Driver";
    final String userName = "root";
    final String password = "";

    public DatabaseConnection() {

    }

    public Connection connect_db() {
        conn = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            conn = DriverManager.getConnection(url,userName,password);
            if (!conn.isClosed()) {
                System.out.println("Database is working :)");
            }
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    public void close_connection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {

        }
    }
}
