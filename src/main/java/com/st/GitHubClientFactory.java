package com.st;

import jakarta.enterprise.context.ApplicationScoped;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

@ApplicationScoped
public class GitHubClientFactory {

    public GitHub createClient(String token) throws IOException {
        if (token != null && !token.isBlank()) {
            return new GitHubBuilder().withOAuthToken(token).build();
        } else {
            return GitHubBuilder.fromEnvironment().build();
        }
    }
}
