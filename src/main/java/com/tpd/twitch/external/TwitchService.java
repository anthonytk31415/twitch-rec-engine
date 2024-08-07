package com.tpd.twitch.external;

import com.tpd.twitch.external.model.Clip;
import com.tpd.twitch.external.model.Game;
import com.tpd.twitch.external.model.Stream;
import com.tpd.twitch.external.model.Video;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// Why do we need Twitch Service? Why can't it be done in the Twitch API client? Twitch API logic
// is one-to-one mapping. We can do additional logic in the Twitch service, such as caching.
// The main purpose of this TwitchService class is to provide a higher-level abstraction for interacting with the Twitch API by utilizing the Feign client (TwitchApiClient).
// Additionally, caching has been added to improve performance by storing and reusing the results of certain API calls.

/**
 * TwitchService is called to do two things:
 * (1) Call the TwitchApiClient to make the API calls to get information like games, streams, videos, and clips, etc.
 * (2) Create a cache for various calls.
 */

@Service          //dependency injection
public class TwitchService {

    private final TwitchApiClient twitchApiClient;

    public TwitchService(TwitchApiClient twitchApiClient) {
        this.twitchApiClient = twitchApiClient;
    }
    @Cacheable("top_games")
    public List<Game> getTopGames() {
        return twitchApiClient.getTopGames().data();
    }
    @Cacheable("games_by_name")
    public List<Game> getGames(String name) {
        return twitchApiClient.getGames(name).data();
    }

    public List<Stream> getStreams(List<String> gameIds, int first) {
        return twitchApiClient.getStreams(gameIds, first).data();
    }

    public List<Video> getVideos(String gameId, int first) {
        return twitchApiClient.getVideos(gameId, first).data();
    }

    public List<Clip> getClips(String gameId, int first) {
        return twitchApiClient.getClips(gameId, first).data();
    }

    public List<String> getTopGameIds() {
        List<String> topGameIds = new ArrayList<>();
        for (Game game : getTopGames()) {
            topGameIds.add(game.id());
        }
        return topGameIds;
    }
}
