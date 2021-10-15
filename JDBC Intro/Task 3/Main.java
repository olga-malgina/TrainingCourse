package com.company;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main {

    static final String FILE_WITH_NAMES = "target/classes/com/company/names.txt";
    static final String FILE_WITH_SURNAMES = "target/classes/com/company/surnames.txt";
    static final String FILE_WITH_TEXT = "target/classes/com/company/Lethal-White.txt";

    public static final String URL = "jdbc:mysql://localhost:3306/task_3?rewriteBatchedStatements=true";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "password";

    public static final String START_TIMESTAMP = "2007-01-01 00:00:00";
    public static final String END_TIMESTAMP =  "2026-01-01 00:00:00";

    public static final String CREATE_USERS_TABLE = "CREATE TABLE USERS (" +
            "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY," +
            "name VARCHAR(50) NOT NULL," +
            "surname VARCHAR(50) NOT NULL," +
            "birthdate DATE NOT NULL)";

    public static final String INSERT_USER = "INSERT INTO USERS " +
            "(name, surname, birthdate) VALUES (?, ?, ?)";

    public static final String CREATE_FRIENDSHIPS = "CREATE TABLE FRIENDSHIPS (" +
            "userid1 INT NOT NULL," +
            "userid2 INT NOT NULL," +
            "timestamp TIMESTAMP)";

    public static final String INSERT_FRIENDSHIP = "INSERT INTO FRIENDSHIPS " +
            "(userid1, userid2, timestamp) VALUES (?, ?, ?)";

    public static final String CREATE_POSTS_TABLE = "CREATE TABLE POSTS (" +
            "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY," +
            "userid INT NOT NULL," +
            "text VARCHAR(1000)," +
            "timestamp TIMESTAMP NOT NULL)";

    public static final String INSERT_POST = "INSERT INTO POSTS " +
            "(userid, text, timestamp) VALUES (?, ?, ?)";

    public static final String CREATE_LIKES_TABLE = "CREATE TABLE LIKES (" +
            "postid INT NOT NULL," +
            "userid INT NOT NULL," +
            "timestamp TIMESTAMP NOT NULL)";

    public static final String INSERT_LIKE = "INSERT INTO LIKES " +
            "(postid, userid, timestamp) VALUES (?, ?, ?)";

    public static final String SELECT_ALL_USERS = "SELECT * FROM USERS";

    public static final String SELECT_ALL_POSTS = "SELECT * FROM POSTS";



    static List<String> parseValues(String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));
        List<String> values = new ArrayList<>();
        while(scanner.hasNext()) {
            values.add(scanner.next().trim());
        }
        return values;
    }

    // TODO: turn into one method with the previous one - NOT POSSIBLE
    static List<String> parseText(String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));
//        scanner.useDelimiter("(?<=[.!?])\\\\s*");
        List<String> lines = new ArrayList<>();
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine().trim());
        }
        List<String> sentences = new ArrayList<>();
        for (String l : lines) {
            if (!l.isEmpty()) {
                String[] parsedLines = l.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s");
                sentences.addAll(Arrays.asList(parsedLines));
            }
        }
        return sentences;
    }

    static LocalDate generateBirthdate() {
        long minDay = LocalDate.of(1965, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2006, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }


    static List<User> createUsersFromFiles() throws IOException {
        List<String> names = parseValues(FILE_WITH_NAMES);
        List<String> surnames = parseValues(FILE_WITH_SURNAMES);

        List<User> users = new ArrayList<>();
        if (names.size() == surnames.size()) {
            for (int i = 0; i < names.size(); ++i) {
                User user = new User(names.get(i), surnames.get(i), generateBirthdate());
                users.add(user);
            }
        }
        return users;
    }

    static Timestamp generateTimestamp() {
        long offset = Timestamp.valueOf(START_TIMESTAMP).getTime();
        long end = Timestamp.valueOf(END_TIMESTAMP).getTime();
        long diff = end - offset + 1;
        return new Timestamp(offset + (long) (Math.random() * diff));

    }

    public static void main(String[] args) throws IOException {
        List<User> users = createUsersFromFiles();
//        users.forEach(System.out::println);


        try {
            Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            // Create USERS table in schema
            Statement createUsers = connection.createStatement();
            createUsers.execute(CREATE_USERS_TABLE);
            System.out.println("Table USERS successfully created");

            // Insert users generated from files
            for (User user : users) {
                PreparedStatement insertUser = connection.prepareStatement(INSERT_USER);
                insertUser.setString(1, user.getName());
                insertUser.setString(2, user.getSurname());
                insertUser.setDate(3, Date.valueOf(user.getBirthdate()));
                insertUser.executeUpdate();
            }

            // Create Friendships table
            Statement createFriendships = connection.createStatement();
            createFriendships.execute(CREATE_FRIENDSHIPS);
            System.out.println("Table FRIENDSHIPS successfully created");

            // Generate data for Friendships
            // First fetch all the ids of the users from database
            List<Integer> userIds = new ArrayList<>();
            Statement selectUsers = connection.createStatement();
            ResultSet usersSet = selectUsers.executeQuery(SELECT_ALL_USERS);
            while (usersSet.next()) {
                userIds.add(usersSet.getInt("id"));
            }
            System.out.println(userIds.size());

            // Generate a list of Friendships - for each user id randomly select random number of user ids
            // and randomly generate timestamp of the date and time of becoming friends
            List<Friendship> friendships = new ArrayList<>();

            for (int i = 0; i < userIds.size(); ++i) {
                int numberOfFriends = ThreadLocalRandom.current().nextInt(0, 2000);
                // Creating a list of user ids to choose from
                // Need to skip those for which friendships have already been created
                List<Integer> copyOfIds = userIds.stream().skip(i).collect(Collectors.toList());
                for (int j = 0; j < numberOfFriends; ++j) {
                    // Choose random id from the list of potential friends
                    if (copyOfIds.size() <= 1) {
                    } else {
                        int friendNumber = ThreadLocalRandom.current().nextInt(0, copyOfIds.size() - 1);
                        // Create a friendship with randomly generated timestamp and add it to the list
                        friendships.add(new Friendship(userIds.get(i), copyOfIds.get(friendNumber), generateTimestamp()));
                    }
                }
            }

            System.out.println(friendships.size());

            for (int i = 0; i < 5; ++i) {
                System.out.println(friendships.get(i));
            }

            // This is a batch statement, empirically found out that it works better than single statement insertion
            // Also added rewriteBatchedStatements=true to the url for the DB connection - also helper to insert 3.8 mln records
            PreparedStatement insertFriendship = connection.prepareStatement(INSERT_FRIENDSHIP);
            for (Friendship f : friendships) {
                insertFriendship.setInt(1, f.getUserid1());
                insertFriendship.setInt(2, f.getUserid2());
                insertFriendship.setTimestamp(3, f.getTimestamp());
                insertFriendship.executeUpdate();
            }

            // Create Posts table
            Statement createPosts = connection.createStatement();
            createPosts.execute(CREATE_POSTS_TABLE);
            System.out.println("Table POSTS successfully created");

            // Parse text from file to sentences for the posts

            List<String> sentences = parseText(FILE_WITH_TEXT);
//            System.out.println(sentences.size());

            // Create a list of posts to upload to database
            // Generate a random (0 - 100) number of posts for a given user
            // Choose randomly a sentence from a pool of sentences
            // Generate a random timestamp for a post
            List<Post> posts = new ArrayList<>();
            for (Integer id : userIds) {
                int numberOfPosts = ThreadLocalRandom.current().nextInt(1, 50);
                for (int i = 0; i < numberOfPosts; ++i) {
                    int postNumber = ThreadLocalRandom.current().nextInt(0, sentences.size());
                    Timestamp timestamp = generateTimestamp();
                    posts.add(new Post(id, sentences.get(postNumber), timestamp));
                }
            }

//            System.out.println(posts.size());

            // Upload generated posts to the database
            PreparedStatement insertPosts = connection.prepareStatement(INSERT_POST);
            for (Post p : posts) {
                insertPosts.setInt(1, p.getUserid());
                insertPosts.setString(2, p.getText());
                insertPosts.setTimestamp(3, p.getTimestamp());
                insertPosts.executeUpdate();
            }

            // Generate likes for posts
            List<Like> likes = new ArrayList<>();

            // Get a list of posts with ids from database
            List<Post> postsList = new ArrayList<>();
            Statement selectPosts = connection.createStatement();
            ResultSet postsSet = selectPosts.executeQuery(SELECT_ALL_POSTS);
            while (postsSet.next()) {
                postsList.add(new Post(postsSet.getInt("id"), postsSet.getInt("userid")));
            }

            for (Post p : postsList) {
                // setting the limit of 50 likes per post so as not to crash my computer
                int numberOfLikes = ThreadLocalRandom.current().nextInt(1, 50);
                // create o copy of userid list and remove the author of the post
                List<Integer> ids = new ArrayList<>(userIds);
                ids.remove(Integer.valueOf(p.getUserid()));
                for (int i = 0; i < numberOfLikes; ++i) {
                    int id = ThreadLocalRandom.current().nextInt(0, ids.size());
                    Timestamp timestamp = generateTimestamp();
                    likes.add(new Like(p.getId(), ids.get(id), timestamp));
                    ids.remove(id);
                }
            }
//            System.out.println(likes.size());

            // Create LIKES table
            Statement createLikes = connection.createStatement();
            createLikes.execute(CREATE_LIKES_TABLE);
            System.out.println("Table LIKES successfully created");

            // Upload generated likes to the database
            PreparedStatement insertLikes = connection.prepareStatement(INSERT_LIKE);
            for (Like l : likes) {
                insertLikes.setInt(1, l.getPostid());
                insertLikes.setInt(2, l.getUserid());
                insertLikes.setTimestamp(3, l.getTimestamp());
                insertLikes.executeUpdate();
            }

            // Get the names and surnames of the users with more than 1000 posts and 20 likes between Jan 20 and Mar 25
            // Conditions are different from the task due to the distribution of data I got as a result of random generations
            String REPORT = "SELECT name, surname\n" +
                    "FROM USERS\n" +
                    "WHERE id IN (\n" +
                    "SELECT userid1 FROM FRIENDSHIPS\n" +
                    "WHERE userid1 IN (\n" +
                    "SELECT userid FROM POSTS\n" +
                    "WHERE id IN \n" +
                    "(SELECT postid\n" +
                    "FROM LIKES\n" +
                    "WHERE timestamp >= '2020-01-01' AND timestamp <= '2025-03-31'\n" +
                    "GROUP BY postid\n" +
                    "HAVING COUNT(*) >= 20)\n" +
                    ")\n" +
                    "GROUP BY userid1\n" +
                    "HAVING COUNT(*) >= 1000)";

            Statement generateReport = connection.createStatement();
            ResultSet names = generateReport.executeQuery(REPORT);
            while (names.next()) {
                System.out.println(names.getString("name") + " " + names.getString("surname"));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
