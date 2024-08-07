// This is an interface that can be used to operate on the database. 
// We don't need to implement the actual execution details inside it because we have extended a parent interface.
package com.tpd.twitch.db;

import com.tpd.twitch.db.entity.ItemEntity;
import org.springframework.data.repository.ListCrudRepository;
/*
Use Spring Data JDBC to build database functions including standard retrieval methods. */
public interface ItemRepository extends ListCrudRepository<ItemEntity, Long> {
    ItemEntity findByTwitchId(String twitchId);

}
