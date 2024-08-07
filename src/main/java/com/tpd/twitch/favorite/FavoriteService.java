package com.tpd.twitch.favorite;

import com.tpd.twitch.db.FavoriteRecordRepository;
import com.tpd.twitch.db.ItemRepository;
import com.tpd.twitch.db.entity.FavoriteRecordEntity;
import com.tpd.twitch.db.entity.ItemEntity;
import com.tpd.twitch.db.entity.UserEntity;
import com.tpd.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// FavoriteService is called by FavoriteController. FavoriteService is used to set/unset/get favorite items.
@Service
public class FavoriteService {
    // What dependencies does this service need? == What external information does the constructor need?
    // ItemRepository, FavoriteRecordRepository
    private final ItemRepository itemRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;
    public FavoriteService(ItemRepository itemRepository,
                           FavoriteRecordRepository favoriteRecordRepository) {
        this.itemRepository = itemRepository;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    @Transactional
    // This annotation means: when there are multiple operations, if all operations are successful,
    // the transaction will go through; if one of the functions fails, it will roll back to the initial state.
    // It can also be understood as writing first in the cache, then completing and moving to the database.
    // => The actual situation is much more complex
    public void setFavoriteItem(UserEntity user, ItemEntity item) throws DuplicateFavoriteException {
        ItemEntity persistedItem = itemRepository.findByTwitchId(item.twitchId());
        // Use ItemRepository to fetch the twitchId of ItemEntity once.
        // Currently, I don't know if this item has been stored in the database.
        // If it is null, save it, then assign it to persistedItem to ensure it is not null.
        if (persistedItem == null) {
            persistedItem = itemRepository.save(item);
        }

        // If this item has been favorited before, there's no point in favoriting it again.
        // You need to inform the frontend to handle this situation.
        if (favoriteRecordRepository.existsByUserIdAndItemId(user.id(), persistedItem.id())) {
            throw new DuplicateFavoriteException();
        }
        // Most common case - If all conditions are met, then save it.
        FavoriteRecordEntity favoriteRecord = new FavoriteRecordEntity(null, user.id(), persistedItem.id(), Instant.now());
        favoriteRecordRepository.save(favoriteRecord);
    }
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void unsetFavoriteItem(UserEntity user, String twitchId) {
        ItemEntity item = itemRepository.findByTwitchId(twitchId);
        // If it's not null, only delete the record that the user has favorited. Never delete the item because other users might have favorited it.
        if (item != null) {
            favoriteRecordRepository.delete(user.id(), item.id());
        }
        // If there's nothing to delete, you can simply do nothing and proceed. Alternatively, you can throw an exception.
    }

    public List<ItemEntity> getFavoriteItems(UserEntity user) {
        // First, find the items favorited by the user, then return - all clips, videos, and streams are placed in one list.
        List<Long> favoriteItemIds = favoriteRecordRepository.findFavoriteItemIdsByUserId(user.id());
        return itemRepository.findAllById(favoriteItemIds);
    }

    public TypeGroupedItemList getGroupedFavoriteItems(UserEntity user) {
        // Unlike the previous API where all entities are grouped into one list, this API separates them into three types - stream, clip, and video, and sends them to you separately.
        List<ItemEntity> items = getFavoriteItems(user);
        return new TypeGroupedItemList(items);
    }
}