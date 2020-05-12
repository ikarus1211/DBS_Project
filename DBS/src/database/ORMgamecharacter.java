package database;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "game_character")
@Table(name = "game_character")
public class ORMgamecharacter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "character_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "character_id", unique = true)
    private int id;

    @Column(name = "character_name", nullable = false)
    private String cName;

    @Column(name = "hours_played")
    private int hours_played;

    @Column(name = "character_xp")
    private int xp;

    @Column(name = "class")
    private String char_class;

    @Column(name = "race")
    private String char_race;

    @Column(name = "game_money")
    private int money;

    @Column(name = "guild_leader")
    private Boolean guild_leader;

    @ManyToOne
    @JoinColumn(name = "guild_id",referencedColumnName = "guild_id")
    private ORMguild guild_id;

    @ManyToOne
    @JoinColumn(name = "player_owner",referencedColumnName = "player_id")
    private ORMplayer owner_id;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getHours_played() {
        return hours_played;
    }

    public void setHours_played(int hours_played) {
        this.hours_played = hours_played;
    }

    public String getChar_class() {
        return char_class;
    }

    public void setChar_class(String char_class) {
        this.char_class = char_class;
    }

    public String getChar_race() {
        return char_race;
    }

    public void setChar_race(String char_race) {
        this.char_race = char_race;
    }

    public boolean isGuild_leader() {
        return guild_leader;
    }

    public void setGuild_leader(boolean guild_leader) {
        this.guild_leader = guild_leader;
    }

    public ORMguild getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(ORMguild guild_id) {
        this.guild_id = guild_id;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public ORMplayer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(ORMplayer owner_id) {
        this.owner_id = owner_id;
    }

    public int getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public int getMoney() {
        return money;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
