/*Use init to create the required tables.*/

DROP TABLE IF EXISTS favorite_records;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;
/* when running the code, if table previously exists, delete the table before running the code */

CREATE TABLE users
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,    /*Basically, this component can also use UUID. The id is the primary component and cannot be omitted; it auto-increments starting from 1, so you don't need to manually assemble it.*/
    username VARCHAR(50) NOT NULL UNIQUE,   /* Can username be used as a primary key? Yes, but it’s not convenient because changing the username would require updating all related data. */
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(100) NOT NULL,
    enabled  TINYINT      NOT NULL DEFAULT 1
);

CREATE TABLE authorities /*Only this table links to username; others link to id. Why? Because it complies with the requirements of the official schema for implementing Spring Security.*/
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE items
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    twitch_id VARCHAR(255) UNIQUE NOT NULL,   /*Why not use the Twitch ID as the primary key? It’s unlikely to change, but using an external ID for setting relationships provides less control. Additionally, before the database is populated, the Twitch ID is not null while the database ID is null; this helps differentiate between stored and non-stored entries.*/
    title TEXT,   /*TEXT is used for very long content, such as in a description with lengthy content. For shorter content, like an order name, you can use VARCHAR.*/
    url VARCHAR(255),
    thumbnail_url VARCHAR(255),
    broadcaster_name VARCHAR(255),
    game_id VARCHAR(255), /*The difference between game_id and twitch_id: game_id is used in itemEntity to uniquely identify a game within your system. twitch_id refers to IDs from Twitch, such as clip IDs or video IDs. These IDs are related to specific games, so you need a game_id to link these Twitch IDs to a particular game.*/
    type VARCHAR(255)
);

CREATE TABLE favorite_records   /*What is a foreign key used for? Without it, the database can still operate, but a foreign key ensures referential integrity by linking records in one table to records in another table. It helps maintain consistent and accurate relationships between tables.*/
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,      /*All IDs that the user likes*/
    item_id INT NOT NULL,      /*All users who like this item*/
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, /*When was the like given?*/
    /*In the schema above, there’s no operation to understand how the favorite_record table relates to the items and user tables. How do you specify that the favorite_record table is connected to the other tables? Use a foreign key.*/
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,  /*You can't just use user_id—it must be an ID that exists in the user table. You can also add constraints, such as ON DELETE CASCADE. If the user ID doesn't exist and isn’t properly connected, it will result in an error. However, once properly set up, you can use cascading actions to automatically handle related records.*/
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    UNIQUE KEY unique_item_and_user_combo (item_id, user_id)  /*Preventing redundant information: What is the use of defining this name? If an error occurs, it will indicate that the name violates constraints, helping you to debug the issue.*/
);

