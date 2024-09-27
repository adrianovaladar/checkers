package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

public class Board extends JFrame implements MouseListener, ActionListener {
    private static final int BOARD_SIZE = 8;
    Checker[][] boardSquares = new Checker[BOARD_SIZE][BOARD_SIZE];

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    JLabel[] numbers = new JLabel[BOARD_SIZE + 1];
    JLabel[] characters = new JLabel[BOARD_SIZE];
    Player[] playersInGame = new Player[2];
    int redCheckers;
    int blackCheckers;
    SimpleEntry<Integer, Integer> positionCurrentChecker;
    boolean canJump;
    boolean canMove;
    boolean gameOver;
    Message message = new Message();
    JButton startGame = new JButton();
    JButton giveUp = new JButton();
    Score score = new Score();
    boolean turn = false; // false for red, true for black

    JLabel playerTurn = new JLabel();
    JMenuBar mainMenuBar = new JMenuBar();

    enum MoveType {NONE, MOVE, JUMP}

    Color lightOrange = new Color(249, 192, 102);
    Color darkBrown = new Color(158, 76, 16);

    private void processPlayerTurn() {
        turn = !turn;
        showPlayerTurn();
        checkMovesAndJumps();
        StringBuilder m = new StringBuilder();
        if (this.canJump) {
            m.append(playersInGame[bool2Int(turn)].getName())
                    .append(" is obliged to jump")
                    .append(System.lineSeparator());
            message.append(m.toString());
        }
        if (isGameOver()) {
            gameOver();
        }
    }

    private void showPlayerTurn() {
        playerTurn.setText(playersInGame[bool2Int(turn)].getName() + " Turn");
        if (!turn)
            playerTurn.setForeground(Color.RED);
        else
            playerTurn.setForeground(Color.BLACK);
    }

    private void setMenuBar() {
        JMenu menu = new JMenu("Menu");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Game developed by Adriano Valadar"));
        JMenuItem redPlayerName = new JMenuItem("Change " + playersInGame[0].getName() + " name");
        redPlayerName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert name:");
            String oldName = playersInGame[0].getName();
            playersInGame[0].setName(name + " (Red)");
            message.append(oldName + " player changed name to " + name + "\n");
            showPlayerTurn();
            score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());
        });
        JMenuItem blackPlayerName = new JMenuItem("Change " + playersInGame[1].getName() + " name");
        blackPlayerName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert name:");
            String oldName = playersInGame[1].getName();
            playersInGame[1].setName(name + " (Black)");
            message.append(oldName + " player changed name to " + name + "\n");
            showPlayerTurn();
            score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());

        });
        menu.add(redPlayerName);
        menu.add(blackPlayerName);
        menu.add(about);
        mainMenuBar.add(menu);
        this.setJMenuBar(mainMenuBar);

    }

    private void startGUI() {

        redCheckers = 12;
        blackCheckers = 12;
        this.setTitle("Checkers Game");
        playersInGame[0] = new Player("Red");
        playersInGame[1] = new Player("Black");
        score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());
        turn = false;
        showPlayerTurn();
        centerPanel.setLayout(new GridLayout(9, 9));
        this.addBoard();
        centerPanel.setPreferredSize(new Dimension(560, 560));
        centerPanel.setMinimumSize(new Dimension(560, 560));

        southPanel.add(score);
        northPanel.add(playerTurn);

        JScrollPane scroll = new JScrollPane(message);
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
                    b.setBackground(lightOrange);
                    boardSquares[i][j] = b;

                } else {
                    Checker c = new Checker(i, j);
                    boardSquares[i][j] = c;
                    boardSquares[i][j].addMouseListener(this);
                    boardSquares[i][j].setBackground(darkBrown);
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
        return !c.getName().equals(Checker.PieceType.NONE.name());
    }

    private boolean isGameOver() {
        return redCheckers == 0 || blackCheckers == 0 || (!this.canJump && !this.canMove);
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
        message.append("Game over\n");
        String name;
        if (redCheckers == 0 || !turn) {
            name = playersInGame[1].getName();
            playersInGame[1].increaseWins();
        } else {
            name = playersInGame[0].getName();
            playersInGame[0].increaseWins();
        }
        message.append(name + " won\n");
        score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());
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
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setName(Checker.PieceColour.NONE.name());
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand(Checker.PieceType.NONE.name());
        boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String name = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand(Checker.PieceType.NONE.name());
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName(Checker.PieceColour.NONE.name());
        boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
        message.append(playersInGame[bool2Int(turn)].getName() + " piece on " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " jumped on " + this.positionToText(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        if (!turn) blackCheckers--;
        else redCheckers--;
    }

    private void moveChecker(Checker c) {
        String name = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();

        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName(Checker.PieceColour.NONE.name());
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand(Checker.PieceType.NONE.name());
        boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);

        message.append(playersInGame[bool2Int(turn)].getName() + " moved piece from " + this.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " to " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
    }

    private void clear() {
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares.length; j++) {
                if (!(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1)) {
                    boardSquares[i][j].setBackground(darkBrown);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        Checker c = (Checker) m.getSource();
        this.canMove = false;
        if (c.getBackground().equals(Color.RED)) {
            jumpChecker(c);
            clear();
            this.canJump = false;

            if (isCurrentPlayerPieceAndTurn(c)) {
                if (canPerformAction(c, MoveType.JUMP)) {
                    this.canJump = true;
                    positionCurrentChecker = c.position;
                    boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setBackground(Color.GREEN.darker());
                    changeColour(c, MoveType.JUMP);
                }
            }

            if (!this.canJump && !turn) {
                Toolkit.getDefaultToolkit().beep();
                if (c.position.getKey() == 0) {
                    c.kingRed();
                    message.append(playersInGame[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
                }
                processPlayerTurn();
            } else if (!this.canJump) { //in this condition, we can consider that turn is true (black turn)
                Toolkit.getDefaultToolkit().beep();
                if (c.position.getKey() == 7) {
                    c.kingBlack();
                    message.append(playersInGame[bool2Int(turn)].getName() + " has a king in " + this.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
                }
                processPlayerTurn();
            }
        } else if (c.getBackground().equals(Color.YELLOW)) {
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
            processPlayerTurn();
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

    private void checkMovesAndJumps() {
        for (int row = 0; row < boardSquares.length; row++) {
            for (int col = 0; col < boardSquares.length; col++) {
                if (isValidSquare(row, col) && isCurrentPlayerPieceAndTurn(row, col)) {
                    checkForActions(row, col);
                    if (this.canJump) {
                        return;
                    }
                }
            }
        }
    }

    private boolean isValidSquare(int row, int col) {
        return !((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1));
    }

    private void checkForActions(int row, int col) {
        if (canPerformAction(boardSquares[row][col], MoveType.MOVE)) {
            this.canMove = true;
        }
        if (canPerformAction(boardSquares[row][col], MoveType.JUMP)) {
            this.canJump = true;
            this.canMove = false;
        }
    }

    private void showLegalMoves(Object m) {
        Checker c = (Checker) m;
        positionCurrentChecker = c.position;
        checkMovesAndJumps();
        if (this.hasChecker(c) && this.canJump) {
            boardSquares[c.position.getKey()][c.position.getValue()].setBackground(Color.GREEN.darker());
            if (canPerformAction(c, MoveType.JUMP)) {
                this.canJump = true;
                changeColour(c, MoveType.JUMP);
            }
        } else if (this.hasChecker(c) && !this.canJump && this.canMove) {
            boardSquares[c.position.getKey()][c.position.getValue()].setBackground(Color.GREEN.darker());
            positionCurrentChecker = c.position;
            if (canPerformAction(c, MoveType.MOVE)) {
                changeColour(c, MoveType.MOVE);
            }
        }
    }

    private void setBoardSquareColor(SimpleEntry<Integer, Integer> position, Color color) {
        boardSquares[position.getKey()][position.getValue()].setBackground(color);
    }

    private void changeColour(Checker c, MoveType m) {
        if (isCurrentPlayerPieceAndTurn(c)) {
            ArrayList<SimpleEntry<Integer, Integer>> positions = getSurroundingPositions(c, m);
            for (SimpleEntry<Integer, Integer> p : positions) {
                if (isPositionInvalid(p)) continue;
                if (m == MoveType.JUMP && isValidJump(c, p)) {
                    setBoardSquareColor(p, Color.RED);
                } else if (m == MoveType.MOVE) {
                    setBoardSquareColor(p, Color.YELLOW);
                }
            }
        }
    }

    private boolean canPerformAction(Checker c, MoveType m) {
        if (isCurrentPlayerPieceAndTurn(c)) {
            ArrayList<SimpleEntry<Integer, Integer>> positions = getSurroundingPositions(c, m);
            for (SimpleEntry<Integer, Integer> p : positions) {
                if (isPositionInvalid(p)) continue;
                if (m == MoveType.MOVE || m == MoveType.JUMP && isValidJump(c, p))
                    return true;
            }
        }
        return false;
    }

    private boolean isValidJump(Checker c, SimpleEntry<Integer, Integer> p) {
        return (!turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed());
    }

    private boolean isCurrentPlayerPieceAndTurn(int row, int col) {
        return (!turn && boardSquares[row][col].isRed()) || (turn && boardSquares[row][col].isBlack());
    }

    private boolean isCurrentRedPlayerPieceAndTurn(Checker c) {
        return !turn && c.isRed();
    }

    private boolean isCurrentBlackPlayerPieceAndTurn(Checker c) {
        return turn && c.isBlack();
    }

    private boolean isCurrentPlayerPieceAndTurn(Checker c) {
        return !turn && c.isRed() || turn && c.isBlack();
    }

    private boolean isPositionInvalid(SimpleEntry<Integer, Integer> p) {
        boolean positionOffBoard = p.getKey() < 0 || p.getKey() >= boardSquares.length || p.getValue() < 0 || p.getValue() >= boardSquares.length;
        boolean positionContainPiece = !boardSquares[p.getKey()][p.getValue()].getName().equals(Checker.PieceType.NONE.name());
        return positionOffBoard || positionContainPiece;
    }

    private ArrayList<SimpleEntry<Integer, Integer>> getSurroundingPositions(Checker c, MoveType moveType) {
        ArrayList<SimpleEntry<Integer, Integer>> positions = new ArrayList<>();
        if (isCurrentRedPlayerPieceAndTurn(c) || c.isKing()) {
            positions.add(new SimpleEntry<>(c.position.getKey() - moveType.ordinal(), c.position.getValue() - moveType.ordinal()));
            positions.add(new SimpleEntry<>(c.position.getKey() - moveType.ordinal(), c.position.getValue() + moveType.ordinal()));
        }
        if (isCurrentBlackPlayerPieceAndTurn(c) || c.isKing()) {
            positions.add(new SimpleEntry<>(c.position.getKey() + moveType.ordinal(), c.position.getValue() + moveType.ordinal()));
            positions.add(new SimpleEntry<>(c.position.getKey() + moveType.ordinal(), c.position.getValue() - moveType.ordinal()));
        }
        positions.removeIf(position -> position.getKey() < 0 || position.getValue() < 0 || position.getValue() >= boardSquares.length || position.getKey() >= boardSquares.length);
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
            message.append("Game started. Select a checker to view the options\n");
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
