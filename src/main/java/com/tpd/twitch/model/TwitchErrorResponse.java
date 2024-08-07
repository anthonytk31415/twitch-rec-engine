package com.tpd.twitch.model;

public record TwitchErrorResponse(
        String message,
        String error,
        String details
) {
}

// All exceptions in Spring Boot have default handling, but I don't like it, so I want to handle them myself.

