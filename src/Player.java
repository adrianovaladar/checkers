public class Player {
    private String name;
    private int wins;

    public int getWins() {
        return wins;
    }

    Player(String n) {
        name = n;
        wins = 0;
    }

    void increaseWins() {
        wins++;
    }

    String getName() {
        return name;
    }

    void setName(String n) {
        name = n;
    }
}
