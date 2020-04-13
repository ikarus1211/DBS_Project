package menu;

public class ModelTable {
    String name;
    String level;
    String id;

    public ModelTable(String name, String level, String id) {
        this.name = name;
        this.level = level;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }
}