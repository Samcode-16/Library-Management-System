package library.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnection {
    
    private static Connection conn = null;

    
    public static Connection getConnection() {
    if (conn == null) {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("library/config/db.properties")) {
            if (input == null) {
                System.out.println("Unable to find db.properties — using default credentials.");
                return null;
            } else {
                Properties props = new Properties();
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Connected to database successfully!");
                System.out.println();
            }
        } catch (IOException | SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    return conn;
}

    //closing connection at program end
    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            }
            catch(SQLException ignored) {
                conn = null;
            }
        }
    }

}

