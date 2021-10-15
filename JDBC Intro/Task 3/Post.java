package com.company;

import java.sql.Timestamp;

public class Post {

    private int id;
    private int userid;
    private String text;
    private Timestamp timestamp;

    public Post(int userid, String text, Timestamp timestamp) {
        this.userid = userid;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Post(int id, int userid, String text, Timestamp timestamp) {
        this.id = id;
        this.userid = userid;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Post(int id, int userid) {
        this.id = id;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
