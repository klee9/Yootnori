package model;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Game {
    private int playerCount;
    private int tokenCount;
    private String shapeType;
    private Board board;
    private GameState gameState;
    private RuleSet rules;

    private Token currentToken;
    private boolean waitForCapture;
    private List<Token> capturedTokens;

    private List<Player> players;
    private int currentPlayerId;

    private int currentTurn;
    private List<TossResult> YutResults = new ArrayList<>(); // TODO 여기 추가함
    private TossResult currentMove = TossResult.BACKDO;      // TODO 여기 추가함

    private PropertyChangeSupport pcs;

    public Game() {
        pcs = new PropertyChangeSupport(this);
    }

    // 게임 시작 설정 함수
    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public void setBoardShape(String shape) { this.shapeType = shape; }
    public void setState(GameState gameState) { this.gameState = gameState; }


    // 게임 흐름 관련 함수
    public void startGame(int playerCount, int tokenCount, String shapeType) {
        //MainWindow mainWindow = new MainWindow();
        // mainWindow.showGameScreen(playerCount, tokenCount, shapeType);
        this.board = new Board(shapeType);
        this.players = new ArrayList<>();
        Color[] palette = {
                new Color(100, 149, 237), // 파랑
                new Color(240, 128, 128), // 빨강
                new Color(255, 215, 0),   // 노랑
                new Color(144, 238, 144)  // 연두
        };
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player("Player " + (i + 1), i, tokenCount, palette[i], board);
            players.add(player);
        }
        this.playerCount = playerCount;
        this.shapeType = shapeType;
        this.tokenCount = tokenCount;
        this.currentPlayerId = 0;
        this.gameState = GameState.READY;
        this.rules = new RuleSet(shapeType);
        this.currentTurn = 1;
        this.currentToken = getCurrentPlayer().getTokens().get(0);
        this.capturedTokens = new ArrayList<>();
    }

    public boolean nextTurn() {
        // 현재 플레이어가 턴을 모두 소진하면 다음 플레이어로 이동
        if (players.get(currentPlayerId).getTurns() <= 0) {
            players.get(currentPlayerId).addTurn(1);
            currentPlayerId = (currentPlayerId + 1) % players.size();
            pcs.firePropertyChange("nextTurn", false, true);
            currentTurn++;
            YutResults.clear();
            currentToken = getCurrentPlayer().getTokens().get(0);
            return true;
        }
        return false;
    }

    public int checkPlayerWin() {
        for (int i = 0; i < players.size(); i++) {
            boolean allFinished = players.get(i).getTokens().stream().allMatch(Token::isFinished);
            if (allFinished) {
                pcs.firePropertyChange("winner", null, players.get(i));
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
        currentTurn = 1;
        gameState = GameState.ENDED;
    }

    public void restartGame() {
        startGame(playerCount, tokenCount, shapeType);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    // 윷 관련 함수
    public TossResult randomThrow() {
        System.out.println("[System] 남은 턴: " + getCurrentPlayer().getTurns());
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
        YutResults.add(result);
        System.out.println("[System] 랜덤 던지기 결과: " + result);

        //  플레이어가 시작하지 않았는데 백도가 나오면 턴 스킵
        if (result == TossResult.BACKDO) {
            boolean allAtStart = getCurrentPlayer().getTokens().stream()
                    .allMatch(token -> token.getPosition().isStart());
            boolean cantMove = YutResults.stream().allMatch(e -> e.equals(TossResult.BACKDO));
            pcs.firePropertyChange("cantMove", null, cantMove&allAtStart);
            if (allAtStart && cantMove) {
                getCurrentPlayer().addTurn(-1);
                nextTurn();
                return TossResult.BACKDO;
            }
        }
        return result;
    }

    public TossResult specifiedThrow(TossResult tossResult) {
        System.out.println("[System] 남은 턴: " + getCurrentPlayer().getTurns());
        if (tossResult == TossResult.YUT || tossResult == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }
        System.out.println("[System] 지정 던지기 결과: " + tossResult);

        //  플레이어가 시작하지 않았는데 백도가 나오면 턴 스킵
        if (tossResult == TossResult.BACKDO) {
            boolean allAtStart = getCurrentPlayer().getTokens().stream()
                    .allMatch(token -> token.getPosition().isStart());
            boolean cantMove = YutResults.stream().allMatch(e -> e.equals(TossResult.BACKDO));
            pcs.firePropertyChange("cantMove", null, cantMove&allAtStart);
            if (allAtStart && cantMove) {
                getCurrentPlayer().addTurn(-1);
                nextTurn();
                return TossResult.BACKDO;
            }
        }
        YutResults.add(tossResult);
        return tossResult;
    }


    // 윷 관련 함수
    public Position selectPosition(int positionIndex) {
        List<Position> positions = board.getPositions();
        return positions.get(positionIndex);
    }

    public void selectToken(int tokenIndex) {
        if (currentPlayerId != tokenIndex/10) {
            System.out.println("WTF!!1");
            currentToken = getCurrentPlayer().getTokens().get(0);
        }
        else {
            currentToken = players.get(currentPlayerId).getTokens().get(tokenIndex % 10);
            System.out.println("[System] 토큰" + currentToken.getId() + " 선택됨. 현재 위치: " + currentToken.getPosition().getId());
        }
    }

    public boolean applyMoveTo(Position dest) {
        for (TossResult result : YutResults) {
            if (rules.checkMove(currentToken.getPosition(), dest, result)) {

                lastMove = new LastMove(
                    currentToken,
                    currentToken.getPosition(),
                    dest,
                    result,
                    /*turnDelta=*/-1
                );

                // 도착 노드에 있는 말 찾기
                List<Token> tokensOnDest = getTokensAt(dest);
                currentMove = result;
                capturedTokens.clear();

                // 말 먼저 이동
                currentToken.moveTo(dest);
                currentToken.getOwner().addTurn(-1); // 움직였으면 현재 턴 수에서 -1
                YutResults.remove(result);
                System.out.println("[Game] "+result+"로 말을 이동했습니다. 남은 턴: " + getCurrentPlayer().getTurns());
                for (TossResult tossResult : YutResults) {
                    System.out.printf("남은 이동: %s\n", tossResult);
                }

                // 도착 칸이 goal이면 종료 처리
                if (dest.isGoal()) {
                    currentToken.setFinished(true);
                    pcs.firePropertyChange("tokenFinished", null, currentToken.getId());
                    nextTurn();
                    checkPlayerWin();
                    return true;
                }

                // 말 잡기/업기 조건 확인 --> 이후 예/아니오 추가
                Token tempToken = new Token(50, Color.BLACK, getCurrentPlayer(),board, null, null);
                tempToken.moveTo(dest);

                for (Token t : tokensOnDest) {
                    if (rules.canCapture(tempToken, t, result)) {
                        pcs.firePropertyChange(
                            "captureRequest",
                            null,
                            new SimpleEntry<>(currentToken, t)
                        );
                        System.out.println("[Game] 말을 잡았습니다. 남은 턴: " + getCurrentPlayer().getTurns());
                        if (currentMove.getValue() < 4) {
                            pcs.firePropertyChange("turnsLeft", null, 0);
                        }
                    }
                    if (rules.canStack(tempToken, t)) {
                        pcs.firePropertyChange(
                            "stackRequest",
                            null,
                            new SimpleEntry<>(currentToken, t)
                        );
                        System.out.println("[Game] 업기 조건 충족됨");
                    }
                }
                tempToken = null;
                nextTurn();
                return true;
            }
        }
        return false;
    }

    public void handleStacking(Token carrier, Position position) {
        // 업을 수 있는 토큰들끼리 묶기
        List<Token> stackable = getTokensAt(position);
        for (Token other : stackable) {
            if (rules.canStack(carrier, other)) {
                carrier.stackWith(other);
            }
        }

        // UI 갱신용 이벤트 - carrier 자신과 업힌 토큰들
        pcs.firePropertyChange("tokenMoved", null, carrier.getId());
        for (Token t : carrier.getStackedTokens()) {
            pcs.firePropertyChange("tokenMoved", null, t.getId());
        }
    }

    public void handleCapturing(Token token, Position position) {
        // position에 token이 잡을 수 있는 말이 있는지 확인
        capturedTokens.clear();

        // 1) 잡을 수 있는 말 찾기
        List<Token> capturableTokens = getTokensAt(position);
        if (capturableTokens.isEmpty()) return;

        for (Token targetToken : capturableTokens) {
            if (rules.canCapture(token, targetToken, currentMove)) {
                /* 2) 말 리셋 */
                targetToken.reset();

                /* 3) 잡힌 말 목록에 추가 ― 컨트롤러가 UI 갱신용으로 사용 */
                capturedTokens.add(targetToken);                        // ★ 추가

                /* 4) 바로 UI에 위치 변경 통보 (토큰이 '출발'로 옮겨졌음을 그림) */
                pcs.firePropertyChange("tokenMoved", null, targetToken.getId()); // ★ 추가

                /* 5) 윷·모로 잡았으면 턴 소모 보정 */
                if (currentMove == TossResult.YUT || currentMove == TossResult.MO) {
                    token.getOwner().addTurn(-1);
                }
            }
        }
    }

    public List<Token> getTokensAt(Position position) {
        List<Token> tokens = new ArrayList<>();
        for (Player player : players) {
            for (Token t : player.getTokens()) {
                if (t.getId() != currentToken.getId() && t.getPosition().getId() == position.getId() && !t.isFinished()) {
                    tokens.add(t);
                    System.out.printf("On position: %d are %d\n", position.getId(), t.getId());
                }
            }
        }
        return tokens;
    }
    // 보드가 아니라 토큰이 클릭돼서 스태킹이 작동하지 않음.

    private record LastMove(Token token,
                            Position from,
                            Position to,
                            TossResult usedYut,
                            int turnDelta) {}

    private LastMove lastMove; // 가장 최근 위치 기억(다이얼로그에서 아니오 눌렀을 때 복구용)

    public void undoLastMove() {
        if (lastMove == null) return;

        Token tok = lastMove.token();
        tok.moveTo(lastMove.from()); // 위치 복구
        getCurrentPlayer().addTurn(-lastMove.turnDelta()); // 턴 수 복구
        YutResults.add(0, lastMove.usedYut()); // 윷 결과 복구

        pcs.firePropertyChange("tokenMoved", null, tok.getId());

        lastMove = null;
    }

    // getters
    public Player getPrevPlayer() { return players.get((currentPlayerId-1+playerCount)%playerCount); }
    public Player getCurrentPlayer() { return players.get(currentPlayerId); }
    public Token getCurrentToken() { return currentToken; }
    public List<Token> getCapturedTokens() { return capturedTokens; }
    public List<Player> getPlayers() { return players; }
}