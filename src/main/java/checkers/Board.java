package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Board extends JPanel {
    public static final int BOARD_SIZE = 8;
    Checker[][] boardSquares = new Checker[BOARD_SIZE][BOARD_SIZE];
    JLabel[] numbers = new JLabel[BOARD_SIZE + 1];
    JLabel[] characters = new JLabel[BOARD_SIZE];
    Color lightOrange = new Color(249, 192, 102);
    Color darkBrown = new Color(158, 76, 16);
    transient MouseListener listener;

    public Board(MouseListener listener) {
        this.setLayout(new GridLayout(9, 9));
        this.setPreferredSize(new Dimension(560, 560));
        this.setMinimumSize(new Dimension(560, 560));
        this.listener = listener;
    }

    public void create() {
        numbers[0] = new JLabel("");
        for (int i = 0; i < characters.length; i++) {
            char character = (char) ('A' + i);
            characters[i] = new JLabel(String.valueOf(character), SwingConstants.CENTER);
        }
        for (int i = 0; i < boardSquares.length; i++) {
            JLabel j1 = new JLabel(8 - i + "", SwingConstants.CENTER);
            this.add(j1);
            for (int j = 0; j < boardSquares.length; j++) {
                Checker b = new Checker();
                b.setBorder(null);
                if (!isPositionValid(i, j)) {
                    b.setBackground(lightOrange);
                    boardSquares[i][j] = b;

                } else {
                    Checker c = new Checker(i, j);
                    boardSquares[i][j] = c;
                    boardSquares[i][j].addMouseListener(listener);
                    boardSquares[i][j].setBackground(darkBrown);
                }
                this.add(boardSquares[i][j]);
            }
        }
        this.add(numbers[0]);
        for (JLabel ch : characters) {
            this.add(ch);
        }
    }

    public boolean isPositionValid(int i, int j) {
        return i % 2 != j % 2;
    }

    public String positionToText(int y, int x) {
        String position = "";
        position += this.characters[x].getText() + (this.boardSquares.length - y);
        return position;
    }

}
