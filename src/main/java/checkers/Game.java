package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

public class Game extends JFrame implements MouseListener, ActionListener {

    Board board = new Board(this); // center panel
    JPanel northPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    Player[] playersInGame = new Player[2];
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
        redPlayerName.addActionListener(e -> changePlayerName(0));
        JMenuItem blackPlayerName = new JMenuItem("Change " + playersInGame[1].getName() + " name");
        blackPlayerName.addActionListener(e -> changePlayerName(1));
        menu.add(redPlayerName);
        menu.add(blackPlayerName);
        menu.add(about);
        mainMenuBar.add(menu);
        this.setJMenuBar(mainMenuBar);
    }

    private void changePlayerName(int playerIndex) {
        String name = JOptionPane.showInputDialog("Insert name:");
        String oldName = playersInGame[playerIndex].getName();
        playersInGame[playerIndex].setName(name);
        message.append(oldName + " changed name to " + name + System.lineSeparator());
        showPlayerTurn();
        score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());
    }

    private void startGUI() {
        this.setTitle("Checkers Game");
        playersInGame[0] = new Player("Red");
        playersInGame[1] = new Player("Black");
        score.show(playersInGame[0].getName(), playersInGame[1].getName(), playersInGame[0].getWins(), playersInGame[1].getWins());
        turn = false;
        showPlayerTurn();
        board.create();
        board.countPieces();

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
        this.add(board, BorderLayout.CENTER);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    public Game(boolean enableGraphics) {
        if (!enableGraphics) return;
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

    private boolean hasChecker(Checker c) {
        return !c.getName().equals(Checker.PieceType.NONE.name());
    }

    private boolean isGameOver() {
        return board.redCheckers == 0 || board.blackCheckers == 0 || (!this.canJump && !this.canMove);
    }

    private void gameOver() {
        message.append("Game over\n");
        String name;
        if (board.redCheckers == 0 || !turn) {
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

        board.removeCheckerActions();
    }

    private void jumpChecker(Checker c) {
        int jumpedCheckerRow;
        int jumpedCheckerColumn;
        jumpedCheckerRow = (positionCurrentChecker.getKey() + c.position.getKey()) / 2;
        jumpedCheckerColumn = (positionCurrentChecker.getValue() + c.position.getValue()) / 2;
        board.boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setName(Checker.PieceColour.NONE.name());
        board.boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setActionCommand(Checker.PieceType.NONE.name());
        board.boardSquares[jumpedCheckerRow][jumpedCheckerColumn].setIcon(null);

        String name = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();
        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand(Checker.PieceType.NONE.name());
        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);
        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName(Checker.PieceColour.NONE.name());
        board.boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        board.boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        board.boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
        message.append(playersInGame[bool2Int(turn)].getName() + " piece on " + board.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " jumped on " + board.positionToText(jumpedCheckerRow, jumpedCheckerColumn) + " and moved to " + board.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        board.countPieces();
    }

    private void moveChecker(Checker c) {
        String name = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getName();
        String action = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getActionCommand();
        Icon icon = board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].getIcon();

        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setName(Checker.PieceColour.NONE.name());
        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setActionCommand(Checker.PieceType.NONE.name());
        board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setIcon(null);

        message.append(playersInGame[bool2Int(turn)].getName() + " moved piece from " + board.positionToText(positionCurrentChecker.getKey(), positionCurrentChecker.getValue()) + " to " + board.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        board.boardSquares[c.position.getKey()][c.position.getValue()].setName(name);
        board.boardSquares[c.position.getKey()][c.position.getValue()].setActionCommand(action);
        board.boardSquares[c.position.getKey()][c.position.getValue()].setIcon(icon);
    }

    private void handleKingPromotion(Checker c) {
        boolean setKing = false;
        if (isCurrentRedPlayerPieceAndTurn(c) && c.isMan() && c.position.getKey() == 0) {
            c.setKing(Checker.PieceColour.RED);
            setKing = true;
        } else if (isCurrentBlackPlayerPieceAndTurn(c) && c.isMan() && c.position.getKey() == Board.BOARD_SIZE - 1) {
            c.setKing(Checker.PieceColour.BLACK);
            setKing = true;
        }
        if (setKing) {
            message.append(playersInGame[bool2Int(turn)].getName() + " has a king in " + board.positionToText(c.position.getKey(), c.position.getValue()) + "\n");
        }
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        Checker c = (Checker) m.getSource();
        this.canMove = false;
        if (c.getBackground().equals(Color.RED)) {
            jumpChecker(c);
            board.clear();
            this.canJump = false;

            if (isCurrentPlayerPieceAndTurn(c) && canPerformAction(c, MoveType.JUMP)) {
                this.canJump = true;
                positionCurrentChecker = c.position;
                board.boardSquares[positionCurrentChecker.getKey()][positionCurrentChecker.getValue()].setBackground(Color.GREEN.darker());
                changeColour(c, MoveType.JUMP);
            }

            if (!this.canJump) {
                Toolkit.getDefaultToolkit().beep();
                handleKingPromotion(c);
                processPlayerTurn();
            }
        } else if (c.getBackground().equals(Color.YELLOW)) {
            moveChecker(c);
            board.clear();
            handleKingPromotion(c);
            processPlayerTurn();
            Toolkit.getDefaultToolkit().beep();
        } else {
            board.clear();
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
        for (int row = 0; row < board.boardSquares.length; row++) {
            for (int col = 0; col < board.boardSquares.length; col++) {
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
        if (canPerformAction(board.boardSquares[row][col], MoveType.MOVE)) {
            this.canMove = true;
        }
        if (canPerformAction(board.boardSquares[row][col], MoveType.JUMP)) {
            this.canJump = true;
            this.canMove = false;
        }
    }

    private void showLegalMoves(Object m) {
        Checker c = (Checker) m;
        if (!isCurrentPlayerPieceAndTurn(c)) {
            return;
        }
        positionCurrentChecker = c.position;
        checkMovesAndJumps();
        if (this.hasChecker(c) && this.canJump) {
            board.boardSquares[c.position.getKey()][c.position.getValue()].setBackground(Color.GREEN.darker());
            if (canPerformAction(c, MoveType.JUMP)) {
                this.canJump = true;
                changeColour(c, MoveType.JUMP);
            }
        } else if (this.hasChecker(c) && !this.canJump && this.canMove) {
            board.boardSquares[c.position.getKey()][c.position.getValue()].setBackground(Color.GREEN.darker());
            positionCurrentChecker = c.position;
            if (canPerformAction(c, MoveType.MOVE)) {
                changeColour(c, MoveType.MOVE);
            }
        }
    }

    private void setBoardSquareColor(SimpleEntry<Integer, Integer> position, Color color) {
        board.boardSquares[position.getKey()][position.getValue()].setBackground(color);
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
        return ((!turn && board.boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isBlack()) || (turn && board.boardSquares[(c.position.getKey() + p.getKey()) / 2][(c.position.getValue() + p.getValue()) / 2].isRed()))
                && board.boardSquares[p.getKey()][p.getValue()].isNone();
    }

    private boolean isCurrentPlayerPieceAndTurn(int row, int col) {
        return (!turn && board.boardSquares[row][col].isRed()) || (turn && board.boardSquares[row][col].isBlack());
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
        boolean positionOffBoard = p.getKey() < 0 || p.getKey() >= board.boardSquares.length || p.getValue() < 0 || p.getValue() >= board.boardSquares.length;
        boolean positionContainPiece = !board.boardSquares[p.getKey()][p.getValue()].getName().equals(Checker.PieceType.NONE.name());
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
        positions.removeIf(position -> position.getKey() < 0 || position.getValue() < 0 || position.getValue() >= board.boardSquares.length || position.getKey() >= board.boardSquares.length);
        return positions;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (this.gameOver) {
            board.removeAll();
            board.create();
            board.revalidate();
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
        SwingUtilities.invokeLater(() -> new Game(true));
    }
}
