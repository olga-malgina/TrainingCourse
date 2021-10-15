package com.company;

import java.sql.Timestamp;

public class Friendship {

    private int userid1;
    private int userid2;
    private Timestamp timestamp;

    public Friendship(int userid1, int userid2, Timestamp timestamp) {
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.timestamp = timestamp;
    }

    public int getUserid1() {
        return userid1;
    }

    public void setUserid1(int userid1) {
        this.userid1 = userid1;
    }

    public int getUserid2() {
        return userid2;
    }

    public void setUserid2(int userid2) {
        this.userid2 = userid2;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return userid1 + " " + userid2 + " " + timestamp.toString();
    }
}
