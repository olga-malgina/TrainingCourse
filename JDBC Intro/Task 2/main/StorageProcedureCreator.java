package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageProcedureCreator {

    private Connection connection;
    private String createUser = "INSERT INTO USERS\n" +
            "    (name, surname, birthdate) VALUES (?, ?, ?);";
    private String addFriend = "INSERT INTO FRIENDSHIPS\n" +
            "    (userid1, userid2, timestamp) VALUES (?, ?, ?);";
    private String addPost = "INSERT INTO POSTS\n" +
            "    (userid, text, timestamp) VALUES (?, ?, ?);";
    private String addLike = "INSERT INTO LIKES\n" +
            "    (postid, userid, timestamp) VALUES (?, ?, ?);";
    private String editPost = "UPDATE POSTS SET text = ? WHERE id = ?;";
    private String changeUserName = "UPDATE USERS SET surname = ? WHERE id = ?;";
    private String deleteFriendship = "DELETE FROM FRIENDSHIPS WHERE userid1 = ? AND userid2 = ?;";
    private String getUsersWithNumberOfFriends = "SELECT name, surname\n" +
            "\tFROM USERS\n" +
            "\tWHERE id IN (\n" +
            "\tSELECT userid1 FROM FRIENDSHIPS\n" +
            "\tGROUP BY userid1\n" +
            "\tHAVING COUNT(*) >= ?\n" +
            "\t);";


    private String createUserProcedure = "CREATE PROCEDURE createUser(IN userName VARCHAR(50), userSurname VARCHAR(50)," +
            " userBirthdate DATE) " +
            "BEGIN " +
            "INSERT INTO USERS (name, surname, birthdate) VALUES (userName, userSurname, userBirthdate);" +
            "END";

    private String createFriendshipProcedure = "CREATE PROCEDURE addFriend(IN friendId1 INT, friendId2 INT, date TIMESTAMP)" +
            "BEGIN " +
            "INSERT INTO FRIENDSHIPS (userid1, userid2, timestamp) VALUES (friendId1, friendId2, date);" +
            "END";

    private String createPostProcedure = "CREATE PROCEDURE addPost(IN userId INT, postText VARCHAR(1000), date TIMESTAMP)" +
            "BEGIN " +
            "INSERT INTO POSTS (userid, text, timestamp) VALUES (userId, postText, date);" +
            " END";

    private String createLikeProcedure = "CREATE PROCEDURE addLike(IN postId INT, userId INT, date TIMESTAMP)" +
            "BEGIN " +
            "INSERT INTO LIKES (postid, userid, timestamp) VALUES (postId, userId, date);" +
            " END";

    private String updatePostProcedure = "CREATE PROCEDURE editPost(IN newText VARCHAR(1000), postId INT)" +
            "BEGIN " +
            "UPDATE POSTS SET text = newText WHERE id = postId;" +
            " END";

    private String updateUserProcedure = "CREATE PROCEDURE changeUserSurname(IN newSurname VARCHAR(50), userId INT)" +
            "BEGIN " +
            "UPDATE USERS SET surname = newSurname WHERE id = userId;" +
            " END";

    private String deleteFriendshipProcedure = "CREATE PROCEDURE deleteFriendship(IN userId1 INT, userId2 INT)" +
            "BEGIN " +
            "DELETE FROM FRIENDSHIPS WHERE userid1 = userId1 AND userid2 = userId2;" +
            " END";

    private String getUsersWithNumberOfFriendsProcedure = "CREATE PROCEDURE getUsersWithNumberOfFriends(IN numberOfFriends INT)" +
            "BEGIN " +
            "SELECT name, surname\n" +
            "\tFROM USERS\n" +
            "\tWHERE id IN (\n" +
            "\tSELECT userid1 FROM FRIENDSHIPS\n" +
            "\tGROUP BY userid1\n" +
            "\tHAVING COUNT(*) >= numberOfFriends\n" +
            "\t);" +
            " END";

    private String getStoredProcedureNames = "SELECT SPECIFIC_NAME\n" +
            "FROM information_schema.routines \n" +
            "WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = 'task_3'";

    private String dropProcedure = "DROP PROCEDURE IF EXISTS task_3.";


    public StorageProcedureCreator(Connection connection) {
        this.connection = connection;
    }

    public void createUser(String name, String surname, LocalDate birthdate) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(createUser);
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setDate(3, Date.valueOf(birthdate));
        statement.executeUpdate();
    }

    public void createFriendship(int userId1, int userId2, Timestamp timestamp) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(addFriend);
        statement.setInt(1, userId1);
        statement.setInt(2, userId2);
        statement.setTimestamp(3, timestamp);
        statement.executeUpdate();
    }

    public void createPost(int userId, String text, Timestamp timestamp) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(addPost);
        statement.setInt(1, userId);
        statement.setString(2, text);
        statement.setTimestamp(3, timestamp);
        statement.executeUpdate();
    }

    public void createLike(int postId, int userId, Timestamp timestamp) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(addLike);
        statement.setInt(1, postId);
        statement.setInt(2, userId);
        statement.setTimestamp(3, timestamp);
        statement.executeUpdate();
    }

    public void updatePost(String text, int postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(editPost);
        statement.setString(1, text);
        statement.setInt(2, postId);
        statement.executeUpdate();
    }

    public void updateUser(String surname, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(changeUserName);
        statement.setString(1, surname);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void deleteFriendship(int userId1, int userId2) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(deleteFriendship);
        statement.setInt(1, userId1);
        statement.setInt(2, userId2);
        statement.executeUpdate();
    }

    public void selectUsersWithNFriends(int numberOfFriends, List<User> users) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(getUsersWithNumberOfFriends);
        statement.setInt(1, numberOfFriends);
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            while (result.next()) {
                users.add(new User(result.getInt("id"), result.getString("name"),
                        result.getString("surname"), result.getDate("birthdate")));
            }
        }
    }

    public List<String> getListOfProceduresNames() throws SQLException {
        List<String> procedures = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(getStoredProcedureNames);
        if (result.isBeforeFirst()) {
            while (result.next()) {
                procedures.add(result.getString("SPECIFIC_NAME"));
            }
        }
        statement.close();
        System.out.println("Printing the list of stored procedures:");
        for (String p : procedures) {
            System.out.println(p);
        }
        return procedures;
    }

    public void deleteStoredProcedures() throws SQLException {
        // Get a list of stored procedures names
        List<String> procedures = getListOfProceduresNames();

        // Delete all the relevant stored procedures in a loop
        if (procedures.size() != 0) {
            for (String p : procedures) {
                System.out.println("Execute DROP PROCEDURE " + p);
                Statement st = connection.createStatement();
                st.execute(dropProcedure + p);
                st.close();
            }
        }

    }

    public void createProcedures() throws SQLException {
        Statement stCreateUserProcedure = connection.createStatement();
        stCreateUserProcedure.execute(createUserProcedure);
        stCreateUserProcedure.close();

        Statement stCreateFriendshipProcedure = connection.createStatement();
        stCreateFriendshipProcedure.execute(createFriendshipProcedure);
        stCreateFriendshipProcedure.close();

        Statement stCreatePostProcedure = connection.createStatement();
        stCreatePostProcedure.execute(createPostProcedure);
        stCreatePostProcedure.close();

        Statement stCreateLikeProcedure = connection.createStatement();
        stCreateLikeProcedure.execute(createLikeProcedure);
        stCreateLikeProcedure.close();

        Statement stUpdateUserProcedure = connection.createStatement();
        stUpdateUserProcedure.execute(updateUserProcedure);
        stUpdateUserProcedure.close();

        Statement stUpdatePostProcedure = connection.createStatement();
        stUpdatePostProcedure.execute(updatePostProcedure);
        stUpdatePostProcedure.close();

        Statement stGetUsersProcedure = connection.createStatement();
        stGetUsersProcedure.execute(getUsersWithNumberOfFriendsProcedure);
        stGetUsersProcedure.close();

        Statement stDeleteFriendshipProcedure = connection.createStatement();
        stDeleteFriendshipProcedure.execute(deleteFriendshipProcedure);
        stDeleteFriendshipProcedure.close();
    }




}
