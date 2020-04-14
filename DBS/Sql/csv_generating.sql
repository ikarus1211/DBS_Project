SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/players.csv' INTO TABLE player
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(player_id, player_name, player_password, email, no_characters, premium_points);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/guild.csv' INTO TABLE guild
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(guild_id, guild_name, number_of_players, guild_rank);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/characters.csv' INTO TABLE game_character
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(character_id, character_name, hours_played, character_xp, class, race, game_money, guild_leader, guild_id, player_owner);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/location.csv' INTO TABLE location
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(location_id, location_name, monsters, active_event, event_description, player_count);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/quest.csv' INTO TABLE quest
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(quest_id, quest_name, required_level, reward_money, reward_xp, quest_description, quest_location_id);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/character_quest.csv' INTO TABLE character_quest
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(character_quest_id, questid, char_id);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/item.csv' INTO TABLE item
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(item_id, item_name, item_level, item_type, item_rarity);

SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/character_item.csv' INTO TABLE character_item
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(character_item_id, itemid, cid);