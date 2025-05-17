package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleSetTest {

    private int square = 4;
    private int pentagon = 5;
    private int hexagon = 6;

    private Board board1;
    private Board board2;
    private Board board3;

    private RuleSet rulesSquare;
    private RuleSet rulesPentagon;
    private RuleSet rulesHexagon;

    private Player owner1;
    private Player owner2;

    private Token token1;
    private Token token2;
    private Token token3;
    private Token token4;

    private Position p1, p2, p3;

    List<Token> tokens;

    @BeforeEach
    void setUp() {
        board1 = new Board(square);
        board2 = new Board(pentagon);
        board3 = new Board(hexagon);

        rulesSquare = new RuleSet(square);
        rulesPentagon = new RuleSet(pentagon);
        rulesHexagon = new RuleSet(hexagon);

        owner1 = new Player("Player 1", 0, 2);
        owner2 = new Player("Player 2", 1, 2);

        token1 = new Token(0, owner1);
        token2 = new Token(1, owner1);
        token3 = new Token(2, owner2);
        token4 = new Token(3, owner2);

        tokens = List.of(token1, token2, token3, token4);

        for (Token t : tokens) {
            t.setPosition(board1.getStartPosition());
        }
    }

    @Test
    void testCheckMoveSqaure() { // 사각형 보드
        // '개'로 0 -> 2 이동 가능한지 체크
        assertTrue(rulesSquare.checkMove(s(0), s(2), TossResult.GAE));

        // '모'로 나갈 수 있는지 체크
        assertTrue(rulesSquare.checkMove(s(17), s(0), TossResult.MO));

        // 코너 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesSquare.checkMove(s(5), s(22), TossResult.DO));
        assertTrue(rulesSquare.checkMove(s(5), s(6), TossResult.DO));

        assertTrue(rulesSquare.checkMove(s(10), s(11), TossResult.DO));
        assertTrue(rulesSquare.checkMove(s(10), s(24), TossResult.DO));

        // 중앙 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesSquare.checkMove(s(28), s(21), TossResult.DO));
        assertTrue(rulesSquare.checkMove(s(28), s(27), TossResult.DO));
    }

    @Test
    void testCheckMovePentagon() {
        assertTrue(rulesPentagon.checkMove(p(0), p(2), TossResult.GAE));

        // '모'로 나갈 수 있는지 체크
        assertTrue(rulesPentagon.checkMove(p(35), s(0), TossResult.MO));

        // 코너 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesPentagon.checkMove(p(5), p(27), TossResult.DO));
        assertTrue(rulesPentagon.checkMove(p(5), p(6), TossResult.DO));

        assertTrue(rulesPentagon.checkMove(p(10), p(11), TossResult.DO));
        assertTrue(rulesPentagon.checkMove(p(10), p(29), TossResult.DO));

        assertTrue(rulesPentagon.checkMove(p(15), p(16), TossResult.DO));
        assertTrue(rulesPentagon.checkMove(p(15), p(31), TossResult.DO));

        // 중앙 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesPentagon.checkMove(p(35), p(26), TossResult.DO));
        assertFalse(rulesPentagon.checkMove(p(35), p(34), TossResult.DO));
    }

    @Test
    void testCheckMoveHexagon() {
        assertTrue(rulesHexagon.checkMove(h(0), h(2), TossResult.GAE));
        assertTrue(rulesHexagon.checkMove(h(33), h(41), TossResult.GAE));

        // '모'로 나갈 수 있는지 체크
        assertTrue(rulesHexagon.checkMove(h(42), s(0), TossResult.MO));

        // 코너 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesHexagon.checkMove(h(5), h(6), TossResult.DO));
        assertTrue(rulesHexagon.checkMove(h(5), h(32), TossResult.DO));

        assertTrue(rulesHexagon.checkMove(h(10), h(11), TossResult.DO));
        assertTrue(rulesHexagon.checkMove(h(10), h(34), TossResult.DO));

        assertTrue(rulesHexagon.checkMove(h(15), h(16), TossResult.DO));
        assertTrue(rulesHexagon.checkMove(h(15), h(36), TossResult.DO));

        assertTrue(rulesHexagon.checkMove(h(20), h(21), TossResult.DO));
        assertTrue(rulesHexagon.checkMove(h(20), h(38), TossResult.DO));

        // 중앙 노드에서 원하는 방향으로 갈 수 있는지 체크
        assertTrue(rulesHexagon.checkMove(h(42), h(31), TossResult.DO));
        assertFalse(rulesHexagon.checkMove(h(42), h(41), TossResult.DO));
        assertFalse(rulesHexagon.checkMove(h(42), h(37), TossResult.DO));
        assertFalse(rulesHexagon.checkMove(h(42), h(39), TossResult.DO));
    }

    @Test
    void testCanStack() {
        // Player1의 말을 2번째 노드로 이동
        token1.moveTo(board1.getPositions().get(2));
        token2.moveTo(board1.getPositions().get(2));

        // Player2의 말을 2번째 노드로 이동
        token3.moveTo(board1.getPositions().get(2));
        token4.moveTo(board1.getPositions().get(2));

        // Player1, Player2가 각자의 말을 업을 수 있는지 체크
        assertTrue(rulesSquare.canStack(token1, token2));
        assertTrue(rulesSquare.canStack(token3, token4));

        // 다른 플레이어의 말을 업을 수 있는지 체크
        assertFalse(rulesSquare.canStack(token1, token3));
        assertFalse(rulesSquare.canStack(token3, token1));

        // 시작/도착점에 있는 말을 업을 수 있는지 체크
        token1.moveTo(board1.getPositions().get(0));
        token2.moveTo(board1.getPositions().get(0));
        assertFalse(rulesSquare.canStack(token1, token2));
    }

    @Test
    void testCanCapture() {
        // Player1의 말을 2번째 노드로 이동
        token1.moveTo(board1.getPositions().get(2));
        token2.moveTo(board1.getPositions().get(2));

        // Player2의 말을 2번째 노드로 이동
        token3.moveTo(board1.getPositions().get(2));
        token4.moveTo(board1.getPositions().get(2));

        // Player1/2의 말이 Player2/1의 말을 잡을 수 있는지 체크
        assertTrue(rulesSquare.canCapture(token1, token3, TossResult.GAE));
        assertTrue(rulesSquare.canCapture(token4, token2, TossResult.GAE));

        // 자신의 말을 잡을 수 있는지 체크
        assertFalse(rulesSquare.canCapture(token1, token2, TossResult.GAE));
        assertFalse(rulesSquare.canCapture(token3, token4, TossResult.GAE));

        // 시작/도착점에 있는 말을 잡을 수 있는지 체크
        token1.moveTo(board1.getPositions().get(1));
        token3.moveTo(board1.getPositions().get(0));
        assertFalse(rulesSquare.canCapture(token1, token3, TossResult.BACKDO));
    }

    private Position s(int id) {
        return board1.getPositions().get(id);
    }

    private Position p(int id) {
        return board2.getPositions().get(id);
    }

    private Position h(int id) {
        return board3.getPositions().get(id);
    }
}