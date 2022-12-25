import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Board extends JFrame implements MouseListener, ActionListener {

    Checker[][] boardSquares = new Checker[8][8];

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
    Score score = new Score();
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
        about.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Game developed by Adriano Valadar"));
        JMenuItem redPlayerName = new JMenuItem("Change " + players[0].getName() + " name");
        redPlayerName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert name:");
            String oldName = players[0].getName();
            players[0].setName(name + " (Red)");
            messages.append(oldName + " player changed name to " + name + "\n");
            showPlayerTurn();
            score.show(players[0].getName(), players[1].getName(), players[0].getWins(), players[1].getWins());
        });
        JMenuItem blackPlayerName = new JMenuItem("Change " + players[1].getName() + " name");
        blackPlayerName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert name:");
            String oldName = players[1].getName();
            players[1].setName(name + " (Black)");
            messages.append(oldName + " player changed name to " + name + "\n");
            showPlayerTurn();
            score.show(players[0].getName(), players[1].getName(), players[0].getWins(), players[1].getWins());

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
        players[1] = new Player("Black");
        score.show(players[0].getName(), players[1].getName(), players[0].getWins(), players[1].getWins());
        turn = false;
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

        for (int i = 0; i < boardSquares.length; i++) {
            JLabel j1 = new JLabel(8 - i + "", SwingConstants.CENTER);
            centerPanel.add(j1);
            for (int j = 0; j < boardSquares.length; j++) {
                Checker b = new Checker();
                b.setBorder(null);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    b.setBackground(new Color(249, 192, 102));
                    boardSquares[i][j] = b;

                } else {
                    if (i < 3) {
                        Checker black = new Checker(i, j);
                        boardSquares[i][j] = black;
                        //BoardSquares[i][j].addActionListener(this);

                    } else if (i > 4) {
                        Checker red = new Checker(i, j);
                        boardSquares[i][j] = red;
                        //BoardSquares[i][j].addActionListener(this);

                    } else {
                        Checker c = new Checker(i, j);
                        c.setBackground(new Color(158, 76, 16));
                        boardSquares[i][j] = c;

                    }
                    boardSquares[i][j].addMouseListener(this);

                }
                centerPanel.add(boardSquares[i][j]);
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
        return redCheckers == 0 || blackCheckers == 0;
    }

    private void removeCheckerActions() {
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares.length; j++) {
                if (!(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1)) {
                    boardSquares[i][j].removeMouseListener(this);
                }
            }
        }
    }

    private void gameOver() {
        messages.append("Game over\n");
        String name;
        if (redCheckers == 0 || !turn) {
            name = players[1].getName();
            players[1].increaseWins();
        } else {
            name = players[0].getName();
            players[0].increaseWins();
        }
        messages.append(name + " won\n");
        score.show(players[0].getName(), players[1].getName(), players[0].getWins(), players[1].getWins());
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
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setName("");
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand("");
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String name = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand("");
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName("");
        boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
        messages.append(players[bool2Int(turn)].getName() + " piece on " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " jumped on " + this.positionToText(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        if (!turn) blackCheckers--;
        else redCheckers--;
        if (isGameOver()) {
            gameOver();
        }
    }

    private void moveChecker(Checker c) {
        String name = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();

        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName("");
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand("");
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);

        messages.append(players[bool2Int(turn)].getName() + " moved piece from " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
    }

    private void clear() {
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares.length; j++) {
                if (!(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1)) {
                    boardSquares[i][j].setBackground(new Color(158, 76, 16));
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
                    boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setBackground(new Color(0, 153, 0));
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
            } else if (!this.canJump) { //in this condition, we can consider that turn is true (black turn)
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
            } else {
                if (c.position.getKey() == 7) {

                    c.kingBlack();
                }
            }
            changePlayerTurn();
            Toolkit.getDefaultToolkit().beep();
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

        for (int row = 0; row < boardSquares.length; row++) {
            for (int col = 0; col < boardSquares.length; col++) {
                if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
                    if (!turn && boardSquares[row][col].isRed() || turn && boardSquares[row][col].isBlack()) {
                        if (canMove(boardSquares[row][col])) {
                            this.canMove = true;
                        }
                        if (canJump(boardSquares[row][col])) {
                            this.canJump = true;
                        }
                    }
                }
            }
        }

        if (this.hasChecker(c) && this.canJump) {
            boardSquares[c.position.getKey()][c.position.getValue()].setBackground(new Color(0, 153, 0));
            if (!turn && c.isRed() || turn && c.isBlack()) {
                if (canJump(c)) {
                    this.canJump = true;
                    changeColourJump(c);
                }
            }
        } else if (this.hasChecker(c) && !this.canJump && this.canMove) {
            boardSquares[c.position.getKey()][c.position.getValue()].setBackground(new Color(0, 153, 0));
            positionCurrentChecker = c.position;
            if (canMove(c)) {
                changeColourMove(c);
            }
        } else if (this.hasChecker(c) && !this.canJump && !this.canMove) {
            if (!turn && c.isRed() || turn && c.isBlack()) {
                gameOver();
            }
        }
    }

    private void changeColourMove(Checker c) {
        ArrayList<Pair<Integer, Integer>> positions = getSurroundingPositionsToMove(c);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!boardSquares[p.getKey()][p.getValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                boardSquares[p.getKey()][p.getValue()].setBackground(new Color(255, 255, 0));
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                boardSquares[p.getKey()][p.getValue()].setBackground(new Color(255, 255, 0));
            }
        }
    }

    private void changeColourJump(Checker c) {
        ArrayList<Pair<Integer, Integer>> positions = getSurroundingPositionsToJump(c);
        for (Pair<Integer, Integer> p : positions) {
            if ((p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) || !boardSquares[p.getKey()][p.getValue()].getName().equals("")) {
                // first condition: p is off the board, second condition: p already contains a piece.
            } else if ((!turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed())) {
                boardSquares[p.getKey()][p.getValue()].setBackground(new Color(255, 0, 0)); // Red turn: There is a black piece to jump. Black turn: There is a red piece to jump.
            }
        }
    }

    private boolean canJump(Checker c) {
        ArrayList<Pair<Integer, Integer>> positionsJump = getSurroundingPositionsToJump(c);
        for (Pair<Integer, Integer> p : positionsJump) {
            if ((p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) || (!boardSquares[p.getKey()][p.getValue()].getName().equals(""))) {
                // first condition: p is off the board, second condition: p already contains a piece.
            } else if ((!turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed())) {
                return true; // Red turn: There is a black piece to jump. Black turn: There is a red piece to jump.
            }
        }
        return false;
    }

    private boolean canMove(Checker c) {
        ArrayList<Pair<Integer, Integer>> positions = getSurroundingPositionsToMove(c);
        for (Pair<Integer, Integer> p : positions) {
            if (p.getKey() < 0 || p.getKey() >= 8 || p.getValue() < 0 || p.getValue() >= 8) {
                continue;  // (r2,c2) is off the board.
            } else if (!boardSquares[p.getKey()][p.getValue()].getName().equals("")) {
                continue;  // (r2,c2) already contains a piece.
            }
            if ((!turn && c.isRed() || turn && c.isBlack()) && c.getActionCommand().equals("king")) {
                return true;
            } else if (!turn && c.isRed() || turn && c.isBlack()) {
                return true;  // The move is legal.
            }
        }
        return false;
    }

    private ArrayList<Pair<Integer, Integer>> getSurroundingPositionsToMove(Checker c) {
        ArrayList<Pair<Integer, Integer>> positions = new ArrayList<>();
        if (c.isRed() || c.isKing()) {
            positions.add(new Pair<>(c.position.getKey() - 1, c.position.getValue() - 1));
            positions.add(new Pair<>(c.position.getKey() - 1, c.position.getValue() + 1));
        }
        if (c.isBlack() || c.isKing()) {
            positions.add(new Pair<>(c.position.getKey() + 1, c.position.getValue() + 1));
            positions.add(new Pair<>(c.position.getKey() + 1, c.position.getValue() - 1));
        }
        return positions;
    }

    private ArrayList<Pair<Integer, Integer>> getSurroundingPositionsToJump(Checker c) {
        ArrayList<Pair<Integer, Integer>> positions = new ArrayList<>();
        if (c.isRed() || c.isKing()) {
            positions.add(new Pair<>(c.position.getKey() - 2, c.position.getValue() - 2));
            positions.add(new Pair<>(c.position.getKey() - 2, c.position.getValue() + 2));
        }
        if (c.isBlack() || c.isKing()) {
            positions.add(new Pair<>(c.position.getKey() + 2, c.position.getValue() + 2));
            positions.add(new Pair<>(c.position.getKey() + 2, c.position.getValue() - 2));
        }
        return positions;
    }

    private String positionToText(int y, int x) {
        String position = "";
        position += characters[x].getText() + (boardSquares.length - y);
        return position;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (this.gameOver) {
            centerPanel.removeAll();
            addBoard();
            centerPanel.revalidate();
            this.startGame.setEnabled(false);
            this.giveUp.setEnabled(true);
            this.gameOver = false;
            messages.append("Game started. Select a checker to view the options\n");
            turn = false;
            showPlayerTurn();

        } else {
            gameOver();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Board::new);
    }
}
