package com.tpd.twitch.model;
import com.fasterxml.jackson.annotation.JsonProperty;


// Spring uses an embedded library to help us with conversion. As long as what you return is a class, it can convert the data into key-value pairs in JSON format to pass dynamic data to the frontend.
// Additional knowledge: How about the frontend passing data to the backend? It uses query parameters. You add a question mark after the URL, then pass the required parameters using @RequestParam, making it more dynamic.
public record RegisterBody(
        String username,
        String password,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName
) {
}