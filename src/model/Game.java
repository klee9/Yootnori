package model;

import java.util.*;

public class Game  {
    private List<Player> players;
    private Board board;
    private int currentPlayerIndex;
    private GameState gameState;
    private RuleSet ruleSet;

    public Game(int playerCount, int tokenCount, int shape_type) {
        this.players = new ArrayList<>();
        TokenType[] tokenTypes = TokenType.values(); // TokenType에 정의된 모든 모양을 배열에 가져온다.(플레이어 마다 다른 말의 모양 가지기 위해서)
        for( int i = 0; i < playerCount; i++){
            TokenType type = tokenTypes[i];
            Player player = new Player("플레이어" + (i + 1),i,tokenCount,type);// 플레이어를 생성하고 players 에 추가된다.
            players.add(player);
        }
        this.board = new Board(shape_type);  // Board 를 shape_type에 맞춰 생성한다.
        this.currentPlayerIndex = 0;         // 첫 번째 플레이어부터 게임을 시작하도록 인덱스를 0으로 초기화
        this.gameState = GameState.READY;    // 게임을 준비 상태로 설정한다. ( READY, STARTED, ENDED )
        this.ruleSet = new RuleSet();        // 게임 내 RuleSet 객체 생성

    }

    public Player getCurrentPlayer() { // 현재 턴의 플레이어를 반환한다.
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() { // 다음 플레이어 인덱스를 나타낸다.
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean isGameOver() { // 게임이 끝났는지 확인한다.
        return players.stream().anyMatch(Player::hasFinished);
    }

    public GameState getState() {
        return gameState;
    }

    public MoveResult applyMoveTo(Token token, int tossResult, Position newPosition) {
        Token capturedToken = getTokenAt(newPosition);
        boolean isCapture = false;
        boolean extraTurn = false;

        token.moveTo(newPosition);

        //도착했는지 확인한다.
        if (board.getGoalPosition().equals(newPosition)) {
            token.setFinished(true);
        }
        //잡았는지 확인한다.
        if (capturedToken != null && ruleSet.canCapture(token, capturedToken)) {
            isCapture = true;
            capturedToken.reset();
        }

        // 추가 턴 여부를 판단한다.
        if (ruleSet.getAdditionalTurn(tossResult, isCapture ? 7 : 0)) {
            extraTurn = true;
        }

        return new MoveResult(token, false, extraTurn, token.isFinished());
    }


    private Token getTokenAt( Position position){ // 해당 위치에 말이 있는지 확인한다.
        for (Player player : players) {
            for (Token t : player.getTokens()) {
                if (t.getPosition().equals(position) && !t.isFinished()) {
                    return t;
                }
            }
        }
        return null;
    }


}
