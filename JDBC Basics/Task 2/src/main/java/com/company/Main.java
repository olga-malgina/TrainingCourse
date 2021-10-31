package com.company;

public class Main {

    public static void main(String[] args) {

        String sourceDatabaseName = args[0];
        // 2 options available - direct or reverse
        String order = args[1];

        DBCopyTool dbCopyTool = new DBCopyTool(sourceDatabaseName, order);
        dbCopyTool.copyDatabase();
    }
}
