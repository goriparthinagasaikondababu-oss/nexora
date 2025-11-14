package org.ts.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private static Connection connection;
    private MySQL(){}
    public static Connection getConnection(){
        String url = "jdbc:mysql://localhost:3306/nexora";
        String user = "root";
        String password = "9876543210";
        if(connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection established successfully!");
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
}
