package com.st;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class IssueServiceTest {

    @Inject
    IssueService issueService;

    @InjectMock
    GitHubClientFactory gitHubClientFactory;

    @Test
    public void testGetIssuePageCount() throws IOException {
        GitHub gitHub = mock(GitHub.class);
        GHRepository repo = mock(GHRepository.class);

        when(gitHubClientFactory.createClient(anyString())).thenReturn(gitHub);
        when(gitHub.getRepository(anyString())).thenReturn(repo);
        when(repo.getOpenIssueCount()).thenReturn(115);

        IssueRequest request = new IssueRequest();
        request.repository = "org/repo";
        request.size = 10;
        request.token = "token";

        PageCountResponse response = issueService.getIssuePageCount(request);

        assertEquals(12, response.pages);
        assertEquals(115, response.totalIssues);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFetchIssues() throws IOException {
        GitHub gitHub = mock(GitHub.class);
        GHRepository repo = mock(GHRepository.class);
        GHIssueQueryBuilder.ForRepository forRepo = mock(GHIssueQueryBuilder.ForRepository.class);
        PagedIterable<GHIssue> pagedIterable = mock(PagedIterable.class);
        PagedIterator<GHIssue> pagedIterator = mock(PagedIterator.class);

        when(gitHubClientFactory.createClient(anyString())).thenReturn(gitHub);
        when(gitHub.getRepository(anyString())).thenReturn(repo);
        when(repo.queryIssues()).thenReturn(forRepo);
        when(forRepo.state(GHIssueState.OPEN)).thenReturn(forRepo);
        when(forRepo.list()).thenReturn(pagedIterable);
        when(pagedIterable.withPageSize(anyInt())).thenReturn(pagedIterable);
        when(pagedIterable.iterator()).thenReturn(pagedIterator);

        GHIssue issue = mock(GHIssue.class);
        GHUser user = mock(GHUser.class);
        when(issue.getTitle()).thenReturn("Test Issue");
        when(issue.getUser()).thenReturn(user);
        when(user.getLogin()).thenReturn("user");
        when(issue.getHtmlUrl()).thenReturn(java.net.URI.create("http://github.com/org/repo/issues/1").toURL());
        when(issue.getBody()).thenReturn("Body");
        when(issue.getNumber()).thenReturn(1);
        when(issue.getComments()).thenReturn(Collections.emptyList());

        when(pagedIterator.hasNext()).thenReturn(true, false);
        when(pagedIterator.next()).thenReturn(issue);

        IssueRequest request = new IssueRequest();
        request.repository = "org/repo";
        request.page = 1;
        request.size = 10;
        request.token = "token";

        List<IssueResponse> responses = issueService.fetchIssues(request);

        assertEquals(1, responses.size());
        assertEquals("Test Issue", responses.get(0).title);
    }
}
