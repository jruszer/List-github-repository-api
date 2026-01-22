package com.example.gitlist.model;

public record ErrorResponse(
    int status,
    String message
) {}