
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.event.*;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class board extends JFrame implements MouseListener, ActionListener {

    JButton[][] BoardSquares = new JButton[8][8];

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    JLabel[] numbers = new JLabel[9];
    JLabel[] characters = new JLabel[8];
    Image img;
    String player;
    int i, j, r, c, red_checkers, black_checkers, wins_red, wins_black;
    boolean canJump, canMove, gameOver;
    int pos1i2, pos1j2, pos2i2, pos2j2, pos3i2, pos3j2, pos4i2, pos4j2, pos1i3, pos1j3, pos2i3, pos2j3, pos3i3, pos3j3, pos4i3, pos4j3;
    PlayerTurn playerturn = new PlayerTurn();
    Messages messages = new Messages();
    JButton startGame = new JButton();
    JButton giveUp = new JButton();
    wins score = new wins();

    public void startGUI() {

        red_checkers = 12;
        black_checkers = 12;
        this.setTitle("Checkers Game - Adriano Valadar");
        this.player = "red";
        centerPanel.setLayout(new GridLayout(9, 9));
        this.addBoard();
        centerPanel.setPreferredSize(new Dimension(560, 560));
        centerPanel.setMinimumSize(new Dimension(560, 560));

        southPanel.add(score);
        northPanel.add(playerturn);

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
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

    }

    public void addBoard() {
        this.gameOver = false;
        numbers[0] = new JLabel("");
        characters[0] = new JLabel("A", SwingConstants.CENTER);
        characters[1] = new JLabel("B", SwingConstants.CENTER);
        characters[2] = new JLabel("C", SwingConstants.CENTER);
        characters[3] = new JLabel("D", SwingConstants.CENTER);
        characters[4] = new JLabel("E", SwingConstants.CENTER);
        characters[5] = new JLabel("F", SwingConstants.CENTER);
        characters[6] = new JLabel("G", SwingConstants.CENTER);
        characters[7] = new JLabel("H", SwingConstants.CENTER);

        for (int i = 0; i < BoardSquares.length; i++) {
            JLabel j1 = new JLabel(8 - i + "", SwingConstants.CENTER);
            centerPanel.add(j1);
            for (int j = 0; j < BoardSquares.length; j++) {
                JButton b = new JButton();
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

    public void jumpChecker(checkers c, int row, int col) {

        this.r = (this.i + row) / 2;
        this.c = (this.j + col) / 2;
        BoardSquares[this.r][this.c].setActionCommand("");
        BoardSquares[this.r][this.c].setIcon(null);
        if (this.player.equals("red") && BoardSquares[this.i][this.j].getActionCommand().equals("red")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("red");
            messages.append("Red piece on " + this.BoardPosition(this.i, this.j) + " jumped on " + this.BoardPosition(this.r, this.c) + " and moved to " + this.BoardPosition(c.i, c.j) + "\n");
            black_checkers--;
            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("red") && BoardSquares[this.i][this.j].getActionCommand().equals("king_red")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("king_red");
            messages.append("Red king on " + this.BoardPosition(this.i, this.j) + " jumped on " + this.BoardPosition(this.r, this.c) + " and moved to " + this.BoardPosition(c.i, c.j) + "\n");
            black_checkers--;
            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_king.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("black") && BoardSquares[this.i][this.j].getActionCommand().equals("black")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("black");
            messages.append("Black piece on " + this.BoardPosition(this.i, this.j) + " jumped on " + this.BoardPosition(this.r, this.c) + " and moved to " + this.BoardPosition(c.i, c.j) + "\n");
            red_checkers--;
            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/black_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("black") && BoardSquares[this.i][this.j].getActionCommand().equals("king_black")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("king_black");
            messages.append("Black king on " + this.BoardPosition(this.i, this.j) + " jumped on " + this.BoardPosition(this.r, this.c) + " and moved to " + this.BoardPosition(c.i, c.j) + "\n");
            red_checkers--;
            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/black_king.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        if (red_checkers == 0) {
            messages.append("Game over\n");
            messages.append("Black won\n");
            wins_black++;
            score.changeWins(wins_red, wins_black);
            this.startGame.setEnabled(true);
            this.giveUp.setEnabled(false);
            this.gameOver = true;

            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {
                    BoardSquares[i][j].removeMouseListener(this);

                }
            }
        } else if (black_checkers == 0) {
            messages.append("Game over\n");
            messages.append("Red won\n");
            wins_red++;
            score.changeWins(wins_red, wins_black);
            this.startGame.setEnabled(true);
            this.giveUp.setEnabled(false);
            this.gameOver = true;
            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {
                    BoardSquares[i][j].removeMouseListener(this);

                }
            }
        }

    }

    /* public void moveChecker(checkers c) {
        BoardSquares[this.i][this.j].setActionCommand("");
        BoardSquares[this.i][this.j].setIcon(null);
        if (this.player.equals("red")) {
            BoardSquares[c.i][c.j].setActionCommand("red");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("black")) {
            BoardSquares[c.i][c.j].setActionCommand("black");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/black_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }*/
    public void moveChecker(checkers c) {

        if (this.player.equals("red") && BoardSquares[this.i][this.j].getActionCommand().equals("red")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("red");
            messages.append("Red moved piece from " + this.BoardPosition(this.i, this.j) + " to " + this.BoardPosition(c.i, c.j) + "\n");
            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("red") && BoardSquares[this.i][this.j].getActionCommand().equals("king_red")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("king_red");
            messages.append("Red moved king from " + this.BoardPosition(this.i, this.j) + " to " + this.BoardPosition(c.i, c.j) + "\n");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_king.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }

        } else if (this.player.equals("black") && BoardSquares[this.i][this.j].getActionCommand().equals("black")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("black");
            messages.append("Black moved piece from " + this.BoardPosition(this.i, this.j) + " to " + this.BoardPosition(c.i, c.j) + "\n");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/black_men.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (this.player.equals("black") && BoardSquares[this.i][this.j].getActionCommand().equals("king_black")) {
            BoardSquares[this.i][this.j].setActionCommand("");
            BoardSquares[this.i][this.j].setIcon(null);
            BoardSquares[c.i][c.j].setActionCommand("king_black");
            messages.append("Black moved piece from " + this.BoardPosition(this.i, this.j) + " to " + this.BoardPosition(c.i, c.j) + "\n");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/black_king.png"));
                BoardSquares[c.i][c.j].setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        checkers c = (checkers) m.getSource();
        this.canMove = false;
        if (c.getBackground().equals(new Color(255, 0, 0))) {
            jumpChecker(c, c.i, c.j);
            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {

                    if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {

                    } else {
                        BoardSquares[i][j].setBackground(new Color(158, 76, 16));

                    }
                }
            }
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
            if (this.player.equals("red") && BoardSquares[c.i][c.j].getActionCommand().equals("red") || this.player.equals("red") && BoardSquares[c.i][c.j].getActionCommand().equals("king_red")) {
                if (canJump(c, this.player, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));
                }
            } else if (this.player.equals("black") && BoardSquares[c.i][c.j].getActionCommand().equals("black") || this.player.equals("black") && BoardSquares[c.i][c.j].getActionCommand().equals("king_black")) {
                if (canJump(c, this.player, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    this.i = c.i;
                    this.j = c.j;
                    BoardSquares[i][j].setBackground(new Color(0, 153, 0));
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));
                }
            }

            if (this.canJump == false && this.player.equals("red")) {
                this.player = "black";
                Toolkit.getDefaultToolkit().beep();
                if (c.i == 0) {
                    c.king_red();
                    messages.append("Red has a king in " + this.BoardPosition(c.i, c.j) + "\n");
                }

            } else if (this.canJump == false && this.player.equals("black")) {
                this.player = "red";
                Toolkit.getDefaultToolkit().beep();

                if (c.i == 7) {
                    c.king_black();
                    messages.append("Black has a king in " + this.BoardPosition(c.i, c.j) + "\n");

                }
            }
        } else if (c.getBackground().equals(new Color(255, 255, 0))) {
            moveChecker(c);
            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {

                    if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {

                    } else {
                        BoardSquares[i][j].setBackground(new Color(158, 76, 16));

                    }
                }
            }
            if (this.player.equals("red")) {
                if (c.i == 0) {
                    c.king_red();
                }
                this.player = "black";
                Toolkit.getDefaultToolkit().beep();

            } else {
                if (c.i == 7) {

                    c.king_black();
                }
                this.player = "red";
                Toolkit.getDefaultToolkit().beep();

            }
        } else {
            for (int i = 0; i < BoardSquares.length; i++) {
                for (int j = 0; j < BoardSquares.length; j++) {

                    if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {

                    } else {
                        BoardSquares[i][j].setBackground(new Color(158, 76, 16));

                    }
                }
            }
            showLegalMoves(m.getSource());
        }
        playerturn.changePlayerTurn(this.player);

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
                if (this.player.equals("red") && BoardSquares[row][col].getActionCommand().equals("red") || this.player.equals("red") && BoardSquares[row][col].getActionCommand().equals("king_red")) {
                    if (canMove(c, this.player, row, col, this.pos1i2, this.pos1j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, this.player, row, col, this.pos2i2, this.pos2j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, this.player, row, col, this.pos3i2, this.pos3j2)) {
                        this.canMove = true;
                    }
                    if (canMove(c, this.player, row, col, this.pos4i2, this.pos4j2)) {
                        this.canMove = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                        this.canJump = true;
                    }
                } else if (this.player.equals("black") && BoardSquares[row][col].getActionCommand().equals("black") || this.player.equals("black") && BoardSquares[row][col].getActionCommand().equals("king_black")) {

                    if (canMove(c, this.player, row, col, this.pos1i2, this.pos1j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, this.player, row, col, this.pos2i2, this.pos2j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, this.player, row, col, this.pos3i2, this.pos3j2)) {
                        this.canMove = true;

                    }
                    if (canMove(c, this.player, row, col, this.pos4i2, this.pos4j2)) {
                        this.canMove = true;

                    }

                    if (canJump(c, this.player, row, col, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                        this.canJump = true;
                    }
                    if (canJump(c, this.player, row, col, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                        this.canJump = true;
                    }
                }
            }
        }

        if (this.hasChecker(c) && this.canJump == true) {
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
            if (this.player.equals("red") && BoardSquares[c.i][c.j].getActionCommand().equals("red") || this.player.equals("red") && BoardSquares[c.i][c.j].getActionCommand().equals("king_red")) {
                if (canJump(c, this.player, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));
                }
                if (canJump(c, this.player, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, this.player, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, this.player, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));

                }
            } else if (this.player.equals("black") && BoardSquares[c.i][c.j].getActionCommand().equals("black") || this.player.equals("black") && BoardSquares[c.i][c.j].getActionCommand().equals("king_black")) {
                if (canJump(c, this.player, c.i, c.j, this.pos1i2, this.pos1j2, this.pos1i3, this.pos1j3)) {
                    this.canJump = true;
                    BoardSquares[pos1i3][pos1j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, this.player, c.i, c.j, this.pos2i2, this.pos2j2, this.pos2i3, this.pos2j3)) {
                    this.canJump = true;
                    BoardSquares[pos2i3][pos2j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, this.player, c.i, c.j, this.pos3i2, this.pos3j2, this.pos3i3, this.pos3j3)) {
                    this.canJump = true;
                    BoardSquares[pos3i3][pos3j3].setBackground(new Color(255, 0, 0));

                }
                if (canJump(c, this.player, c.i, c.j, this.pos4i2, this.pos4j2, this.pos4i3, this.pos4j3)) {
                    this.canJump = true;
                    BoardSquares[pos4i3][pos4j3].setBackground(new Color(255, 0, 0));

                }
            }
            //}
        } else if (this.hasChecker(c) && this.canJump == false && this.canMove == true) {
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
            if (canMove(c, this.player, c.i, c.j, this.pos1i2, this.pos1j2) == true) {
                BoardSquares[pos1i2][pos1j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, this.player, c.i, c.j, this.pos2i2, this.pos2j2) == true) {
                BoardSquares[pos2i2][pos2j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, this.player, c.i, c.j, this.pos3i2, this.pos3j2) == true) {
                BoardSquares[pos3i2][pos3j2].setBackground(new Color(255, 255, 0));

            }
            if (canMove(c, this.player, c.i, c.j, this.pos4i2, this.pos4j2) == true) {
                BoardSquares[pos4i2][pos4j2].setBackground(new Color(255, 255, 0));

            }
        } else if (this.hasChecker(c) && this.canJump == false && this.canMove == false) {
            if (this.player.equals("red") && c.getActionCommand().equals("red") || this.player.equals("red") && c.getActionCommand().equals("king_red") || this.player.equals("black") && c.getActionCommand().equals("black") || this.player.equals("black") && c.getActionCommand().equals("king_black")) {

                messages.append("Game over\n");
                if (this.player.equals("black")) {
                    messages.append("Red won\n");
                    wins_red++;
                    score.changeWins(wins_red, wins_black);
                } else if (this.player.equals("red")) {
                    messages.append("Black won\n");
                    wins_black++;
                    score.changeWins(wins_red, wins_black);
                }
                this.startGame.setEnabled(true);
                this.giveUp.setEnabled(false);
                this.gameOver = true;
                for (int i = 0; i < BoardSquares.length; i++) {
                    for (int j = 0; j < BoardSquares.length; j++) {
                        BoardSquares[i][j].removeMouseListener(this);

                    }
                }
            }
        }
    }

    public boolean canJump(checkers c, String player, int r1, int c1, int r2, int c2, int r3, int c3) {

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
            return false;  // (r3,c3) is off the board.
        } else if (!BoardSquares[r3][c3].getActionCommand().equals("")) {

            return false;  // (r3,c3) already contains a piece.
        } else if (player.equals("red")) {
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

    public boolean canMove(checkers c, String player, int r1, int c1, int r2, int c2) {
        //modificar para damas
        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8) {
            return false;  // (r2,c2) is off the board.
        } else if (BoardSquares[r2][c2].getActionCommand().equals("red") || BoardSquares[r2][c2].getActionCommand().equals("black") || BoardSquares[r2][c2].getActionCommand().equals("king_black") || BoardSquares[r2][c2].getActionCommand().equals("king_red")) {
            return false;  // (r2,c2) already contains a piece.
        } else if (player.equals("red")) {
            if (c.getActionCommand().equals("king_red")) {
                return true;
            } else if (c.getActionCommand().equals("red") && r2 > r1) {
                return false;  // Regular red piece can only move up.
            } else if (c.getActionCommand().equals("red")) {
                return true;  // The move is legal.
            }
        } else if (player.equals("black")) {
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
            this.player = "red";
            playerturn.changePlayerTurn("red");

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
            if (this.player.equals("black")) {
                messages.append("Red won\n");
                wins_red++;
                score.changeWins(wins_red, wins_black);
            } else if (this.player.equals("red")) {
                messages.append("Black won\n");
                wins_black++;
                score.changeWins(wins_red, wins_black);
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            board content;
            content = new board();
        });
    }
}
