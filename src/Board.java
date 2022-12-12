import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener, ActionListener {

    Checker[][] BoardSquares = new Checker[8][8];

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    JLabel[] numbers = new JLabel[9];
    JLabel[] characters = new JLabel[8];
    Player[] players = new Player[2];
    int redCheckers, blackCheckers;
    Pair<Integer, Integer> positionCurrentChecker;
    boolean canJump, canMove, gameOver;
    Messages messages = new Messages();
    JButton startGame = new JButton();
    JButton giveUp = new JButton();
    Score score = new Score(0, 0);
    boolean turn = false; // false for red, true for black

    JLabel playerTurn = new JLabel();
    JMenuBar menuBar = new JMenuBar();

    private void changePlayerTurn() {
        turn = !turn;
        showPlayerTurn();
    }

    private void showPlayerTurn() {
        playerTurn.setText(players[bool2Int(turn)].getName() + " Turn");
        if (!turn)
            playerTurn.setForeground(new Color(255, 0, 0));
        else
            playerTurn.setForeground(new Color(0, 0, 0));
    }

    private void setMenuBar() {
        JMenu menu = new JMenu("Menu");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), "Game developed by Adriano Valadar");
            }
        });
        JMenuItem redPlayerName = new JMenuItem("Change " + players[0].getName() + " name");
        redPlayerName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Insert name:");
                if (!name.equals(players[1].getName())) {
                    String oldName = players[0].getName();
                    players[0].setName(name);
                    messages.append(oldName + " player changed name to " + name + "\n");
                    showPlayerTurn();
                    score.show(players[0].getWins(), players[1].getWins());
                }
            }
        });
        JMenuItem blackPlayerName = new JMenuItem("Change " + players[1].getName() + " name");
        blackPlayerName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Insert name:");
                if (!name.equals(players[0].getName())) {
                    String oldName = players[1].getName();
                    players[1].setName(name);
                    messages.append(oldName + " player changed name to " + name + "\n");
                    showPlayerTurn();
                    score.show(players[0].getWins(), players[1].getWins());
                }
            }
        });
        menu.add(redPlayerName);
        menu.add(blackPlayerName);
        menu.add(about);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

    }

    private void startGUI() {

        redCheckers = 12;
        blackCheckers = 12;
        this.setTitle("Checkers Game");
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

        this.setMenuBar();
        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    private Board() {
        this.startGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(960, 700));
        this.setResizable(true);
        this.pack();
        this.setVisible(true);

    }

    private int bool2Int(boolean b) {
        return b ? 1 : 0;
    }

    private void addBoard() {
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
                Checker b = new Checker();
                b.setBorder(null);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    b.setBackground(new Color(249, 192, 102));
                    BoardSquares[i][j] = b;

                } else {
                    if (i < 3) {
                        Checker black = new Checker(i, j);
                        BoardSquares[i][j] = black;
                        //BoardSquares[i][j].addActionListener(this);

                    } else if (i > 4) {
                        Checker red = new Checker(i, j);
                        BoardSquares[i][j] = red;
                        //BoardSquares[i][j].addActionListener(this);

                    } else {
                        Checker c = new Checker(i, j);
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

    private boolean hasChecker(Checker c) {
        return !c.getName().equals("");
    }

    private boolean isGameOver() {
        if (redCheckers == 0 || blackCheckers == 0) {
            return true;
        }
        return false;
    }

    private void removeCheckerActions() {
        for (int i = 0; i < BoardSquares.length; i++) {
            for (int j = 0; j < BoardSquares.length; j++) {
                if (!(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1)) {
                    BoardSquares[i][j].removeMouseListener(this);
                }
            }
        }
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
        score.show(players[0].getWins(), players[1].getWins());
        this.startGame.setEnabled(true);
        this.giveUp.setEnabled(false);
        this.gameOver = true;

        removeCheckerActions();
    }

    private void jumpChecker(Checker c) {
        int jumpedCheckerRow;
        int jumpedCheckerColumn;
        jumpedCheckerRow = (positionCurrentChecker.getKey() + c.position.getKey()) / 2;
        jumpedCheckerColumn = (positionCurrentChecker.getValue() + c.position.getValue()) / 2;
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setName("");
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand("");
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String name = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();
        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand("");
        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);
        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName("");
        BoardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        BoardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        BoardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
        messages.append(players[bool2Int(turn)].getName() + " piece on " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " jumped on " + this.positionToText(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        if (!turn) blackCheckers--;
        else redCheckers--;
        if (isGameOver()) {
            gameOver();
        }
    }

    private void moveChecker(Checker c) {
        String name = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();

        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName("");
        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand("");
        BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);

        messages.append(players[bool2Int(turn)].getName() + " moved piece from " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        BoardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        BoardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        BoardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
    }

    private void clear() {
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
        Checker c = (Checker) m.getSource();
        this.canMove = false;
        if (c.getBackground().equals(new Color(255, 0, 0))) {
            jumpChecker(c);
            clear();
            this.canJump = false;

            if (!turn && c.isRed() || turn && c.isBlack()) {
                if (canJump(c)) {
                    this.canJump = true;
                    positionCurrentChecker = c.position;
                    BoardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setBackground(new Color(0, 153, 0));
                    changeColourJump(c);
                }
            }

            if (!this.canJump && !turn) {
                Toolkit.getDefaultToolkit().beep();
                if (c.position.getKey() == 0) {
                    c.kingRed();
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
                }
                changePlayerTurn();

            } else if (!this.canJump && turn) {
                Toolkit.getDefaultToolkit().beep();

                if (c.position.getKey() == 7) {
                    c.kingBlack();
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
                }
                changePlayerTurn();
            }
        } else if (c.getBackground().equals(new Color(255, 255, 0))) {
            moveChecker(c);
            clear();
            if (!turn) {
                if (c.position.getKey() == 0) {
                    c.kingRed();
                }
                changePlayerTurn();
                Toolkit.getDefaultToolkit().beep();

            } else {
                if (c.position.getKey() == 7) {

                    c.kingBlack();
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

    private void showLegalMoves(Object m) {
        Checker c = (Checker) m;
        positionCurrentChecker = c.position;

        positionCurrentChecker = c.position;

        for (int row = 0; row < BoardSquares.length; row++) {
            for (int col = 0; col < BoardSquares.length; col++) {
                if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
                    if (!turn && BoardSquares[row][col].isRed() || turn && BoardSquares[row][col].isBlack()) {
                        if (canMove(BoardSquares[row][col])) {
                            this.canMove = true;
                        }
                        if (canJump(BoardSquares[row][col])) {
                            this.canJump = true;
                        }
                    }
                }
            }
        }

        if (this.hasChecker(c) && this.canJump) {
            BoardSquares[c.position.getKey()][c.position.getValue()].setBackground(new Color(0, 153, 0));
            if (!turn && c.isRed() || turn && c.isBlack()) {
                if (canJump(c)) {
                    this.canJump = true;
                    changeColourJump(c);
                }
            }
        } else if (this.hasChecker(c) && !this.canJump && this.canMove) {
            BoardSquares[c.position.getKey()][c.position.getValue()].setBackground(new Color(0, 153, 0));
            positionCurrentChecker = c.position;
            if (canMove(c)) {
                changeColourMove(c);
            }
        } else if (this.hasChecker(c) && !this.canJump && !this.canMove) {
            if (!turn && c.isRed() || turn && c.isBlack()) {
                messages.append("Game over\n");
                if (turn) {
                    messages.append(players[bool2Int(!turn)].getName() + " won\n");
                    players[0].increaseWins();
                    score.show(players[0].getWins(), players[1].getWins());
                } else if (!turn) {
                    messages.append(players[bool2Int(!turn)].getName() + " won\n");
                    players[1].increaseWins();
                    score.show(players[0].getWins(), players[1].getWins());
                }
                this.startGame.setEnabled(true);
                this.giveUp.setEnabled(false);
                this.gameOver = true;
                removeCheckerActions();
            }
        }
    }

    private void changeColourMove(Checker c) {
        Pair<Integer, Integer>[] positions = getSurroundingPositionsToMove(c);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey().intValue() < 0 || p.getKey().intValue() >= 8 || p.getValue().intValue() < 0 || p.getValue().intValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!BoardSquares[p.getKey().intValue()][p.getValue().intValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                BoardSquares[p.getKey().intValue()][p.getValue().intValue()].setBackground(new Color(255, 255, 0));
            } else if (!turn && c.isRed() && p.getKey().intValue() > c.position.getKey() || turn && c.isBlack() && p.getKey().intValue() < c.position.getKey()) {
                // Regular red piece can only move up and regular black piece can only move down.
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                BoardSquares[p.getKey().intValue()][p.getValue().intValue()].setBackground(new Color(255, 255, 0));
            }
        }
    }

    private void changeColourJump(Checker c) { //todo: refactor
        Pair<Integer, Integer>[] positions = getSurroundingPositionsToJump(c);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) {
                continue;  // p is off the board.
            } else if (!BoardSquares[p.getKey()][p.getValue()].getName().equals("")) {
                continue;  // p already contains a piece.
            } else if (BoardSquares[c.position.getKey()][c.position.getValue()].getName().equals("red") && BoardSquares[c.position.getKey()][c.position.getValue()].isMan() && p.getKey().intValue() > c.position.getKey() || BoardSquares[c.position.getKey()][c.position.getValue()].isBlack() && BoardSquares[c.position.getKey()][c.position.getValue()].isMan() && p.getKey().intValue() < c.position.getKey()) {
                continue;  // Red turn: Regular red piece can only jump up. Black turn: Regular black piece can only jump down.
            } else if ((!turn && BoardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && BoardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed())) {
                BoardSquares[p.getKey()][p.getValue()].setBackground(new Color(255, 0, 0)); // Red turn: There is a black piece to jump. Black turn: There is a red piece to jump.
            }
        }
    }

    private boolean canJump(Checker c) {
        Pair<Integer, Integer>[] positionsJump = getSurroundingPositionsToJump(c);
        for (Pair<Integer, Integer> p : positionsJump) {
            if (p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) {
                continue;  // p is off the board.
            } else if (!BoardSquares[p.getKey()][p.getValue()].getName().equals("")) {
                continue;  // p already contains a piece.
            } else if (BoardSquares[c.position.getKey()][c.position.getValue()].getName().equals("red") && BoardSquares[c.position.getKey()][c.position.getValue()].isMan() && p.getKey().intValue() > c.position.getKey() || BoardSquares[c.position.getKey()][c.position.getValue()].isBlack() && BoardSquares[c.position.getKey()][c.position.getValue()].isMan() && p.getKey().intValue() < c.position.getKey()) {
                continue;  // Red turn: Regular red piece can only jump up. Black turn: Regular black piece can only jump down.
            } else if ((!turn && BoardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && BoardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed())) {
                return true; // Red turn: There is a black piece to jump. Black turn: There is a red piece to jump.
            }
        }
        return false;
    }

    private boolean canMove(Checker c) {
        Pair<Integer, Integer>[] positions = getSurroundingPositionsToMove(c);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey().intValue() < 0 || p.getKey().intValue() >= 8 || p.getValue().intValue() < 0 || p.getValue().intValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!BoardSquares[p.getKey().intValue()][p.getValue().intValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                return true;
            } else if (!turn && c.isRed() && p.getKey().intValue() > c.position.getKey() || turn && c.isBlack() && p.getKey().intValue() < c.position.getKey()) {
                // Regular red piece can only move up and regular black piece can only move down.
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                return true;  // The move is legal.
            }
        }
        return false;
    }

    private Pair<Integer, Integer>[] getSurroundingPositionsToMove(Checker c) {
        Pair<Integer, Integer>[] positions = new Pair[4];
        positions[0] = new Pair(c.position.getKey() - 1, c.position.getValue() - 1);
        positions[1] = new Pair(c.position.getKey() - 1, c.position.getValue() + 1);
        positions[2] = new Pair(c.position.getKey() + 1, c.position.getValue() + 1);
        positions[3] = new Pair(c.position.getKey() + 1, c.position.getValue() - 1);
        return positions;
    }

    private Pair<Integer, Integer>[] getSurroundingPositionsToJump(Checker c) {
        Pair<Integer, Integer>[] positions = new Pair[4];
        positions[0] = new Pair(c.position.getKey() - 2, c.position.getValue() - 2);
        positions[1] = new Pair(c.position.getKey() - 2, c.position.getValue() + 2);
        positions[2] = new Pair(c.position.getKey() + 2, c.position.getValue() + 2);
        positions[3] = new Pair(c.position.getKey() + 2, c.position.getValue() - 2);
        return positions;
    }

    private String positionToText(int y, int x) {
        String position = "";
        position += characters[x].getText() + (BoardSquares.length - y);
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
            removeCheckerActions();
            messages.append("Game over\n");
            if (turn == true) {
                messages.append(players[0].getName() + " won\n");
                players[0].increaseWins();
                score.show(players[0].getWins(), players[1].getWins());
            } else if (turn == false) {
                messages.append(players[0].getName() + " won\n");
                players[1].increaseWins();
                score.show(players[0].getWins(), players[1].getWins());
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Board::new);
    }
}
