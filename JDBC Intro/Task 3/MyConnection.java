package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private String url;
    private String userName;
    private String password;
    private Connection connection;

    public MyConnection(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        try {
            this.connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
