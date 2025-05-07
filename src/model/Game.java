package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private int playerCount;
    private int tokenCount;
    private String shapeType;
    private Token currentToken; // 디버깅 과정에서 사용됨. 나중에 처리
    private List<Player> players;
    private Board board;
    private int currentPlayerId;
    private GameState gameState;
    private RuleSet rules;

    public Game() {}

    // 게임 시작 설정 함수
    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public void setBoardShape(String shape) { this.shapeType = shape; }
    public void setState(GameState gameState) { this.gameState = gameState; }


    // 게임 흐름 관련 함수
    public void startGame(int playerCount, int tokenCount, String shapeType) {
        //MainWindow mainWindow = new MainWindow();
        // mainWindow.showGameScreen(playerCount, tokenCount, shapeType);
        this.players = new ArrayList<>();
        Color[] palette = {
                new Color(100, 149, 237), // 파랑
                new Color(240, 128, 128), // 빨강
                new Color(255, 215, 0),   // 노랑
                new Color(144, 238, 144)  // 연두
        };
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player("Player " + (i + 1), i, tokenCount, palette[i]);
            players.add(player);
        }
        this.board = new Board(shapeType);
        this.currentPlayerId = 0;
        this.gameState = GameState.READY;
        this.rules = new RuleSet();
    }

    public void nextTurn() {
        // 현재 플레이어가 턴을 모두 소진하면 다음 플레이어로 이동
        if (players.get(currentPlayerId).getTurns() == 0) {
            players.get(currentPlayerId).addTurn(1);
            currentPlayerId = (currentPlayerId + 1) % players.size();
        }
    }

    public int checkPlayerWin() {
        for (int i = 0; i < players.size(); i++) {
            boolean allFinished = true;
            for (Token token : players.get(i).getTokens()) {
                if (!token.isFinished()) {
                    allFinished = false;
                    break;
                }
            }
            if (allFinished) {
                return i;
            }
        }
        return -1;
    }

    public void endGame() {
        playerCount = 0;
        tokenCount = 0;
        shapeType = "사각형";
        players.clear();
        currentPlayerId = 0;
        gameState = GameState.ENDED;
    }

    public void restartGame() {
        endGame();
        startGame(playerCount, tokenCount, shapeType);
    }

    // 윷 관련 함수
    public TossResult randomThrow() {
        // 백도(6.25%; '도'일 때 25%의 확률), 도(25%), 개(37.5%), 걸(25%), 윷(6.25%), 모(6.25%)
        TossResult[] outcomes = {TossResult.BACKDO, TossResult.DO, TossResult.GAE, TossResult.GEOL, TossResult.YUT, TossResult.MO};
        double[] probabilities = {0.25, 0.625, 0.875, 0.9375, 1.0};

        TossResult result = null;
        Random rand = new Random();
        double r = rand.nextDouble();

        for (int i = 0; i < probabilities.length; i++) {
            if (r < probabilities[i]) {
                result = outcomes[i+1];
                break;
            }
        }

        // 백도 체크
        if (result == TossResult.DO && rand.nextDouble() <= 0.25) {
            result = outcomes[0];
        }

        if (result == TossResult.YUT || result == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }

        return result;
    }

    public TossResult specifiedThrow(TossResult tossResult) {
        if (tossResult == TossResult.YUT || tossResult == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }
        return tossResult;
    }


    // 윷 관련 함수
    public Position selectPosition(int positionIndex) {
        List<Position> positions = board.getPositions();
        return positions.get(positionIndex);
    }

    public Token selectToken(int tokenIndex) {
        currentToken = getCurrentPlayer().getTokens().get(tokenIndex);
        return currentToken;
    }

    /* 사용 예시
     * token = selectToken(tokenIndex);
     * newPos = selectPosition(positionIndex);
     * throwResult = randomThrow();
     * applyMoveTo(token, newPos, throwResult)
     */
    public boolean applyMoveTo(Token token, Position dest, TossResult tossResult) {
        if (rules.checkMove(token.getPosition(), dest, tossResult)) {
            token.moveTo(dest);
            token.getOwner().addTurn(-1); // 움직였으면 현재 턴 수에서 -1

            // handle finishing conditions
            if (dest.isGoal()) {
                token.setFinished(true);
            }
            return true;
        }
        return false;
    }

    public void handleStacking(Token token, Position position) {
        // position에 token이 업을 수 있는 말이 있는지 확인
        List<Token> stackableTokens = getTokensAt(position);
        if (!stackableTokens.isEmpty()) {
            for (Token targetToken: stackableTokens) {
                if (rules.canStack(token, targetToken)) {
                    token.stackWith(targetToken);
                }
            }
        }
    }

    public void handleCapturing(Token token, Position position) {
        // position에 token이 잡을 수 있는 말이 있는지 확인
        List<Token> capturableTokens = getTokensAt(position);
        if (!capturableTokens.isEmpty()) {
            for (Token targetToken: capturableTokens) {
                if (rules.canCapture(token, targetToken)) {
                    targetToken.reset();
                    token.getOwner().addTurn(1); // 말을 잡았다면 현재 턴 수에서 +1
                }
            }
        }
    }

    public List<Token> getTokensAt(Position position) {
        List<Token> tokens = new ArrayList<>();
        for (Player player : players) {
            for (Token t : player.getTokens()) {
                if (t.getId() != currentToken.getId() && t.getPosition() != null && t.getPosition().equals(position) && !t.isFinished()) {
                    tokens.add(t);
                }
            }
        }
        return tokens;
    }

    // getters
    public GameState getState() { return gameState; }
    public Player getCurrentPlayer() { return players.get(currentPlayerId); }
}
