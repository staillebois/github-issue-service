package com.st;

import jakarta.enterprise.context.ApplicationScoped;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class IssueService {

    public List<IssueResponse> fetchIssues(IssueRequest request) throws IOException {
        GitHub client = getGitHubClient(request.token);

        GHRepository repo = client.getRepository(request.repository);
        Iterable<GHIssue> issues = repo.queryIssues().state(GHIssueState.OPEN).list().withPageSize(request.size);

        List<IssueResponse> responseList = new java.util.ArrayList<>();

        // Skip to the requested page
        int skip = (request.page - 1) * request.size;
        int current = 0;
        int collected = 0;

        for (GHIssue issue : issues) {
            if (issue.isPullRequest())
                continue;

            if (current < skip) {
                current++;
                continue;
            }

            if (collected >= request.size) {
                break;
            }

            responseList.add(mapToResponse(issue));
            collected++;
            current++;
        }

        return responseList;
    }

    public PageCountResponse getIssuePageCount(IssueRequest request) throws IOException {
        GitHub client = getGitHubClient(request.token);
        GHRepository repo = client.getRepository(request.repository);
        int totalIssues = repo.getOpenIssueCount();
        int pages = (int) Math.ceil((double) totalIssues / request.size);
        return new PageCountResponse(pages, totalIssues);
    }

    private GitHub getGitHubClient(String token) throws IOException {
        if (token != null && !token.isBlank()) {
            return new GitHubBuilder().withOAuthToken(token).build();
        } else {
            return GitHubBuilder.fromEnvironment().build();
        }
    }

    private IssueResponse mapToResponse(GHIssue issue) throws IOException {
        List<CommentResponse> comments = new java.util.ArrayList<>();
        for (GHIssueComment comment : issue.getComments()) {
            comments.add(new CommentResponse(
                    comment.getUser().getLogin(),
                    comment.getBody(),
                    comment.getCreatedAt()));
        }

        return new IssueResponse(
                issue.getTitle(),
                issue.getUser().getLogin(),
                issue.getHtmlUrl().toString(),
                issue.getBody(),
                comments,
                issue.getNumber());
    }
}
