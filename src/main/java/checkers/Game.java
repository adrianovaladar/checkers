package checkers;

import javax.swing.*;

public class Game {

    public Game() {
        this.initializeGame();
    }

    private void initializeGame() {
        new Board();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
