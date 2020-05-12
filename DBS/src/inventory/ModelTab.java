package inventory;

public class ModelTab {
    String name;
    String level;
    String type;
    String rarity;

    public ModelTab(String name, String level, String type, String rarity) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public String getRarity() {
        return rarity;
    }
}