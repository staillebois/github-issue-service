package com.st;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.inject.Inject;
import java.io.IOException;

public class IssueTool {

    @Inject
    IssueService issueService;

    @Inject
    com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Tool(description = "Fetch issues from a GitHub repository and return them as JSON")
    public String getIssues(
            @ToolArg(description = "The GitHub repository in 'owner/repo' format") String repository,
            @ToolArg(description = "The page number to fetch (default 1)") int page,
            @ToolArg(description = "The number of issues per page (default 10)") int size) {

        IssueRequest request = new IssueRequest();
        request.repository = repository;
        request.page = page;
        request.size = size;

        try {
            java.util.List<IssueResponse> issues = issueService.fetchIssues(request);
            return objectMapper.writeValueAsString(issues);
        } catch (IOException e) {
            return "Error fetching issues: " + e.getMessage();
        }
    }

    @Tool(description = "Get the total number of pages of issues for a repository")
    public String getIssuePageCount(
            @ToolArg(description = "The GitHub repository in 'owner/repo' format") String repository,
            @ToolArg(description = "The number of issues per page (default 10)") int size) {

        IssueRequest request = new IssueRequest();
        request.repository = repository;
        request.size = size;

        try {
            PageCountResponse response = issueService.getIssuePageCount(request);
            return objectMapper.writeValueAsString(response);
        } catch (IOException e) {
            return "Error fetching page count: " + e.getMessage();
        }
    }
}
