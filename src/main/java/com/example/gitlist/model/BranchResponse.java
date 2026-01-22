package com.example.gitlist.model;

public record BranchResponse(
    String name,
    String lastCommitSha
) {}