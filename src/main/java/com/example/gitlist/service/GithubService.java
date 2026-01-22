package com.example.gitlist.service;

import com.example.gitlist.client.GithubClient;
import com.example.gitlist.model.BranchResponse;
import com.example.gitlist.model.RepositoryResponse;
import com.example.gitlist.client.GithubClient.GithubRepository;
import com.example.gitlist.client.GithubClient.GithubBranch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {

    private final GithubClient githubApiClient;

    public GithubService(GithubClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    public List<RepositoryResponse> getRepositories(String username) {
        List<GithubRepository> repositories = githubApiClient.fetchUserRepos(username);
        List<RepositoryResponse> finalRepositories = new ArrayList<>();

        for (GithubRepository repo : repositories) {
            if (!repo.fork()) {
                String owner = repo.owner().login();
                String repoName = repo.name();
                List<BranchResponse> branches = getBranches(owner, repoName);
                finalRepositories.add(new RepositoryResponse(repoName, owner, branches));
            }
        }
        return finalRepositories;
    }

    private List<BranchResponse> getBranches(String owner, String repoName) {
        List<GithubBranch> branches = githubApiClient.fetchRepoBranches(owner, repoName);
        List<BranchResponse> finalBranches = new ArrayList<>();

        for (GithubBranch branch : branches) {
            finalBranches.add(new BranchResponse(branch.name(),branch.commit().sha()));
        }

        return finalBranches;
    }
}