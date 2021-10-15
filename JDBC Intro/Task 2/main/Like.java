package com.company;

import java.sql.Timestamp;

public class Like {

    private int postid;
    private int userid;
    private Timestamp timestamp;

    public Like(int postid, int userid, Timestamp timestamp) {
        this.postid = postid;
        this.userid = userid;
        this.timestamp = timestamp;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
