package com.st;

public class PageCountResponse {
    public int pages;
    public int totalIssues;

    public PageCountResponse(int pages, int totalIssues) {
        this.pages = pages;
        this.totalIssues = totalIssues;
    }
}
