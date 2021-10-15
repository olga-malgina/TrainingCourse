package com.company;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MyFirstConnection {

    private String url;
    private String userName;
    private String password;
    private String request;
    private Connection connection;

    public MyFirstConnection(String url, String userName, String password, String request) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.request = request;
        try {
            this.connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    public void executeStatement() {
        Logger log = LogManager.getRootLogger();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(request);
            while (result.next()) {
                System.out.println(result.getRow() + " " + result.getString(2) + " " + result.getInt(3));
            }
            log.info("Simple request executed");
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    public void executePreparedStatement(int parameter) {
        Logger log = LogManager.getRootLogger();

        try (PreparedStatement st = connection.prepareStatement(request)) {
            st.setInt(1, parameter);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                System.out.println(result.getRow() + " " + result.getString(2) + " " + result.getInt(3));
            }
            log.info("Parametrized request executed");
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    public void printTables() {
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables("task_1", null, "%", new String[]{"TABLE"});
            System.out.println("Tables in Database for Task 1:");
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                System.out.println("Table " + tableName);
                ResultSet columns = meta.getColumns(null, null, tableName, null);
                while (columns.next()) {
                    String name = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    int size = columns.getInt("COLUMN_SIZE");

                    System.out.println("Column name: [" + name + "]; type: [" + type
                            + "]; size: [" + size + "]");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
