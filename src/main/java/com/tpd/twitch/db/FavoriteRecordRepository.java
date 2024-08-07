package com.tpd.twitch.db;

import com.tpd.twitch.db.entity.FavoriteRecordEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;


/**
 * The input tells which table content to connect to and what the primary key is.
 */
public interface FavoriteRecordRepository extends ListCrudRepository<FavoriteRecordEntity, Long> {

    /**
     * Return list of records that are favorited by the user
     * @param userId
     * @return
     */
    List<FavoriteRecordEntity> findAllByUserId(Long userId);

    /**
     * Returns whether a specific user has favorited this item.
     * @param userId
     * @param itemId
     * @return
     */
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    @Query("SELECT item_id FROM favorite_records WHERE user_id = :userId")  //
    List<Long> findFavoriteItemIdsByUserId(Long userId);

    /**
     * Use this if you need to delete multiple input settings, you must use this.
     * The ones created manually are read-only, while others are for writing operations. If changes are needed in the manually created API, @Modifying must be added.
     */
    @Modifying
    @Query("DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId")
    void delete(Long userId, Long itemId);

    /*

    The differences between the three different input commands are as follows:
        fetch: This command retrieves data from a specified source or database.
        store: This command saves or stores data into a specified destination or database.
        update: This command modifies or updates existing data in a specified database.
    Each command serves a distinct purpose in managing data: fetching retrieves data, storing saves data, and updating modifies existing data.

    * DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId --》 If a user decides to stop favoriting this item, it needs to be deleted.
    * DELETE FROM favorite_records WHERE item_id = :itemId  --》 All items previously favorited by any user are now gone.
    * DELETE FROM favorite_records WHERE user_id = :userId --》 All items that belong to a specific user have been deleted.
    * */
}
