package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;

class GameTests {

    private Game game;
    private Checker redChecker;
    private Checker blackChecker;

    @BeforeEach
    public void setUp() {
        game = new Game(false);
        game.board.create();

        redChecker = new Checker();
        redChecker.redMan();
        blackChecker = new Checker();
        blackChecker.blackMan();
    }

    public void setBoardSquaresNull() {
        for (int i = 0; i < game.board.boardSquares.length; i++) {
            for (int j = 0; j < game.board.boardSquares.length; j++) {
                game.board.boardSquares[i][j].setNone();
            }
        }
    }

    @Test
    void isValidJumpRedCheckerOverBlack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[4][5].redMan();
        game.board.boardSquares[4][5].setPosition(new SimpleEntry<>(4, 5));
        game.board.boardSquares[3][4].blackMan();
        game.board.boardSquares[3][4].setPosition(new SimpleEntry<>(3, 4));
        game.board.boardSquares[2][3] = new Checker();
        game.board.boardSquares[2][3].setPosition(new SimpleEntry<>(2, 3));
        game.turn = false;
        SimpleEntry<Integer, Integer> targetPosition = new SimpleEntry<>(2, 3);
        Method isValidJump = Game.class.getDeclaredMethod("isValidJump", Checker.class, SimpleEntry.class);
        isValidJump.setAccessible(true);
        boolean result = (boolean) isValidJump.invoke(game, game.board.boardSquares[4][5], targetPosition);
        assertTrue(result, "Red checker should be able to jump over black checker");
    }

    @Test
    void isInvalidJumpRedCheckerOverBlack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[4][5].redMan();
        game.board.boardSquares[4][5].setPosition(new SimpleEntry<>(4, 5));
        game.board.boardSquares[3][4].blackMan();
        game.board.boardSquares[3][4].setPosition(new SimpleEntry<>(3, 4));
        game.board.boardSquares[2][3].blackMan();
        game.board.boardSquares[2][3].setPosition(new SimpleEntry<>(2, 3));
        game.turn = false;
        SimpleEntry<Integer, Integer> targetPosition = new SimpleEntry<>(2, 3);
        Method isValidJump = Game.class.getDeclaredMethod("isValidJump", Checker.class, SimpleEntry.class);
        isValidJump.setAccessible(true);
        boolean result = (boolean) isValidJump.invoke(game, game.board.boardSquares[4][5], targetPosition);
        assertFalse(result, "Red checker should not be able to jump over black checker");
    }

    @Test
    void isValidJumpBlackCheckerOverRed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].blackMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        game.board.boardSquares[4][3].redMan();
        game.board.boardSquares[4][3].setPosition(new SimpleEntry<>(4, 3));
        game.board.boardSquares[3][4] = new Checker();
        game.board.boardSquares[5][4].setPosition(new SimpleEntry<>(5, 4));
        game.turn = true;
        SimpleEntry<Integer, Integer> targetPosition = new SimpleEntry<>(5, 4);
        Method isValidJump = Game.class.getDeclaredMethod("isValidJump", Checker.class, SimpleEntry.class);
        isValidJump.setAccessible(true);
        boolean result = (boolean) isValidJump.invoke(game, game.board.boardSquares[3][2], targetPosition);
        assertTrue(result, "Black checker should be able to jump over red checker");
    }

    @Test
    void isInvalidJumpBlackCheckerOverRed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].blackMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        game.board.boardSquares[4][3].redMan();
        game.board.boardSquares[4][3].setPosition(new SimpleEntry<>(4, 3));
        game.board.boardSquares[5][4].blackMan();
        game.board.boardSquares[5][4].setPosition(new SimpleEntry<>(5, 4));
        game.turn = true;
        SimpleEntry<Integer, Integer> targetPosition = new SimpleEntry<>(5, 4);
        Method isValidJump = Game.class.getDeclaredMethod("isValidJump", Checker.class, SimpleEntry.class);
        isValidJump.setAccessible(true);
        boolean result = (boolean) isValidJump.invoke(game, game.board.boardSquares[3][2], targetPosition);
        assertFalse(result, "Black checker should be able to jump over red checker");
    }

    @Test
    void isCurrentPlayerPieceAndTurnBlack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].blackMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        Method isCurrentPlayerPieceAndTurn = Game.class.getDeclaredMethod("isCurrentPlayerPieceAndTurn", int.class, int.class);
        isCurrentPlayerPieceAndTurn.setAccessible(true);
        game.turn = true;
        boolean result = (boolean) isCurrentPlayerPieceAndTurn.invoke(game, 3, 2);
        assertTrue(result, "Game turn is black and the piece is black");
    }

    @Test
    void isCurrentPlayerPieceAndTurnRed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].redMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        Method isCurrentPlayerPieceAndTurn = Game.class.getDeclaredMethod("isCurrentPlayerPieceAndTurn", int.class, int.class);
        isCurrentPlayerPieceAndTurn.setAccessible(true);
        game.turn = false;
        boolean result = (boolean) isCurrentPlayerPieceAndTurn.invoke(game, 3, 2);
        assertTrue(result, "Game turn is red and the piece is red");
    }

    @Test
    void isNotCurrentPlayerPieceAndTurn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].blackMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        Method isCurrentPlayerPieceAndTurn = Game.class.getDeclaredMethod("isCurrentPlayerPieceAndTurn", int.class, int.class);
        isCurrentPlayerPieceAndTurn.setAccessible(true);
        game.turn = false;
        boolean result = (boolean) isCurrentPlayerPieceAndTurn.invoke(game, 3, 2);
        assertFalse(result, "Game turn is black and the piece is black");
    }

    @Test
    void isCurrentRedPlayerPieceAndTurnRed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].redMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        Method isCurrentRedPlayerPieceAndTurn = Game.class.getDeclaredMethod("isCurrentRedPlayerPieceAndTurn", Checker.class);
        isCurrentRedPlayerPieceAndTurn.setAccessible(true);
        game.turn = false;
        boolean result = (boolean) isCurrentRedPlayerPieceAndTurn.invoke(game, game.board.boardSquares[3][2]);
        assertTrue(result, "Game turn is red and the piece is red");
    }

    @Test
    void isCurrentRedPlayerPieceAndTurnBlack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setBoardSquaresNull();
        game.board.boardSquares[3][2].blackMan();
        game.board.boardSquares[3][2].setPosition(new SimpleEntry<>(3, 2));
        Method isCurrentRedPlayerPieceAndTurn = Game.class.getDeclaredMethod("isCurrentRedPlayerPieceAndTurn", Checker.class);
        isCurrentRedPlayerPieceAndTurn.setAccessible(true);
        game.turn = false;
        boolean result = (boolean) isCurrentRedPlayerPieceAndTurn.invoke(game, game.board.boardSquares[3][2]);
        assertFalse(result, "Game turn is red and the piece is red");
    }
}
