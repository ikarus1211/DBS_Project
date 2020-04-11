package sample.menu;

public class ModelTable {
    String name;
    int level;

    public ModelTable(String name, int levle) {
        this.name = name;
        this.level = levle;
    }

    public String getName() {
        return name;
    }

    public int getLevle() {
        return level;
    }
}
