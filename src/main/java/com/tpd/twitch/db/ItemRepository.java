// 这是可以拿来对DB做操作的interface了。我们不需要在里面implement要去执行的内容，因为我们extend了parent interface
package com.tpd.twitch.db;

import com.tpd.twitch.db.entity.ItemEntity;
import org.springframework.data.repository.ListCrudRepository;
/*
Use Spring Data JDBC to build database functions including standard retrieval methods. */
public interface ItemRepository extends ListCrudRepository<ItemEntity, Long> {
    ItemEntity findByTwitchId(String twitchId);

}
