package sample;

public class Character {

    private int character_id;
    private String char_name;
    private int hours;
    private int xp;
    private String char_class;
    private String race;
    private int money;
    private boolean guild_leader;
    private int guild_id;
    private int owner;

    public Character(String char_name, int hours, int xp, String char_class, String race, int money, boolean guild_leader, int guild_id, int owner) {

        this.char_name = char_name;
        this.hours = hours;
        this.xp = xp;
        this.char_class = char_class;
        this.race = race;
        this.money = money;
        this.guild_leader = guild_leader;
        this.guild_id = guild_id;
        this.owner = owner;
    }

    public int getCharacter_id() {
        return character_id;
    }

    public String getChar_name() {
        return char_name;
    }

    public int getHours() {
        return hours;
    }

    public int getXp() {
        return xp;
    }

    public String getChar_class() {
        return char_class;
    }

    public String getRace() {
        return race;
    }

    public int getMoney() {
        return money;
    }

    public boolean isGuild_leader() {
        return guild_leader;
    }

    public int getGuild_id() {
        return guild_id;
    }

    public int getOwner() {
        return owner;
    }

    public void setCharacter_id(int character_id) {
        this.character_id = character_id;
    }

    public void setChar_name(String char_name) {
        this.char_name = char_name;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setChar_class(String char_class) {
        this.char_class = char_class;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setGuild_leader(boolean guild_leader) {
        this.guild_leader = guild_leader;
    }

    public void setGuild_id(int guild_id) {
        this.guild_id = guild_id;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
