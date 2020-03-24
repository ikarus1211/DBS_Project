DROP DATABASE IF EXISTS dbs_projekt;
CREATE DATABASE dbs_projekt charset=utf8;
USE dbs_projekt;

CREATE TABLE dbs_projekt.player (
    player_id int not null AUTO_INCREMENT,
    player_name varchar(20),
    player_password varchar(20),
	email varchar(50),
    no_characters int,
    premium_points int,

    primary key (player_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.game_server (
    server_id int not null AUTO_INCREMENT,
    server_name varchar(20),
    no_players_active int,
    max_players int,
    game_server_status boolean,
    primary key (server_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.player_server (
    pid int NOT NULL, 
    sid int NOT NULL,
    CONSTRAINT fk_player FOREIGN KEY (pid) REFERENCES player (player_id),
    CONSTRAINT fk_server FOREIGN KEY (sid) REFERENCES game_server (server_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.quest (
    quest_id int not null AUTO_INCREMENT,
    required_level int not null,
    reward_money int,
    reward_xp int not null,
    quest_description varchar(1000),
    primary key (quest_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.game_character (
    character_id int not null AUTO_INCREMENT,
    hours_played int,
    character_xp int not null,
    class varchar(20),
    race varchar(20),
    game_money int,
    primary key (character_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.location (
    location_id int not null AUTO_INCREMENT,
    location_name varchar(50),
	monsters varchar(500),
    active_event boolean,
    event_description varchar(1000),
    player_count int,
    primary key (location_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.item (
	item_id int not null AUTO_INCREMENT,
    item_name varchar(50),
	item_level int,
	item_type boolean,
    item_rarity varchar(10),
    primary key (item_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE dbs_projekt.guild (
	guild_id int not null AUTO_INCREMENT,
    guild_leader int not null,
	number_of_players int,
	guild_rank int,
    primary key (guild_id),
    CONSTRAINT fk_leader FOREIGN KEY (guild_leader) REFERENCES game_character (character_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;
