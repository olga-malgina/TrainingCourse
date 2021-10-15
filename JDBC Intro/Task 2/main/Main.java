package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:3306/task_3?rewriteBatchedStatements=true";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "password";

    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);

            StorageProcedureCreator proceduresCreator = new StorageProcedureCreator(connection);

            // First - delete existing procedures
            proceduresCreator.deleteStoredProcedures();

            // Second - create stored procedures from Java code
            proceduresCreator.createProcedures();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
