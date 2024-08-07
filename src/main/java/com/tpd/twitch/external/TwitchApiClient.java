package com.tpd.twitch.external;

import com.tpd.twitch.external.model.ClipResponse;
import com.tpd.twitch.external.model.GameResponse;
import com.tpd.twitch.external.model.StreamResponse;
import com.tpd.twitch.external.model.VideoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
Use the TwitchApiClient to communicate with the Twitch API using the FeignClient, a convenient way to
write and standardize API calls with Spring Boot to the Twitch API and whatever other external APIs you use.
*/

@FeignClient(name = "twitch-api")
// This annotation is used to define a Feign client interface. Feign is a declarative web service client that simplifies
// making HTTP requests to external APIs. The name attribute specifies the logical name of the Feign client, this name is
// created from application.yml configuration section and needs to be consistent with that to pull the info.
public interface TwitchApiClient {

    /*This method is a declaration for making a GET request to the "/games" endpoint of the Twitch API.
    It expects a String parameter named name, which is sent as a query parameter in the request.
    The return type seems to be a class named GameResponse.*/
    @GetMapping("/games")   // These annotations are used to specify the HTTP methods and endpoints for the methods declared in the interface. They correspond to the respective API endpoints provided by the Twitch API.
    GameResponse getGames(@RequestParam("name") String name);

    @GetMapping("/games/top")
    GameResponse getTopGames();

    @GetMapping("/videos/")
    VideoResponse getVideos(@RequestParam("game_id") String gameId, @RequestParam("first") int first);

    @GetMapping("/clips/")
    ClipResponse getClips(@RequestParam("game_id") String gameId, @RequestParam("first") int first);

    /* This method is a declaration for making a GET request to the "/streams/" endpoint of the Twitch API.
    It expects a List<String> parameter named gameIds, sent as a query parameter named "game_id," and an int parameter
    named first, sent as a query parameter named "first." The return type is probably StreamResponse.*/
    @GetMapping("/streams/")
    StreamResponse getStreams(@RequestParam("game_id") List<String> gameIds, @RequestParam("first") int first);

}
