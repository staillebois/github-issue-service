package com.st;

import java.util.List;

public class IssueResponse {
    public String title;
    public String author;
    public String url;
    public String description;
    public List<CommentResponse> comments;
    public int number;

    public IssueResponse(String title, String author, String url, String description, List<CommentResponse> comments,
            int number) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.description = description;
        this.comments = comments;
        this.number = number;
    }
}
