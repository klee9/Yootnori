package test;

import model.Board;
import model.Player;
import model.Position;
import model.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    private Token token1, token2, token3, token4;
    private Position initialPosition, newPosition;
    private Point2D.Double initialCoordinates;
    private Player player;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(4);
        player = new Player("Player1", 0, 4);
        initialPosition = board.getPositions().get(0);
        newPosition = board.getPositions().get(1);

        double startX = 650; // TODO: hard-coded -> relative values
        double startY = 250;

        int diameter = 24;
        int ownerId = player.getId();

        double x = startX + diameter * ownerId + ownerId * 8;
        double y = startY + diameter * ownerId + ownerId * 15;

        initialCoordinates = new Point2D.Double(x, y);

        token1 = new Token(0, player);
        token2 = new Token(1, player);
        token3 = new Token(2, player);
        token4 = new Token(3, player);

        List<Token> tokens = Arrays.asList(token1, token2, token3, token4);
        for (Token token : tokens) {
            token.setPosition(initialPosition);
        }
    }

    @Test
    void moveTo() {
        // token1 이동
        token1.moveTo(newPosition);
        assertEquals(newPosition.getId(), token1.getPosition().getId());

        // token1을 token2와 업은 후 2번째 칸으로 이동
        newPosition = board.getPositions().get(10);
        token1.stackWith(token2);
        token1.moveTo(newPosition);
        assertEquals(10, token2.getPosition().getId());
    }

    @Test
    void setFinished() {
        // token1이 완주했다고 가정
        token1.setFinished(true);
        assertTrue(token1.isFinished());

        // 업힌 말도 같이 완주되는지 체크
        token1.stackWith(token2);
        token1.setFinished(true);
        assertTrue(token2.isFinished());
    }

    @Test
    void reset() {
        // 말을 완주시킨 후, 상태 초기화
        token1.moveTo(board.getPositions().get(0));
        token1.setFinished(true);
        token1.reset();

        assertEquals(initialPosition.getId(), token1.getPosition().getId());
        assertFalse(token1.isFinished());

        // 업힌 말도 같이 초기화 되는지 체크
        token1.stackWith(token2);
        token1.reset();
        assertEquals(initialPosition.getId(), token2.getPosition().getId());
        assertFalse(token2.isFinished());
    }

    @Test
    void stackWith() {
        // token1, token2 업기
        token1.stackWith(token2);
        token3.stackWith(token4);

        // token1, token2의 업힌 말들 확인
        List<Token> stack = token1.getStackedTokens();
        assertTrue(stack.contains(token2));
        assertTrue(token2.getStackedTokens().contains(token1));

        // token1, token2를 제외한 말(token3)이 정상적으로 업히는지 확인
        token1.stackWith(token3);

        assertFalse(token1.getStackedTokens().contains(token1));
        assertTrue(token1.getStackedTokens().contains(token2));
        assertTrue(token1.getStackedTokens().contains(token3));
        assertTrue(token1.getStackedTokens().contains(token4));

        assertFalse(token2.getStackedTokens().contains(token2));
        assertTrue(token2.getStackedTokens().contains(token1));
        assertTrue(token2.getStackedTokens().contains(token3));
        assertTrue(token2.getStackedTokens().contains(token4));

        assertFalse(token3.getStackedTokens().contains(token3));
        assertTrue(token3.getStackedTokens().contains(token1));
        assertTrue(token3.getStackedTokens().contains(token2));
        assertTrue(token3.getStackedTokens().contains(token4));

        assertFalse(token4.getStackedTokens().contains(token4));
        assertTrue(token4.getStackedTokens().contains(token1));
        assertTrue(token4.getStackedTokens().contains(token2));
        assertTrue(token4.getStackedTokens().contains(token3));
    }

    @Test
    void isFinished() {
        // 초기 상태의 말은 완료되지 않은 상태여야 함
        assertFalse(token1.isFinished());

        // 완료 상태 변경 후 확인
        token1.setFinished(true);
        assertTrue(token1.isFinished());
    }

    @Test
    void testGetInitialCoordinates() {
        assertEquals(initialCoordinates, token1.getInitialCoordinates());
    }
}