package leaderboard;

public class LbModelTable {
    private String playerName;
    private String bestCharacter;
    private String avgExperience;
    private String numberCharacter;
    private String bestExperience;

    public LbModelTable(String playerName, String bestCharacter, float avgExperience, int numberCharacter, int bestExperience) {
        this.playerName = playerName;
        this.bestCharacter = bestCharacter;
        this.avgExperience = Float.toString(avgExperience);
        this.numberCharacter = Float.toString(numberCharacter);
        this.bestExperience = Integer.toString(bestExperience);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBestCharacter() {
        return bestCharacter;
    }

    public String getAvgExperience() {
        return avgExperience;
    }

    public String getNumberCharacter() {
        return numberCharacter;
    }

    public String getBestExperience() {
        return bestExperience;
    }
}
