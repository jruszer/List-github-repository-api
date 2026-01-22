package com.example.gitlist.controller;

import com.example.gitlist.model.RepositoryResponse;
import com.example.gitlist.service.GithubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = "/repos/{username}")
    public ResponseEntity<List<RepositoryResponse>> getUserRepositories(@PathVariable String username) {
        List<RepositoryResponse> response = githubService.getRepositories(username);
        return ResponseEntity.ok(response);
    }
}