
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.event.*;

public class board extends JFrame implements MouseListener, ActionListener {

    checkers[][] BoardSquares = new checkers[8][8];

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
    wins score = new wins(0, 0);
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

    public board() {
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
                checkers b = new checkers();
                b.setBorder(null);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    b.setBackground(new Color(249, 192, 102));
                    BoardSquares[i][j] = b;

                } else {
                    if (i < 3) {
                        checkers black = new checkers(i, j);
                        BoardSquares[i][j] = black;
                        //BoardSquares[i][j].addActionListener(this);

                    } else if (i > 4) {
                        checkers red = new checkers(i, j);
                        BoardSquares[i][j] = red;
                        //BoardSquares[i][j].addActionListener(this);

                    } else {
                        checkers c = new checkers(i, j);
                        c.setBackground(new Color(158, 76, 16));
                        BoardSquares[i][j] = c;

                    }
                    BoardSquares[i][j].addMouseListener(this);

                }
                centerPanel.add(BoardSquares[i][j]);
            }

        }
        centerPanel.add(numbers[0]);

        for (JLabel letra : characters) {
            centerPanel.add(letra);
        }
    }

    public boolean hasChecker(checkers c) {
        return !c.getActionCommand().equals("");
    }

    public void jumpChecker(checkers c) {
        int jumpedCheckerRow;
        int jumpedCheckerColumn;
        jumpedCheckerRow = (this.i + c.i) / 2;
        jumpedCheckerColumn = (this.j + c.j) / 2;
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand("");
        BoardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String action = BoardSquares[this.i][this.j].getActionCommand();
        Icon icon = BoardSquares[this.i][this.j].getIcon();
        BoardSquares[this.i][this.j].setActionCommand("");
        BoardSquares[this.i][this.j].setIcon(null);
        BoardSquares[c.i][c.j].setActionCommand(action);
        BoardSquares[c.i][c.j].setIcon(icon);
        messages.append(players[bool2Int(turn)].getName() + " piece on " + this.BoardPosition(this.i, this.j) + " jumped on " + this.BoardPosition(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + this.BoardPosition(c.i, c.j) + "\n");
        if (!turn) blackCheckers--;
        else redCheckers--;
        if (redCheckers == 0 || blackCheckers == 0) {
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

            for (checkers[] boardSquare : BoardSquares) {
                for (int j = 0; j < BoardSquares.length; j++) {
                    boardSquare[j].removeMouseListener(this);

                }
            }
        }

    }

    public void moveChecker(checkers c) {

        String action = BoardSquares[this.i][this.j].getActionCommand();
        Icon icon = BoardSquares[this.i][this.j].getIcon();

        BoardSquares[this.i][this.j].setActionCommand("");
        BoardSquares[this.i][this.j].setIcon(null);

        messages.append(players[bool2Int(turn)].getName() + " moved piece from " + this.BoardPosition(this.i, this.j) + " to " + this.BoardPosition(c.i, c.j) + "\n");
        BoardSquares[c.i][c.j].setActionCommand(action);
        BoardSquares[c.i][c.j].setIcon(icon);
    }

    public void clear() {
        for (int i = 0; i < BoardSquares.length; i++) {
            for (int j = 0; j < BoardSquares.length; j++) {

                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {

                } else {
                    BoardSquares[i][j].setBackground(new Color(158, 76, 16));

                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        checkers c = (checkers) m.getSource();
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

            if (!turn && BoardSquares[c.i][c.j].getActionCommand().equals("red") || !turn && BoardSquares[c.i][c.j].getActionCommand().equals("king_red")) {
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
            } else if (turn && BoardSquares[c.i][c.j].getActionCommand().equals("black") || turn && BoardSquares[c.i][c.j].getActionCommand().equals("king_black")) {
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
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.BoardPosition(c.i, c.j) + "\n");
                }
                changePlayerTurn();

            } else if (!this.canJump && turn) {
                Toolkit.getDefaultToolkit().beep();

                if (c.i == 7) {
                    c.king_black();
                    messages.append(players[bool2Int(turn)].getName() + " has a king in " + this.BoardPosition(c.i, c.j) + "\n");
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
        checkers c = (checkers) m;
        this.i = c.i;

        this.j = c.j;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
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
                if (!turn && BoardSquares[row][col].getActionCommand().equals("red") || !turn && BoardSquares[row][col].getActionCommand().equals("king_red")) {
                    if (canMove(c, row, col, this.pos1i2, this.pos1j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, row, col, this.pos2i2, this.pos2j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, row, col, this.pos3i2, this.pos3j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, row, col, this.pos4i2, this.pos4j2)) {
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
                } else if (turn && BoardSquares[row][col].getActionCommand().equals("black") || turn && BoardSquares[row][col].getActionCommand().equals("king_black")) {

                    if (canMove(c, row, col, this.pos1i2, this.pos1j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, row, col, this.pos2i2, this.pos2j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, row, col, this.pos3i2, this.pos3j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, row, col, this.pos4i2, this.pos4j2)) {
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
            if (!turn && BoardSquares[c.i][c.j].getActionCommand().equals("red") || !turn && BoardSquares[c.i][c.j].getActionCommand().equals("king_red")) {
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
            } else if (turn && BoardSquares[c.i][c.j].getActionCommand().equals("black") || turn && BoardSquares[c.i][c.j].getActionCommand().equals("king_black")) {
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
            pos1i2 = (c.i) - 1;
            pos1j2 = (c.j) - 1;
            pos2i2 = (c.i) - 1;
            pos2j2 = (c.j) + 1;
            pos3i2 = (c.i) + 1;
            pos3j2 = (c.j) - 1;
            pos4i2 = (c.i) + 1;
            pos4j2 = (c.j) + 1;
            BoardSquares[c.i][c.j].setBackground(new Color(0, 153, 0));
            this.i = c.i;
            this.j = c.j;
            if (canMove(c, c.i, c.j, this.pos1i2, this.pos1j2)) {
                BoardSquares[pos1i2][pos1j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, c.i, c.j, this.pos2i2, this.pos2j2)) {
                BoardSquares[pos2i2][pos2j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, c.i, c.j, this.pos3i2, this.pos3j2)) {
                BoardSquares[pos3i2][pos3j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, c.i, c.j, this.pos4i2, this.pos4j2)) {
                BoardSquares[pos4i2][pos4j2].setBackground(new Color(255, 255, 0));

            }
        } else if (this.hasChecker(c) && !this.canJump && !this.canMove) {
            if (!turn && c.getActionCommand().equals("red") || !turn && c.getActionCommand().equals("king_red") || turn && c.getActionCommand().equals("black") || turn && c.getActionCommand().equals("king_black")) {

                messages.append("Game over\n");
                if (turn) {
                    messages.append(players[bool2Int(turn)].getName() + " won\n");
                    players[0].increaseWins();
                    score.changeWins(players[0].getWins(), players[1].getWins());
                } else if (!turn) {
                    messages.append(players[bool2Int(turn)].getName() + " won\n");
                    players[1].increaseWins();
                    score.changeWins(players[0].getWins(), players[1].getWins());
                }
                this.startGame.setEnabled(true);
                this.giveUp.setEnabled(false);
                this.gameOver = true;
                for (checkers[] boardSquare : BoardSquares) {
                    for (int j = 0; j < BoardSquares.length; j++) {
                        boardSquare[j].removeMouseListener(this);

                    }
                }
            }
        }
    }

    public boolean canJump(checkers c, int r1, int c1, int r2, int c2, int r3, int c3) {

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
            return false;  // (r3,c3) is off the board.
        } else if (!BoardSquares[r3][c3].getActionCommand().equals("")) {

            return false;  // (r3,c3) already contains a piece.
        } else if (turn == false) {
            if (BoardSquares[r1][c1].getActionCommand().equals("red") && r3 > r1) {

                return false;  // Regular red piece can only move up.
            }
            if (BoardSquares[r2][c2].getActionCommand().equals("") || BoardSquares[r2][c2].getActionCommand().equals("red") || BoardSquares[r2][c2].getActionCommand().equals("king_red")) {
                return false;  // There is no black piece to jump.
            }
        } else {
            if (BoardSquares[r1][c1].getActionCommand().equals("black") && r3 < r1) {
                return false;  // Regular black piece can only move downn.
            } else if (BoardSquares[r2][c2].getActionCommand().equals("") || BoardSquares[r2][c2].getActionCommand().equals("black") || BoardSquares[r2][c2].getActionCommand().equals("king_black")) {
                return false;  // There is no red piece to jump.
            }
        }
        return true;
    }

    public boolean canMove(checkers c, int r1, int c1, int r2, int c2) {
        //modificar para damas
        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8) {
            return false;  // (r2,c2) is off the board.
        } else if (BoardSquares[r2][c2].getActionCommand().equals("red") || BoardSquares[r2][c2].getActionCommand().equals("black") || BoardSquares[r2][c2].getActionCommand().equals("king_black") || BoardSquares[r2][c2].getActionCommand().equals("king_red")) {
            return false;  // (r2,c2) already contains a piece.
        } else if (turn == false) {
            if (c.getActionCommand().equals("king_red")) {
                return true;
            } else if (c.getActionCommand().equals("red") && r2 > r1) {
                return false;  // Regular red piece can only move up.
            } else if (c.getActionCommand().equals("red")) {
                return true;  // The move is legal.
            }
        } else if (turn == true) {
            if (c.getActionCommand().equals("king_black")) {
                return true;
            } else if (c.getActionCommand().equals("black") && r2 < r1) {
                return false;  // Regular black piece can only move down.
            } else if (c.getActionCommand().equals("black")) {
                return true;  // The move is legal.
            }
        }
        return false;
    }

    public String BoardPosition(int x, int y) {
        if (x == 0) {
            if (y == 1) {
                return "8B";
            } else if (y == 3) {
                return "8D";
            } else if (y == 5) {
                return "8F";
            } else if (y == 7) {
                return "8H";
            }
        } else if (x == 1) {
            if (y == 0) {
                return "7A";
            } else if (y == 2) {
                return "7C";
            } else if (y == 4) {
                return "7E";
            } else if (y == 6) {
                return "7G";
            }
        }
        if (x == 2) {
            if (y == 1) {
                return "6B";
            } else if (y == 3) {
                return "6D";
            } else if (y == 5) {
                return "6F";
            } else if (y == 7) {
                return "6H";
            }
        } else if (x == 3) {
            if (y == 0) {
                return "5A";
            } else if (y == 2) {
                return "5C";
            } else if (y == 4) {
                return "5E";
            } else if (y == 6) {
                return "5G";
            }
        }
        if (x == 4) {
            if (y == 1) {
                return "4B";
            } else if (y == 3) {
                return "4D";
            } else if (y == 5) {
                return "4F";
            } else if (y == 7) {
                return "4H";
            }
        } else if (x == 5) {
            if (y == 0) {
                return "3A";
            } else if (y == 2) {
                return "3C";
            } else if (y == 4) {
                return "3E";
            } else if (y == 6) {
                return "3G";
            }
        }
        if (x == 6) {
            if (y == 1) {
                return "2B";
            } else if (y == 3) {
                return "2D";
            } else if (y == 5) {
                return "2F";
            } else if (y == 7) {
                return "2H";
            }
        } else if (x == 7) {
            if (y == 0) {
                return "1A";
            } else if (y == 2) {
                return "1C";
            } else if (y == 4) {
                return "1E";
            } else if (y == 6) {
                return "1G";
            }
        }
        return "";
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
                messages.append("Red won\n");
                players[0].increaseWins();
                score.changeWins(players[0].getWins(), players[1].getWins());
            } else if (turn == false) {
                messages.append("Black won\n");
                players[1].increaseWins();
                score.changeWins(players[0].getWins(), players[1].getWins());
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new board();
        });
    }
}
