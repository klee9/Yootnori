package test;

import model.Player;
import model.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1, player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1", 0, 3);
        player2 = new Player("Player2", 1, 5);
    }

    @Test
    void testGetTokens() {
        List<Token> tokenList1 = player1.getTokens();
        List<Token> tokenList2 = player2.getTokens();

        assertEquals(3, tokenList1.size());
        assertEquals(5, tokenList2.size());

        assertEquals(0, tokenList1.get(0).getId());
        assertEquals(1, tokenList1.get(1).getId());
        assertEquals(2, tokenList1.get(2).getId());

        assertEquals(10, tokenList2.get(0).getId());
        assertEquals(11, tokenList2.get(1).getId());
        assertEquals(14, tokenList2.get(4).getId());
    }

    @Test
    void testGetRemainingTokens() {
        // player1, player2의 남은 말 개수 불러오기
        int tokenCount1 = player1.getRemainingTokens();
        int tokenCount2 = player2.getRemainingTokens();

        // 완주하지 않았다면 변화가 없어야 함
        assertEquals(3, tokenCount1);
        assertEquals(5, tokenCount2);

        List<Token> tokenList1 = player1.getTokens();
        List<Token> tokenList2 = player2.getTokens();

        tokenList1.get(0).setFinished(true);
        tokenList2.get(0).setFinished(true);
        assertEquals(2, player1.getRemainingTokens());
        assertEquals(4, player2.getRemainingTokens());
    }

    @Test
    void testGetTurns() {
        // 각 플레이어의 초기 턴은 1이어야 함
        assertEquals(1, player1.getTurns());
        assertEquals(1, player2.getTurns());
    }

    @Test
    void testAddTurn() {
        player1.addTurn(1);
        player2.addTurn(-1);

        assertEquals(2, player1.getTurns());
        assertEquals(0, player2.getTurns());
    }

    @Test
    void testGetName() {
        assertEquals("Player1", player1.getName());
        assertEquals("Player2", player2.getName());
    }

    @Test
    void testGetId() {
        assertEquals(0, player1.getId());
        assertEquals(1, player2.getId());
    }
}