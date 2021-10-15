package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageProcedureCreatorTest {

    private final String URL = "jdbc:mysql://localhost:3306/task_3?rewriteBatchedStatements=true";
    private final String USER_NAME = "root";
    private final String PASSWORD = "password";

    @org.junit.jupiter.api.Test
    void deleteStoredProcedures() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            StorageProcedureCreator proceduresCreator = new StorageProcedureCreator(connection);
            proceduresCreator.deleteStoredProcedures();
            List<String> procedures = proceduresCreator.getListOfProceduresNames();
            assertEquals(0, procedures.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void createProcedures() {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            StorageProcedureCreator proceduresCreator = new StorageProcedureCreator(connection);
            // First delete all the procedures, then create them from Java code
            proceduresCreator.deleteStoredProcedures();
            proceduresCreator.createProcedures();
            List<String> procedures = proceduresCreator.getListOfProceduresNames();
            assertTrue(procedures.size() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}