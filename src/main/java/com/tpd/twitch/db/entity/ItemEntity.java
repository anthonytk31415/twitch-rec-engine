package com.tpd.twitch.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tpd.twitch.external.model.Clip;
import com.tpd.twitch.external.model.Stream;
import com.tpd.twitch.external.model.Video;
import com.tpd.twitch.model.ItemType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * ItemEntity stores game item properties for a specific stream.
 * Write the item to the table, otherwise when running ItemRepository, it won't be possible to connect the data returned by ItemEntity to this record
 * @param id
 * @param twitchId
 * @param title
 * @param url
 * @param thumbnailUrl
 * @param broadcasterName
 * @param gameId
 * @param type
 */
@Table("items")
public record ItemEntity(
        /**
         * When returning from Twitch, we also need the GDBI ID and Twitch ID.
         * @Id = primary key of the table.
         */
        @Id Long id,
        @JsonProperty("twitch_id") String twitchId,
        String title,
        String url,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        @JsonProperty("broadcaster_name") String broadcasterName,
        @JsonProperty("game_id") String gameId,
        @JsonProperty("item_type") ItemType type
) {
    // ItemEntity objects are all created from Video/Clip/Stream class objects. Therefore, no need to create a object with input parameters mentioned above.
    // If I use Video/Clip/Stream object to create ItemEntity is more convenient as this will be the sole case to create this class objects.
    public ItemEntity(String gameId, Video video) {
        this(null, video.id(), video.title(), video.url(), video.thumbnailUrl(), video.userName(), gameId, ItemType.VIDEO);
    }

    public ItemEntity(Clip clip) {
        this(null, clip.id(), clip.title(), clip.url(), clip.thumbnailUrl(), clip.broadcasterName(), clip.gameId(), ItemType.CLIP);
    }

    public ItemEntity(Stream stream) {
        this(null, stream.id(), stream.title(), null, stream.thumbnailUrl(), stream.userName(), stream.gameId(), ItemType.STREAM);
    }

}
