package com.st;

import java.util.Date;

public class CommentResponse {
    public String author;
    public String body;
    public Date date;

    public CommentResponse(String author, String body, Date date) {
        this.author = author;
        this.body = body;
        this.date = date;
    }
}
