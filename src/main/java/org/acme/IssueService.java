package org.acme;

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
        GitHub client;
        if (request.token != null && !request.token.isBlank()) {
            client = new GitHubBuilder().withOAuthToken(request.token).build();
        } else {
            client = GitHubBuilder.fromEnvironment().build();
        }

        GHRepository repo = client.getRepository(request.repository);
        Iterable<GHIssue> issues = repo.queryIssues().state(GHIssueState.OPEN).list();

        List<IssueResponse> responseList = new java.util.ArrayList<>();
        int fetched = 0;
        for (GHIssue issue : issues) {
            if (fetched >= request.count)
                break;
            if (issue.isPullRequest())
                continue;

            responseList.add(mapToResponse(issue));
            fetched++;
        }

        return responseList;
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
