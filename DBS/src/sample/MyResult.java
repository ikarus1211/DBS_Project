package sample;

public final class MyResult {
    private final int finalLevel;
    private final double exp;


    public MyResult(int first, double second) {
        this.finalLevel = first;
        this.exp = second;
    }

    public int getFinalLevel() {
        return finalLevel;
    }

    public double getExp() {
        return exp;
    }
}