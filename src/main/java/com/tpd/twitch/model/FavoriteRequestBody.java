package com.tpd.twitch.model;

import com.tpd.twitch.db.entity.ItemEntity;

/**
 * Used to define favorites.
 * @param favorite
 */
public record FavoriteRequestBody(
        ItemEntity favorite
) {
}
