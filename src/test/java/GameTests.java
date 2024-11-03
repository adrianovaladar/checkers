package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;

public class GameTests {

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
    public void testIsValidJumpRedCheckerOverBlack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
    public void testIsValidJumpBlackCheckerOverRed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
}