package com.tpd.twitch.item;

import com.tpd.twitch.external.TwitchService;
import com.tpd.twitch.external.model.Clip;
import com.tpd.twitch.external.model.Stream;
import com.tpd.twitch.external.model.Video;
import com.tpd.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ItemService uses the TwitchService API and gets by gameId: videos, clips, streams as a TypeGroupedItemList object.
 */
@Service
public class ItemService {

    private static final int SEARCH_RESULT_SIZE = 20;

    private final TwitchService twitchService;

    public ItemService(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    /**
     * Given a gameId, return TypeGroupedItemList from the Twitch API.
     * @param gameId The gameId of the game, as defined within Twitch.
     * @return
     */
    @Cacheable("items")
    public TypeGroupedItemList getItems(String gameId) {
        List<Video> videos = twitchService.getVideos(gameId, SEARCH_RESULT_SIZE);
        List<Clip> clips = twitchService.getClips(gameId, SEARCH_RESULT_SIZE);
        List<Stream> streams = twitchService.getStreams(List.of(gameId), SEARCH_RESULT_SIZE);
        return new TypeGroupedItemList(gameId, streams, videos, clips);
    }

}