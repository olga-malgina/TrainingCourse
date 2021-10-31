package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBCopyTool {

    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String USERNAME = "root";
    private final String PASSWORD = "password";

    // Name for a copy of the database
    private final String COPY_DATABASE_NAME = "copyDB";

    // Connection to create a database
    private Connection connection;
    private Connection connectionToSource;
    private Connection connectionToCopy;

    private final String originalDatabaseName;

    private final String order;
    private final String DIRECT_ORDER = "direct";
    private final String REVERSE_ORDER = "reverse";


    public static final Logger log = LogManager.getRootLogger();

    public DBCopyTool (String originalDatabaseName, String order) {
        this.originalDatabaseName = originalDatabaseName;
        this.order = order;
        try {
            this.connection = getConnection();
            dropCopyDatabase();
            createCopyDatabase();

            this.connectionToSource = getConnectionToDatabase(originalDatabaseName);
            this.connectionToCopy = getConnectionToDatabase(COPY_DATABASE_NAME);

            connectionToSource.setAutoCommit(false);
            connectionToCopy.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection to the server not tied to the schema/database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Establish connection to a specific database
    private Connection getConnectionToDatabase(String name) throws SQLException {
        return DriverManager.getConnection(URL + name + "?rewriteBatchedStatements=true&useCursorFetch=true", USERNAME, PASSWORD);
    }

    // Drop an instance of "copyDB" if it already exists
    private void dropCopyDatabase() {
        try (Statement st = connection.createStatement()) {
            String dropDatabaseQuery = "DROP DATABASE IF EXISTS " + COPY_DATABASE_NAME;
            st.executeUpdate(dropDatabaseQuery);
            log.info("Database " + COPY_DATABASE_NAME + " successfully dropped");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a database to copy all the data to
    private void createCopyDatabase() {
        try (Statement st = connection.createStatement()) {
            String dropDatabaseQuery = "CREATE DATABASE " + COPY_DATABASE_NAME;
            st.executeUpdate(dropDatabaseQuery);
            log.info("Database " + COPY_DATABASE_NAME + " successfully created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all table names from the original database (already in lexicographical order)
    private List<String> getTablesNames() {
        List<String> tablesNames = new ArrayList<>();

        try {
            DatabaseMetaData dbMetaData = connectionToSource.getMetaData();
            ResultSet tables = dbMetaData.getTables(originalDatabaseName, originalDatabaseName, null, new String[]{"TABLE"});
            while (tables.next()) {
                tablesNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablesNames;
    }

    // Gets the tables and columns of the source database and creates their copies in a copy database
    private void createTables() {
        try {

            DatabaseMetaData dbMetaData = connectionToSource.getMetaData();
            ResultSet tables = dbMetaData.getTables(originalDatabaseName, originalDatabaseName, null, new String[]{"TABLE"});
            tables.setFetchSize(100);

            while (tables.next()) {

                Statement createTableStatement = connectionToCopy.createStatement();
                String tableName = tables.getString("TABLE_NAME");
                StringBuilder createTableQueryBuilder = new StringBuilder("CREATE TABLE ");
                createTableQueryBuilder.append(tableName).append(" (");

                List<String> columnNames = new ArrayList<>();
                List<String> columnTypes = new ArrayList<>();

                ResultSet columns = dbMetaData.getColumns(originalDatabaseName, originalDatabaseName, tableName, null);
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    if (columnType.equals("VARCHAR")) {
                        columnType += "(" + columns.getString("COLUMN_SIZE") + ")";
                    }

                    columnNames.add(columnName);
                    columnTypes.add(columnType);
                }

                int columnsNumber = columnNames.size();

                for (int i = 0; i < columnsNumber; ++i) {
                    createTableQueryBuilder.append(columnNames.get(i)).append(" ").append(columnTypes.get(i));

                    if (i != columnsNumber - 1) {
                        createTableQueryBuilder.append(", ");
                    } else {
                        createTableQueryBuilder.append(")");
                    }
                }

                String createTableQuery = createTableQueryBuilder.toString();
                createTableStatement.execute(createTableQuery);
            }
            connectionToSource.commit();
            connectionToCopy.commit();
            log.info("Tables successfully copied");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to prepare insert queries for copied tables
    private String generateInsertQuery(String table, int numberOfColumns) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(table).append(" VALUES (");
        for (int i = 0; i < numberOfColumns; ++i) {
            if (i != numberOfColumns - 1) {
                queryBuilder.append("?, ");
            } else {
                queryBuilder.append("?)");
            }
        }

        return queryBuilder.toString();
    }



    private void copyData(List<String> tables) {
        log.info("Starting to copy data");
        int batchSize = 1000;

        for (String table : tables) {
            String selectQuery = "SELECT * FROM " + table;

            try (Statement statement = connectionToSource.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);) {

                statement.setFetchSize(1000);

                ResultSet data = statement.executeQuery(selectQuery);

                ResultSetMetaData metaData = data.getMetaData();
                int numberOfColumns = metaData.getColumnCount();

                String insertQuery = generateInsertQuery(table, numberOfColumns);

                if (order.equals(DIRECT_ORDER)) {
                    copyDataDirectOrder(data, numberOfColumns, insertQuery, batchSize);
                } else if (order.equals(REVERSE_ORDER)) {
                    copyDataReverseOrder(data, numberOfColumns, insertQuery, batchSize);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            log.info("Data successfully copied");
        }
    }

    private void copyDataDirectOrder(ResultSet rs, int numberOfColumns, String insertQuery, int batchSize) throws SQLException {
        int counter = 0;

        try (PreparedStatement insertStatement = connectionToCopy.prepareStatement(insertQuery)) {
            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; ++i) {
                    insertStatement.setObject(i, rs.getObject(i));
                }

                insertStatement.addBatch();
                // committing results of the inserts in batches
                if (++counter % batchSize == 0) {
                    insertStatement.executeBatch();
                    connectionToCopy.commit();
                }
            }
            insertStatement.executeBatch();
            connectionToCopy.commit();
        }
    }

    private void copyDataReverseOrder(ResultSet rs, int numberOfColumns, String insertQuery, int batchSize) throws SQLException {
        int counter = 0;
        rs.afterLast();

        try (PreparedStatement insertStatement = connectionToCopy.prepareStatement(insertQuery)) {
            while (rs.previous()) {
                for (int i = 1; i <= numberOfColumns; ++i) {
                    insertStatement.setObject(i, rs.getObject(i));
                }

                insertStatement.addBatch();
                if (++counter % batchSize == 0) {
                    insertStatement.executeBatch();
                    connectionToCopy.commit();
                }
            }
            insertStatement.executeBatch();
            connectionToCopy.commit();
        }
    }

    // Upper level method to copy database
    public void copyDatabase() {

        createTables();
        List<String> tablesNames = getTablesNames();
        copyData(tablesNames);

    }
}
