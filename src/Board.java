import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener, ActionListener {

    Checkers[][] BoardSquares = new Checkers[8][8];

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    JLabel[] numbers = new JLabel[9];
    JLabel[] characters = new JLabel[8];
    Player[] players = new Player[2];
    int i, j, redCheckers, blackCheckers;
    boolean canJump, canMove, gameOver;
    int pos1i2, pos1j2, pos2i2, pos2j2, pos3i2, pos3j2, pos4i2, pos4j2, pos1i3, pos1j3, pos2i3, pos2j3, pos3i3, pos3j3, pos4i3, pos4j3;
    Messages messages = new Messages();
    JButton startGame = new JButton();
    JButton giveUp = new JButton();
    Wins score = new Wins(0, 0);
    boolean turn = false; // false for red, true for black

    JLabel playerTurn = new JLabel();

    public void changePlayerTurn() {
        turn = !turn;
        showPlayerTurn();
    }

    public void showPlayerTurn() {
        playerTurn.setText(players[bool2Int(turn)].getName() + " Turn");
        if (!turn)
            playerTurn.setForeground(new Color(255, 0, 0));
        else
            playerTurn.setForeground(new Color(0, 0, 0));
    }

    public void startGUI() {

        redCheckers = 12;
        blackCheckers = 12;
        this.setTitle("Checkers Game - Adriano Valadar");
        players[0] = new Player("Red");
        turn = false;
        players[1] = new Player("Black");
        showPlayerTurn();
        centerPanel.setLayout(new GridLayout(9, 9));
        this.addBoard();
        centerPanel.setPreferredSize(new Dimension(560, 560));
        centerPanel.setMinimumSize(new Dimension(560, 560));

        southPanel.add(score);
        northPanel.add(playerTurn);

        JScrollPane scroll = new JScrollPane(messages);
        scroll.setPreferredSize(new Dimension(330, 400));
        scroll.setMinimumSize(new Dimension(330, 400));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        startGame.setText("Start Game");
        startGame.setEnabled(false);
        startGame.setPreferredSize(new Dimension(200, 50));
        startGame.setMinimumSize(new Dimension(200, 50));
        startGame.addActionListener(this);
        giveUp.setText("Give Up");
        giveUp.addActionListener(this);
        giveUp.setPreferredSize(new Dimension(200, 50));
        giveUp.setMinimumSize(new Dimension(200, 50));
        eastPanel.add(scroll);
        eastPanel.add(startGame);
        eastPanel.add(giveUp);
        eastPanel.setPreferredSize(new Dimension(370, 560));
        eastPanel.setMinimumSize(new Dimension(370, 560));

        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    public Board() {
        this.startGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(960, 700));
        this.setResizable(true);
        this.pack();
        this.setVisible(true);

    }

    public int bool2Int(boolean b) {
        return b ? 1 : 0;
    }

    public void addBoard() {
        this.gameOver = false;
        numbers[0] = new JLabel("");
        for (int i = 0; i < characters.length; i++) {
            char character = (char) ('A' + i);
            characters[i] = new JLabel(String.valueOf(character), SwingConstants.CENTER);
        }

        for (int i = 0; i < BoardSquares.length; i++) {
            JLabel j1 = new JLabel(8 - i + "", SwingConstants.CENTER);
            centerPanel.add(j1);
            for (int j = 0; j < BoardSquares.length; j++) {
                Checkers b = new Checkers();
                b.setBorder(null);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    b.setBackground(new Color(249, 192, 102));
                    BoardSquares[i][j] = b;

                } else {
                    if (i < 3) {
                        Checkers black = new Checkers(i, j);
                        BoardSquares[i][j] = black;
                        //BoardSquares[i][j].addActionListener(this);

                    } else if (i > 4) {
                        Checkers red = new Checkers(i, j);
                        BoardSquares[i][j] = red;
                        //BoardSquares[i][j].addActionListener(this);

                    } else {
                        Checkers c = new Checkers(i, j);
                        c.setBackground(new Color(158, 76, 16));
                        BoardSquares[i][j] = c;

                    }
                    BoardSquares[i][j].addMouseListener(this);

                }
                centerPanel.add(BoardSquares[i][j]);
            }

        }
        centerPanel.add(numbers[0]);

        for (JLabel ch : characters) {
            centerPanel.add(ch);
        }
    }

    public boolean hasChecker(Checkers c) {
        return !c.getName().equals("");
    }

    public boolean isGameOver() {
        if (redCheckers == 0 || blackCheckers == 0) {
            return true;
        }
        return false;
    }


    private void gameOver() {
        messages.append("Game over\n");
        String name;
        if (redCheckers == 0) {
            name = players[1].getName();
            players[1].increaseWins();
        } else {
            name = players[0].getName();
            players[0].increaseWins();
        }
        messages.append(name + " won\n");
        score.changeWins(players[0].getWins(), players[1].getWins());
        this.startGame.setEnabled(true);
        this.giveUp.setEnabled(false);
        this.gameOver = true;

        for (Checkers[] boardSquare : BoardSquares) {
            for (int j = 0; j < BoardSquares.length; j++) {
                boardSquare[j].removeMouseListener(this);
            }
        }
    }

    public void jumpChecker(Checkers c) {
        int jumpedCheckerRow;
        int jumpedCheckerColumn;
        jumpedCheckerRow = (this.i + c.i) / 2;
        jumpedCheckerColumn = (this.j + c.j) / 2;
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setName("");
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand("");
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String name = BoardSquares[this.i][this.j].getName();
        String action = BoardSquares[this.i][this.j].getActionCommand();
        Icon icon = BoardSquares[this.i][this.j].getIcon();
        BoardSquares[this.i][this.j].setActionCommand("");
        BoardSquares[this.i][this.j].setIcon(null);
        BoardSquares[this.i][this.j].setName("");
        BoardSquares[c.i][c.j].setName(name);
        BoardSquares[c.i][c.j].setActionCommand(action);
        BoardSquares[c.i][c.j].setIcon(icon);
        messages.append(players[bool2Int(turn)].getName() + " piece on " + this.positionToText(this.i, this.j) + " jumped on " + this.positionToText(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + this.positionToText(c.i, c.j) + "\n");
        if (!turn) blackCheckers--;
        else redCheckers--;
        if (isGameOver()) {
            gameOver();
        }
    }

    public void moveChecker(Checkers c) {

        String name = BoardSquares[this.i][this.j].getName();
        String action = BoardSquares[this.i][this.j].getActionCommand();
        Icon icon = BoardSquares[this.i][this.j].getIcon();

        BoardSquares[this.i][this.j].setName("");
        BoardSquares[this.i][this.j].setActionCommand("");
        BoardSquares[this.i][this.j].setIcon(null);

        messages.append(players[bool2Int(turn)].getName() + " moved piece from " + this.positionToText(this.i, this.j) + " to " + this.positionToText(c.i, c.j) + "\n");
        BoardSquares[c.i][c.j].setName(name);
        BoardSquares[c.i][c.j].setActionCommand(action);
        BoardSquares[c.i][c.j].setIcon(icon);
    }

    public void clear() {
        for (int i = 0; i < BoardSquares.length; i++) {
            for (int j = 0; j < BoardSquares.length; j++) {
                if (!(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1)) {
                    BoardSquares[i][j].setBackground(new Color(158, 76, 16));
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        Checkers c = (Checkers) m.getSource();
        this.canMove = false;
        if (c.getBackground().equals(new Color(255, 0, 0))) {
            jumpChecker(c);
            clear();
            this.canJump = false;

            this.pos1i2 = (c.i) - 1;
            this.pos1j2 = (c.j) - 1;
            this.pos2i2 = (c.i) - 1;
            this.pos2j2 = (c.j) + 1;
            this.pos3i2 = (c.i) + 1;
            this.pos3j2 = (c.j) - 1;
            this.pos4i2 = (c.i) + 1;
            this.pos4j2 = (c.j) + 1;
            this.pos1i3 = (c.i) - 2;
            this.pos1j3 = (c.j) - 2;
            this.pos2i3 = (c.i) - 2;
            this.pos2j3 = (c.j) + 2;
            this.pos3i3 = (c.i) + 2;
            this.pos3j3 = (c.j) - 2;
            this.pos4i3 = (c.i) + 2;
            this.pos4j3 = (c.j) + 2;

            if (!turn && c.isRed()) {
                if (canJump(c, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));
                }
            } else if (turn && c.isBlack()) {
                if (canJump(c, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));
                }
            }

            if (!this.canJump && !turn) {
                Toolkit.getDefaultToolkit().beep();
                if (c.i == 0) {
                    c.king_red();
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.i, c.j) + "\n");
                }
                changePlayerTurn();

            } else if (!this.canJump && turn) {
                Toolkit.getDefaultToolkit().beep();

                if (c.i == 7) {
                    c.king_black();
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.i, c.j) + "\n");
                }
                changePlayerTurn();
            }
        } else if (c.getBackground().equals(new Color(255, 255, 0))) {
            moveChecker(c);
            clear();
            if (!turn) {
                if (c.i == 0) {
                    c.king_red();
                }
                changePlayerTurn();
                Toolkit.getDefaultToolkit().beep();

            } else {
                if (c.i == 7) {

                    c.king_black();
                }
                changePlayerTurn();
                Toolkit.getDefaultToolkit().beep();

            }
        } else {
            clear();
            showLegalMoves(m.getSource());
        }
        //changePlayerTurn();

    }

    public void mouseEntered(MouseEvent m) {

    }

    public void mouseExited(MouseEvent m) {
    }

    public void mousePressed(MouseEvent m) {
    }

    public void mouseReleased(MouseEvent m) {
    }

    public void showLegalMoves(Object m) {
        Checkers c = (Checkers) m;
        this.i = c.i;

        this.j = c.j;

        for (int row = 0; row < BoardSquares.length; row++) {
            for (int col = 0; col < BoardSquares.length; col++) {
                if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
                    this.pos1i2 = (row) - 1;
                    this.pos1j2 = (col) - 1;
                    this.pos2i2 = (row) - 1;
                    this.pos2j2 = (col) + 1;
                    this.pos3i2 = (row) + 1;
                    this.pos3j2 = (col) - 1;
                    this.pos4i2 = (row) + 1;
                    this.pos4j2 = (col) + 1;
                    this.pos1i3 = (row) - 2;
                    this.pos1j3 = (col) - 2;
                    this.pos2i3 = (row) - 2;
                    this.pos2j3 = (col) + 2;
                    this.pos3i3 = (row) + 2;
                    this.pos3j3 = (col) - 2;
                    this.pos4i3 = (row) + 2;
                    this.pos4j3 = (col) + 2;
                    if (!turn && BoardSquares[row][col].isRed() || turn && BoardSquares[row][col].isBlack()) {
                        if (canMove(BoardSquares[row][col])) {
                            this.canMove = true;
                        }
                        if (canJump(c, row, col, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                            this.canJump = true;
                        }
                        if (canJump(c, row, col, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                            this.canJump = true;
                        }
                        if (canJump(c, row, col, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                            this.canJump = true;
                        }
                        if (canJump(c, row, col, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                            this.canJump = true;
                        }
                    }
                }
            }
        }

        if (this.hasChecker(c) && this.canJump) {
            BoardSquares[c.i][c.j].setBackground(new Color(0, 153, 0));
            this.pos1i2 = (c.i) - 1;
            this.pos1j2 = (c.j) - 1;
            this.pos2i2 = (c.i) - 1;
            this.pos2j2 = (c.j) + 1;
            this.pos3i2 = (c.i) + 1;
            this.pos3j2 = (c.j) - 1;
            this.pos4i2 = (c.i) + 1;
            this.pos4j2 = (c.j) + 1;
            this.pos1i3 = (c.i) - 2;
            this.pos1j3 = (c.j) - 2;
            this.pos2i3 = (c.i) - 2;
            this.pos2j3 = (c.j) + 2;
            this.pos3i3 = (c.i) + 2;
            this.pos3j3 = (c.j) - 2;
            this.pos4i3 = (c.i) + 2;
            this.pos4j3 = (c.j) + 2;
            if (!turn && c.isRed()) {
                if (canJump(c, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));

                }
            } else if (turn && c.isBlack()) {
                if (canJump(c, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));

                }
            }
            //}
        } else if (this.hasChecker(c) && !this.canJump && this.canMove) {
            BoardSquares[c.i][c.j].setBackground(new Color(0, 153, 0));
            this.i = c.i;
            this.j = c.j;
            if (canMove(c)) {
                changeColourMove(c);
            }
        } else if (this.hasChecker(c) && !this.canJump && !this.canMove) {
            if (!turn && c.isRed() || turn && c.isBlack()) {
                messages.append("Game over\n");
                if (turn) {
                    messages.append(players[bool2Int(!turn)].getName() + " won\n");
                    players[0].increaseWins();
                    score.changeWins(players[0].getWins(), players[1].getWins());
                } else if (!turn) {
                    messages.append(players[bool2Int(!turn)].getName() + " won\n");
                    players[1].increaseWins();
                    score.changeWins(players[0].getWins(), players[1].getWins());
                }
                this.startGame.setEnabled(true);
                this.giveUp.setEnabled(false);
                this.gameOver = true;
                for (Checkers[] boardSquare : BoardSquares) {
                    for (int j = 0; j < BoardSquares.length; j++) {
                        boardSquare[j].removeMouseListener(this);

                    }
                }
            }
        }
    }

    private void changeColourMove(Checkers c) {
        Pair<Integer, Integer>[] positions = new Pair[4];
        positions[0] = new Pair(c.i - 1, c.j - 1);
        positions[1] = new Pair(c.i - 1, c.j + 1);
        positions[2] = new Pair(c.i + 1, c.j + 1);
        positions[3] = new Pair(c.i + 1, c.j - 1);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey().intValue() < 0 || p.getKey().intValue() >= 8 || p.getValue().intValue() < 0 || p.getValue().intValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!BoardSquares[p.getKey().intValue()][p.getValue().intValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                BoardSquares[p.getKey().intValue()][p.getValue().intValue()].setBackground(new Color(255, 255, 0));
            } else if (!turn && c.isRed() && p.getKey().intValue() > c.i || turn && c.isBlack() && p.getKey().intValue() < c.i) {
                // Regular red piece can only move up and regular black piece can only move down.
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                BoardSquares[p.getKey().intValue()][p.getValue().intValue()].setBackground(new Color(255, 255, 0));
            }
        }
    }

    public boolean canJump(Checkers c, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
            return false;  // (r3,c3) is off the board.
        } else if (!BoardSquares[r3][c3].getName().equals("")) {
            return false;  // (r3,c3) already contains a piece.
        } else if (!turn) {
            if (BoardSquares[r1][c1].getName().equals("red") && BoardSquares[r1][c1].isMan() && r3 > r1) {
                return false;  // Regular red piece can only move up.
            }
            if (BoardSquares[r2][c2].getName().equals("") || BoardSquares[r2][c2].isRed()) {
                return false;  // There is no black piece to jump.
            }
        } else {
            if (BoardSquares[r1][c1].isBlack() && BoardSquares[r1][c1].isMan() && r3 < r1) {
                return false;  // Regular black piece can only move downn.
            } else if (BoardSquares[r2][c2].getName().equals("") || BoardSquares[r2][c2].isBlack()) {
                return false;  // There is no red piece to jump.
            }
        }
        return true;
    }

    public boolean canMove(Checkers c) {
        Pair<Integer, Integer>[] positions = new Pair[4];
        positions[0] = new Pair(c.i - 1, c.j - 1);
        positions[1] = new Pair(c.i - 1, c.j + 1);
        positions[2] = new Pair(c.i + 1, c.j + 1);
        positions[3] = new Pair(c.i + 1, c.j - 1);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey().intValue() < 0 || p.getKey().intValue() >= 8 || p.getValue().intValue() < 0 || p.getValue().intValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!BoardSquares[p.getKey().intValue()][p.getValue().intValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                return true;
            } else if (!turn && c.isRed() && p.getKey().intValue() > c.i || turn && c.isBlack() && p.getKey().intValue() < c.i) {
                // Regular red piece can only move up and regular black piece can only move down.
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                return true;  // The move is legal.
            }
        }
        return false;
    }

    public String positionToText(int x, int y) {
        String position = "";
        position += characters[characters.length - x - 1].getText() + (y + 1);
        return position;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (this.gameOver == true) {
            centerPanel.removeAll();
            addBoard();
            centerPanel.revalidate();
            this.startGame.setEnabled(false);
            this.giveUp.setEnabled(true);
            this.gameOver = false;
            messages.append("Game started. Select a checker to see the options\n");
            turn = false;
            showPlayerTurn();

        } else if (this.gameOver == false) {
            this.gameOver = true;
            this.startGame.setEnabled(true);
            this.giveUp.setEnabled(false);
            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {
                    BoardSquares[i][j].removeMouseListener(this);

                }
            }
            messages.append("Game over\n");
            if (turn == true) {
                messages.append(players[0].getName() + " won\n");
                players[0].increaseWins();
                score.changeWins(players[0].getWins(), players[1].getWins());
            } else if (turn == false) {
                messages.append(players[0].getName() + " won\n");
                players[1].increaseWins();
                score.changeWins(players[0].getWins(), players[1].getWins());
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Board::new);
    }
}
