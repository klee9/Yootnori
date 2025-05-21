package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private int playerCount;
    private int tokenCount;
    private int shapeType;

    @BeforeEach
    void setUp() {
        game = new Game();
        playerCount = 2;
        tokenCount = 3;
        shapeType = 4;
        game.startGame(playerCount, tokenCount, shapeType);
    }

    @Test
    void testStartGame() {
        // 게임 관련 변수들이 정상적으로 초기화 되었는지 체크
        assertNotNull(game.getPlayers());
        assertEquals(playerCount, game.getPlayers().size());
        assertEquals(0, game.getCurrentPlayer().getId());
        assertNotNull(game.getCurrentPlayer().getTokens());
        assertEquals(tokenCount, game.getCurrentPlayer().getTokens().size());
        assertNotNull(game.randomThrow());
    }

    @Test
    void testNextTurn() {
        // 현재 플레이어의 턴을 강제로 소진한 후, nextTurn() 호출
        int currentPlayer = game.getCurrentPlayer().getId();
        game.getCurrentPlayer().addTurn(-1);
        game.nextTurn();

        // 정상적으로 턴이 넘어갔다면 현재 플레이어는 다음 플레이어가 되어야 함
        int nextPlayer = game.getCurrentPlayer().getId();
        assertNotEquals(currentPlayer, nextPlayer);
        assertEquals((currentPlayer + 1) % playerCount, nextPlayer);
    }

    @Test
    void testRestartGame() {
        game.restartGame();
        assertEquals(0, game.getCurrentPlayer().getId());
        assertNotNull(game.getPlayers());
        assertEquals(playerCount, game.getPlayers().size());
    }

    @Test
    void testRandomThrow() {
        // 던지면 유효한 값이 나와야 함
        TossResult result = game.randomThrow();
        assertNotNull(result);
    }

    @Test
    void testSpecifiedThrow() {
        // 개를 지정한 후 던지면, 결과는 항상 개여야 함
        TossResult result = game.specifiedThrow(TossResult.GAE);
        assertEquals(TossResult.GAE, result);
    }

    @Test
    void testCheckPlayerWin() {
        // return type이 없기 때문에 직접적으로 확인할 수 없으나, 간접적으로 체크 가능
        game.getCurrentPlayer().getTokens().forEach(token -> token.setFinished(true));
        game.checkPlayerWin();
        assertTrue(game.getPlayers().get(0).getTokens().stream().allMatch(Token::isFinished));
    }

    @Test
    void testSelectPosition() {
        // 1번 노드가 반환되는지 확인
        Position position = game.selectPosition(1);
        assertNotNull(position);
        assertEquals(1, position.getId());
    }

    @Test
    void testSelectToken() {
        // 0번 말이 반환되는지 확인
        game.selectToken(0);
        assertEquals(0, game.getCurrentPlayer().getTokens().get(0).getId());
    }

    @Test
    void testApplyMoveTo() {
        // 현재 말: Token0, 움직일 곳: 2번째 노드
        game.selectToken(0);
        game.specifiedThrow(TossResult.GAE);

        Position next = game.selectPosition(2);

        boolean result = game.applyMoveTo(next);

        // 말 이동 결과는 참이어야 하고, Token0의 위치는 2여야 한다 (차례가 넘어가서 CurrentPlayer X, PrevPlayer O)
        assertTrue(result);
        assertEquals(2, game.getPrevPlayer().getTokens().get(0).getPosition().getId());
    }

    @Test
    void testHandleCapturing() {
        // 현재 말: Token0, 잡을 말: Token10, Token11
        game.selectToken(0);

        int remainingTurns = game.getCurrentPlayer().getTurns();
        Position pos = game.selectPosition(1);

        Token token1 = game.getCurrentPlayer().getTokens().get(0);
        Token targetToken1 = game.getPrevPlayer().getTokens().get(0);
        Token targetToken2 = game.getPrevPlayer().getTokens().get(1);

        token1.moveTo(pos);
        targetToken1.moveTo(pos);
        targetToken2.moveTo(pos);

        game.handleCapturing(token1, pos);

        // 출발 위치로 돌아가야 함
        assertEquals(0, targetToken1.getPosition().getId());
        assertEquals(0, targetToken2.getPosition().getId());

        // unstack 되어야 함
        assertEquals(0, targetToken1.getStackedTokens().size());
        assertEquals(0, targetToken2.getStackedTokens().size());

        // 말을 잡은 플레이어에게 턴을 추가해야 함 (applyMoveTo를 사용하지 않았으므로 2가 되어야 함)
        assertEquals(remainingTurns + 1, game.getCurrentPlayer().getTurns());
    }

    @Test
    void testHandleStacking() {
        // 현재 말: Token0, 업을 말: Token1, Token2
        game.selectToken(0);
        Position pos = game.selectPosition(1);

        Token token1 = game.getCurrentPlayer().getTokens().get(0);
        Token token2 = game.getCurrentPlayer().getTokens().get(1);
        Token token3 = game.getCurrentPlayer().getTokens().get(2);

        token1.moveTo(pos);
        token2.moveTo(pos);
        token3.moveTo(pos);

        game.handleStacking(token1, pos);

        assertEquals(2, token1.getStackedTokens().size());
        assertTrue(token1.getStackedTokens().contains(token2));
        assertTrue(token1.getStackedTokens().contains(token3));
    }

    @Test
    void testGetTokensAt() {
        // 0번 노드에는 현재 말을 제외한 다른 모든 말이 있어야 함
        Position pos = game.selectPosition(0);
        List<Token> tokens = game.getTokensAt(pos);
        assertEquals(playerCount * tokenCount - 1, tokens.size());
    }

    @Test
    void testCheckAutoControl() {
        int result = game.checkAutoControl();
        assertTrue(result >= -1);
    }

    @Test
    void testIdToPosition() {
        Position pos = game.idToPosition(1);
        assertNotNull(pos);
        assertEquals(1, pos.getId());
    }
}