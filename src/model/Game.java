package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
    private int currentTurn;
    private TossResult currentTossResult;
    private boolean waitForCapture;
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
        this.currentPlayerId = 0;
        this.gameState = GameState.READY;
        this.rules = new RuleSet();
        this.currentTurn = 1;
        this.currentToken = getCurrentPlayer().getTokens().getFirst();
    }

    public boolean nextTurn() {
        int prevTurn = currentTurn;
        // 현재 플레이어가 턴을 모두 소진하면 다음 플레이어로 이동
        if (players.get(currentPlayerId).getTurns() == 0) {
            players.get(currentPlayerId).addTurn(1);
            currentPlayerId = (currentPlayerId + 1) % players.size();
            System.out.println("[System] 현재 플레이어: Player"+ (int)(currentPlayerId+1));
            currentTurn++;
            pcs.firePropertyChange("currentTurn", prevTurn, currentTurn);
            currentToken = getCurrentPlayer().getTokens().getFirst();
            return true;
        }
        return false;
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
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
        System.out.println("[System] 랜덤 던지기 결과: " + result);

        if (result == TossResult.BACKDO && currentTurn == 1) {
            getCurrentPlayer().addTurn(-1);
            nextTurn();
            return TossResult.STOP;
        }

        currentTossResult = result;
        return result;
    }

    public TossResult specifiedThrow(TossResult tossResult) {
        if (tossResult == TossResult.YUT || tossResult == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }
        System.out.println("[System] 지정 던지기 결과: " + tossResult);

        //  플레이어가 시작하지 않았는데 백도가 나오면 턴 스킵
        if (tossResult == TossResult.BACKDO) {
            boolean allAtStart = getCurrentPlayer().getTokens().stream()
                    .allMatch(token -> token.getPosition().isStart());
            if (allAtStart) {
                getCurrentPlayer().addTurn(-1);
                nextTurn();
                return TossResult.STOP;
            }
        }
        currentTossResult = tossResult;
        return tossResult;
    }


    // 윷 관련 함수
    public Position selectPosition(int positionIndex) {
        List<Position> positions = board.getPositions();
        return positions.get(positionIndex);
    }

    public Token selectToken(int tokenIndex) {
        currentToken = players.get(currentPlayerId).getTokens().get(tokenIndex % 10);
        if (currentPlayerId != tokenIndex/10) { return null; }
        System.out.println("[System] 토큰" + currentToken.getId() + " 선택됨. 현재 위치: " + currentToken.getPosition().getId());
        return currentToken;
    }

    /* 사용 예시
     * token = selectToken(tokenIndex);
     * newPos = selectPosition(positionIndex);
     * throwResult = randomThrow();
     * applyMoveTo(token, newPos, throwResult)
     */
    public boolean applyMoveTo(Position dest) {
        if (rules.checkMove(currentToken.getPosition(), dest, currentTossResult)) {
            currentToken.moveTo(dest);
            currentToken.getOwner().addTurn(-1); // 움직였으면 현재 턴 수에서 -1
            System.out.println();
            // handle finishing conditions
            if (dest.isGoal()) {
                currentToken.setFinished(true);
            }

            List<Token> capturableTokens = getTokensAt(dest);
            if (!capturableTokens.isEmpty()) { waitForCapture = true; }
            if (!waitForCapture) { nextTurn(); }
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
                    if (currentTossResult == TossResult.YUT || currentTossResult == TossResult.MO) {
                        token.getOwner().addTurn(-1);
                    }
                }
            }
        }
        if (waitForCapture) {
            waitForCapture = false;
            nextTurn();
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
    public Token getCurrentToken() { return currentToken; }
    public int getPlayerCount() { return players.size(); }
    public int getTokenCount() { return tokenCount; }
    public String getShapeType() { return shapeType; }
}

/*
setState(GameState.RUNNING);
        Scanner scanner = new Scanner(System.in);
        while (gameState == GameState.RUNNING) {
            // 게임 시작 및 차례 테스팅
            while (getCurrentPlayer().getTurns() > 0) {
                TossResult result = null;
                int choice = 0;
                String yutChoice;
                boolean moveResult = false;

                System.out.printf("[게임 상태 표시] 현재 플레이어: %s, 남은 턴: %d\n", getCurrentPlayer().getName(), getCurrentPlayer().getTurns());
                System.out.println("[윷 던지기] t: 랜덤 윷 던지기, s: 지정 윷 던지기");

                // 윷 던지기 테스팅
                yutChoice = scanner.nextLine();
                if (yutChoice.equals("t")) {
                    result = randomThrow();
                    System.out.printf("결과: %d\n", result.getValue());
                }
                else if (yutChoice.equals("s")) {
                    System.out.println("[지정 윷 던지기] 결과 선택 -1 ~ 5");
                    result = scanner.nextInt(); scanner.nextLine();
                }

                // 말 이동, 업기, 잡기 테스팅
                System.out.println("[말 이동] 어느 말을 움직일까요?");
                for (int i = 0; i < getCurrentPlayer().getTokens().size(); i++) {
                    int posInfo = -1;
                    if (getCurrentPlayer().getTokens().get(i).getPosition() != null) { posInfo = getCurrentPlayer().getTokens().get(i).getPosition().getId(); }
                    System.out.printf("%d) %d (on %d, stacked: ", i+1, getCurrentPlayer().getTokens().get(i).getId(), posInfo);
                    List<Token> stackedTokens = getCurrentPlayer().getTokens().get(i).getStackedTokens();
                    if (!stackedTokens.isEmpty()) {
                        for (Token token : stackedTokens) {
                            System.out.printf("%d, ", token.getId());
                        }
                    }
                    System.out.println(")");
                }

                choice = scanner.nextInt();scanner.nextLine();
                currentToken = getCurrentPlayer().getTokens().get(choice-1);
                if (currentToken.getPosition() == null)
                    currentToken.moveTo(board.getPositions().get(0));


                while (!moveResult) {
                    System.out.println("[말 이동] 어디로 움직일까요?");
                    int posId = scanner.nextInt(); scanner.nextLine();
                    moveResult = applyMoveTo(currentToken, board.getPositions().get(posId), result);
                    if (moveResult) {
                        System.out.printf("[말 이동] %d번째 말이 %d번째 칸으로 이동했습니다. 남은 턴: %d\n", choice-1, posId, getCurrentPlayer().getTurns());

                        List<Token> tokensOnPos = getTokensAt(board.getPositions().get(posId));
                        for (Token targetToken : tokensOnPos) {
                            if (rules.canCapture(currentToken, targetToken)) {
                                System.out.println("[말 잡기] 상대의 말을 잡을 수 있습니다. 0/1");
                                choice = scanner.nextInt(); scanner.nextLine();
                                if (choice == 1) {
                                    targetToken.reset();
                                }
                            }
                            if (rules.canStack(currentToken, targetToken)) {
                                System.out.println("[말 업기] 말을 업을 수 있습니다. 0/1");
                                choice = scanner.nextInt(); scanner.nextLine();
                                if (choice == 1) {
                                    currentToken.stackWith(targetToken);
                                }
                                else { getCurrentPlayer().addTurn(-1); }
                            }
                        }

                    }
                    else {
                        System.out.println("이동할 수 없습니다.");
                    }
                }
            }
            int winner = checkPlayerWin();
            if (winner > -1) {
                System.out.println("[게임 종료]: " + players.get(winner).getName() + " 우승!");
                endGame();
            }
            else {
                nextTurn();
            }
        }
 */