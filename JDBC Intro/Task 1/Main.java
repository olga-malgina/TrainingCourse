package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:3306/task_1";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "password";

    public static void main(String[] args) throws SQLException {

        String request = "SELECT * FROM plants WHERE difficulty = 1";
        String parametrizedRequest = "SELECT * FROM plants WHERE difficulty = ?";

        Logger log = LogManager.getRootLogger();

        // for simple request without parameters
        MyFirstConnection connection = new MyFirstConnection(URL, USER_NAME, PASSWORD, request);
        connection.executeStatement();

        // for parametrized request
        MyFirstConnection paramConnection = new MyFirstConnection(URL, USER_NAME, PASSWORD, parametrizedRequest);
        paramConnection.executePreparedStatement(1);

        paramConnection.printTables();

    }
}
