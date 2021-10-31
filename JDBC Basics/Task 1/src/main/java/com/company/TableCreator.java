package com.company;

import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TableCreator extends Connectable {

    private final int TABLE_NAME_LENGTH = 10;
    private final int COLUMN_NAME_LENGTH = 4;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static final String START_TIMESTAMP = "1753-01-01 00:00:00";
    public static final String END_TIMESTAMP =  "9999-12-31 00:00:00";

    static SecureRandom rnd = new SecureRandom();

    static final String[] TYPES =  {"BIT", "TINYINT", "BOOL", "SMALLINT", "MEDIUMINT", "INT",
                                    "BIGINT", "FLOAT", "DOUBLE", "DATE",
                                    "DATETIME", "CHAR", "VARCHAR(50)", "TEXT"};

//    static final String[] TYPES =  {"INT",
//            "BIGINT", "FLOAT", "DOUBLE", "TEXT"};

    private int numberOfTables;
    private int numberOfColumns;
    private int numberOfTypes;
    private int[] numberOfRows;
    private Connection connection;
    private List<String> types;
    // To keep a list of tables created by this instance
    private List<String> tableNames;

    public TableCreator(int numberOfTables, int numberOfColumns, int numberOfTypes, int[] numberOfRows) throws SQLException {

        this.connection = getConnection();
        this.numberOfTables = numberOfTables;
        this.numberOfColumns = numberOfColumns;
        this.numberOfTypes = numberOfTypes;
        this.numberOfRows = Arrays.copyOf(numberOfRows, numberOfRows.length);

        this.types = getAvailableTypes();
        this.tableNames = new ArrayList<>();

    }

    // Helper method to generate the list of data types to choose from
    private List<String> getAvailableTypes() {
        List<String> fullList = new ArrayList<>(Arrays.asList(TYPES));
        List<String> types = new ArrayList<>();
        for (int i = 0; i < numberOfTypes; ++i) {
            int n = ThreadLocalRandom.current().nextInt(0, fullList.size());
            types.add(fullList.get(n));
            fullList.remove(n);
        }
        return types;
    }

    // Helper method to generate random names for tables and columns
    // Parameter - expected length of the name. Constants for table and column name lengths will be passed
    private String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            builder.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return builder.toString();
    }

    // Helper method to generate random date (from the year 1753 till the end of 9999)
    private Date generateRandomDate() {
        long minDay = LocalDate.of(1753, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(9999, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return Date.valueOf(LocalDate.ofEpochDay(randomDay));
    }

    // Helper method to generate random timestamp
    static Timestamp generateTimestamp() {
        long offset = Timestamp.valueOf(START_TIMESTAMP).getTime();
        long end = Timestamp.valueOf(END_TIMESTAMP).getTime();
        long diff = end - offset + 1;
        return new Timestamp(offset + (long) (Math.random() * diff));

    }

    private String generateCreateTableQuery() {

        // Generate name for the table
        String tableName = generateRandomString(TABLE_NAME_LENGTH);
        tableNames.add(tableName);

        // Generate the query which includes generating name for the column and choosing its type
        StringBuilder builder = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (int i = 0; i < numberOfColumns; ++i) {
            String columnName = generateRandomString(COLUMN_NAME_LENGTH);
            String type = types.get(ThreadLocalRandom.current().nextInt(0, types.size()));
            builder.append(columnName).append(" ").append(type);
            if (i != numberOfColumns - 1) {
                builder.append(", ");
            } else {
                builder.append(")");
            }
        }
        return builder.toString();
    }

    // Returns a list of names of newly created tables
    public void createTables() {
        try (Statement st = connection.createStatement()) {
            for (int i = 0; i < numberOfTables; ++i) {
                String query = generateCreateTableQuery();
                System.out.println(query);
                st.addBatch(query);
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To populate tables with data:
    // 1. Get list of tables - their names will be needed for INSERT queries
    // 2. For each table get the list of columns - their types are enough
    // 3. For each row generate random values of required type (helper method needed)
    // 4. Execute statements

    // Helper method to get a map from Table Name - List of column Data types
    private Map<String, List<String>> getListOfTables() {
        Map<String, List<String>> mapOfMetadata = new HashMap<>();
        System.out.println("Number of tables generated: " + tableNames.size());

        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();

            for (String tableName : tableNames) {
                ResultSet columns = dbMetaData.getColumns(null, null, tableName, null);
                List<String> columnsTypes = new ArrayList<>();

                while (columns.next()) {
                    String type = columns.getString("TYPE_NAME");
                    columnsTypes.add(type);
                }
                mapOfMetadata.putIfAbsent(tableName, columnsTypes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mapOfMetadata;
    }

    // Method to populate tables with rows of random data
    public void generateRows(Map<String, List<String>> mapOfTables) {
        int counter = 0;
        for (String table : mapOfTables.keySet()) {

            // First we need to prepare SQL query - with the correct table name and number of values
            StringBuilder insertQuery = new StringBuilder("INSERT INTO ");
            insertQuery.append(table);
            insertQuery.append(" VALUES (");

            // Getting the list of column types for future use
            List<String> types = mapOfTables.getOrDefault(table, new ArrayList<>());

            // Adding as many parameters to the query as there are columns in the table
            int numberOfColumns = types.size();
            for (int i = 0; i < numberOfColumns; ++i) {
                if (i != numberOfColumns - 1) {
                    insertQuery.append("?, ");
                } else {
                    insertQuery.append("?)");
                }
            }

            String query = insertQuery.toString();

            // Get number of rows for current table
            int rows = numberOfRows[counter];
            ++counter;

            try (PreparedStatement st = connection.prepareStatement(query)) {
                for (int i = 0; i < rows; ++i) {
                    for (int j = 0; j < numberOfColumns; ++j) {
                        String type = types.get(j);
                        switch (type) {
                            case "BIT": case "TINYINT": case "BOOL":
                                st.setBoolean(j + 1, ThreadLocalRandom.current().nextBoolean());
                                break;
                            case "SMALLINT":
                                st.setInt(j + 1, ThreadLocalRandom.current().nextInt(0, 128));
                                break;
                            case "MEDIUMINT":
                                st.setInt(j + 1, ThreadLocalRandom.current().nextInt(0, 65534));
                                break;
                            case "INT": case "BIGINT":
                                st.setInt(j + 1, ThreadLocalRandom.current().nextInt());
                                break;
                            case "FLOAT":
                                st.setFloat(j + 1, ThreadLocalRandom.current().nextFloat());
                                break;
                            case "DOUBLE":
                                st.setDouble(j + 1, ThreadLocalRandom.current().nextDouble());
                                break;
                            case "DATE":
                                st.setDate(j + 1, generateRandomDate());
                                break;
                            case "DATETIME":
                                st.setTimestamp(j + 1, generateTimestamp());
                                break;
                            case "CHAR":
                                st.setString(j + 1, generateRandomString(1));
                                break;
                            case "VARCHAR":
                                st.setString(j + 1, generateRandomString(30));
                                break;
                            case "TEXT":
                                st.setString(j + 1, generateRandomString(50));
                                break;
                        }
                    }
                    st.addBatch();
                }
                st.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void launchTablesCreation() {
        createTables();
        Map<String, List<String>> listOfTables = getListOfTables();
        generateRows(listOfTables);
    }
}
