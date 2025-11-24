package org.acme;

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
            @ToolArg(description = "The number of issues to fetch") int count) {

        IssueRequest request = new IssueRequest();
        request.repository = repository;
        request.count = count;

        try {
            java.util.List<IssueResponse> issues = issueService.fetchIssues(request);
            return objectMapper.writeValueAsString(issues);
        } catch (IOException e) {
            return "Error fetching issues: " + e.getMessage();
        }
    }
}
