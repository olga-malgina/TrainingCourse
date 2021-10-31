package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class Main {

    public static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {

        Properties properties = new Properties();
        String fileName = args[0];

        try (InputStream input = new FileInputStream(fileName);) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("File not found " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        int numberOfTables = Integer.parseInt(properties.getProperty("numberOfTables"));
        int numberOfInstances = Integer.parseInt(properties.getProperty("numberOfInstances"));
        int numberOfTypes = Integer.parseInt(properties.getProperty("numberOfTypes"));
        int numberOfColumns = Integer.parseInt(properties.getProperty("numberOfColumns"));

        // Parse int array of number of rows for each table
        String rows = properties.getProperty("numberOfRows");
        int[] numberOfRows = Stream.of(rows.split(",")).mapToInt(Integer::parseInt).toArray();

        // Calculate number of tables to be created in each instance of Table Creator
        // Number of tables to create by all instances except for the last
        int commonNumber = numberOfTables / numberOfInstances;
        // The remainder to be added to the number of tables to be created by the last instance of tableCreator
        int remainder = numberOfTables % numberOfInstances;

        // Create a list of arrays with the number of rows for tables for each instance
        List<int[]> rowsByInstance = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < numberOfInstances; ++i) {
            if (i != numberOfInstances - 1) {
                rowsByInstance.add(Arrays.copyOfRange(numberOfRows, counter, counter + commonNumber));
                counter += commonNumber;
            } else {
                rowsByInstance.add(Arrays.copyOfRange(numberOfRows, counter, counter + commonNumber + remainder));
            }
        }

        try {
            for (int i = 0; i < numberOfInstances; ++i) {
                TableCreator creator;
                if (i != numberOfInstances - 1) {
                    creator = new TableCreator(commonNumber, numberOfColumns, numberOfTypes, rowsByInstance.get(i));
                } else {
                    creator = new TableCreator(commonNumber + remainder, numberOfColumns, numberOfTypes, rowsByInstance.get(i));
                }
                creator.launchTablesCreation();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
