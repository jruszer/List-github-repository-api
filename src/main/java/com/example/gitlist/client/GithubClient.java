package com.example.gitlist.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Component
public class GithubClient {

    private final RestClient restClient;

    public GithubClient(@Value("${github.api.url}") String githubUrl) {
        this.restClient = RestClient.builder().baseUrl(githubUrl).defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultHeader("User-Agent", "Gitlist").build();
    }

    public List<GithubRepository> fetchUserRepos(String username) {
        GithubRepository[] results = restClient.get().uri("/users/{username}/repos", username).retrieve().body(GithubRepository[].class);

        if (results != null) {
            return List.of(results);
        } else {
            return new ArrayList<>();
        }
    }


    public List<GithubBranch> fetchRepoBranches(String owner, String repoName) {
        try {
            GithubBranch[] results = restClient.get().uri("/repos/{owner}/{repo}/branches", owner, repoName).retrieve().body(GithubBranch[].class);

            if (results == null) {
                return new ArrayList<>();
            }
            return List.of(results);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public record GithubRepository(String name, GithubUser owner, boolean fork) {}
    public record GithubUser(String login) {}
    public record GithubBranch(String name, GithubCommit commit) {}
    public record GithubCommit(String sha) {}
}