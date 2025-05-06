package model;

import ui.MainWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private int playerCount;
    private int tokenCount;
    private int shapeType;
    private Token currentToken; // 디버깅 과정에서 사용됨. 나중에 처리
    private List<Player> players;
    private Board board;
    private int currentPlayerId;
    private GameState gameState;
    private RuleSet rules;

    public Game(int playerCount, int tokenCount, int shapeType) {
        this.players = new ArrayList<>();
        for( int i = 0; i < playerCount; i++){
            Color color = Color.BLACK;
            Player player = new Player("플레이어" + (i + 1), i, tokenCount, color);
            players.add(player);
        }
        this.board = new Board(shapeType);
        this.currentPlayerId = 0;
        this.gameState = GameState.READY;
        this.rules = new RuleSet();
    }

    // 게임 시작 설정 함수
    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public void setBoardShape(int shape) { this.shapeType = shape; }
    public void setState(GameState gameState) { this.gameState = gameState; }


    // 게임 흐름 관련 함수
    public Game startGame(int playerCount, int tokenCount, int shapeType) {
        Game game = new Game(playerCount, tokenCount, shapeType);
        MainWindow mainWindow = new MainWindow();
        setState(GameState.RUNNING);
        // mainWindow.showGameScreen(playerCount, tokenCount, shapeType);
        return game;
    }

    public void nextTurn() {
        // 현재 플레이어가 턴을 모두 소진하면 다음 플레이어로 이동
        if (players.get(currentPlayerId).getTurns() == 0) {
            players.get(currentPlayerId).addTurn(1);
            currentPlayerId = (currentPlayerId + 1) % players.size();
        }
    }

    public void endGame() {
        playerCount = 0;
        tokenCount = 0;
        shapeType = 4;
        players.clear();
        currentPlayerId = 0;
        gameState = GameState.ENDED;
    }

    public void restartGame() {
        endGame();
        startGame(playerCount, tokenCount, shapeType);
    }

    // 윷 관련 함수
    public int randomThrow() {
        int[] outcomes = {-1, 1, 2, 3, 4, 5}; // 백도(6.25%; '도'일 때 25%의 확률), 도(25%), 개(37.5%), 걸(25%), 윷(6.25%), 모(6.25%)
        double[] probabilities = {0.25, 0.625, 0.875, 0.9375, 1.0};

        int result = 0;
        Random rand = new Random();
        double r = rand.nextDouble();

        for (int i = 0; i < probabilities.length; i++) {
            if (r < probabilities[i]) {
                result = outcomes[i+1];
            }
        }

        // 백도 체크
        if (result == 1 && rand.nextDouble() <= 0.25) {
            result = outcomes[0];
        }

        return result;
    }

    public int specifiedThrow(int tossResult) {
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
    public void applyMoveTo(Token token, Position newPos, int throwResult) {
        List<Token> capturableTokens = getTokensAt(newPos);

        if (rules.checkMove(token.getPosition(), newPos, throwResult)) {
            token.moveTo(newPos);
            token.getOwner().addTurn(-1); // 움직였으면 현재 턴 수에서 -1

            // handle finishing conditions
            if (board.getGoalPosition().equals(newPos)) {
                token.setFinished(true);
                handleTokenFinished(token);
            }

            // handle capturing
            if (!capturableTokens.isEmpty()) {
                for (Token targetToken: capturableTokens) {
                    if (rules.canCapture(token, targetToken)) {
                        targetToken.reset();
                        token.getOwner().addTurn(1); // 말을 잡았다면 현재 턴 수에서 +1
                    }
                }
            }
        }
    }

    public void handleStacking(Token tokenA, Token tokenB) {
        if (rules.canStack(tokenA, tokenB)) {
            tokenA.stackWith(tokenB);
        }
    }

    public void handleCapturing(Token tokenA, Token tokenB) {
        // tokenA captures tokenB
        if (rules.canCapture(tokenA, tokenB)) {
            tokenB.reset();
        }
    }

    public void handleTokenFinished(Token token) {
        if (token.isFinished()) {
            Player owner = token.getOwner();
            owner.removeToken(token);
            // UI에서는 owner.tokens.length로 남은 말 개수 표시
        }
    }

    public List<Token> getTokensAt(Position position) {
        List<Token> tokens = new ArrayList<>();
        for (Player player : players) {
            for (Token t : player.getTokens()) {
                if (t.getPosition().equals(position) && !t.isFinished()) {
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
