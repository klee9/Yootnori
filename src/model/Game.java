package model;

import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class Game {
    private int playerCount;
    private int tokenCount;
    private int shapeType;

    private Board board;
    private RuleSet rules;

    private List<Player> players;
    private int currentPlayerId;

    private Token currentToken;
    private List<Token> capturedTokens;

    private List<TossResult> YutResults;
    private TossResult currentMove;

    private PropertyChangeSupport pcs;

    public Game() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /* 게임 흐름 관련 함수
     * startGame: 게임 시작 요소 초기화
     * nextTurn: 다음 차례인지 확인하고, 넘어감
     * checkPlayerWin: 이긴 플레이어가 있는지 확인
     * restartGame: 게임 재시작 시 변수 초기화
     */
    public void startGame(int playerCount, int tokenCount, int shapeType) {
        this.playerCount = playerCount;
        this.shapeType = shapeType;
        this.tokenCount = tokenCount;

        this.board = new Board(shapeType);
        this.rules = new RuleSet(shapeType);

        this.players = new ArrayList<>();
        this.currentPlayerId = 0;
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player("Player " + (i + 1), i, tokenCount);

            for (Token token : player.getTokens()) {
                token.setPosition(board.getStartPosition());
            }
            players.add(player);
        }

        this.currentToken = players.get(0).getTokens().get(0);
        this.capturedTokens = new ArrayList<>();

        this.YutResults = new ArrayList<>();
        this.currentMove = TossResult.BACKDO;
    }

    public void nextTurn() {
        // 현재 플레이어가 턴을 모두 소진하면 다음 플레이어로 이동
        if (players.get(currentPlayerId).getTurns() <= 0) {
            YutResults.clear();
            players.get(currentPlayerId).addTurn(1);
            currentPlayerId = (currentPlayerId + 1) % players.size();
            updateCurrentToken();
            pcs.firePropertyChange("nextTurn", false, true);
        }
    }

    public void checkPlayerWin() {
        for (Player player : players) {
            boolean allFinished = player.getTokens().stream().allMatch(Token::isFinished);
            if (allFinished) {
                pcs.firePropertyChange("winner", null, player);
            }
        }
    }

    public void restartGame() {
        startGame(playerCount, tokenCount, shapeType);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /* 윷 관련 함수
     * randomThrow: 랜덤 윷 던지기
     * specifiedThrow: 지정 윷 던지기
     */
    public TossResult randomThrow() {
        // 빽도(6.25%; '도'일 때 25%의 확률), 도(25%), 개(37.5%), 걸(25%), 윷(6.25%), 모(6.25%)
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

        // 빽도 체크 (도가 나왔을 때 25%의 확률)
        if (result == TossResult.DO && rand.nextDouble() <= 0.25) {
            result = outcomes[0];
        }

        if (result == TossResult.YUT || result == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }

        System.out.println("[Game] 랜덤 윷 던지기 결과: " + result);

        //  플레이어가 시작하지 않았는데 백도가 나오면 턴 스킵
        if (result == TossResult.BACKDO) {
            boolean allAtStart = getCurrentPlayer().getTokens().stream().allMatch(token -> token.getPosition().isStart());
            boolean cantMove = YutResults.stream().allMatch(e -> e.equals(TossResult.BACKDO));
            pcs.firePropertyChange("cantMove", null, cantMove&allAtStart);
            if (allAtStart && cantMove) {
                getCurrentPlayer().addTurn(-1);
                nextTurn();
            }
        }

        YutResults.add(result);
        return result;
    }

    public TossResult specifiedThrow(TossResult tossResult) {
        if (tossResult == TossResult.YUT || tossResult == TossResult.MO) {
            getCurrentPlayer().addTurn(1);
        }

        YutResults.add(tossResult);
        System.out.println("[Game] 지정 윷 던지기 결과: " + tossResult);

        //  플레이어가 시작하지 않았는데 백도가 나오면 턴 스킵
        if (tossResult == TossResult.BACKDO) {
            boolean allAtStart = getCurrentPlayer().getTokens().stream().allMatch(token -> token.getPosition().isStart());
            boolean cantMove = YutResults.stream().allMatch(e -> e.equals(TossResult.BACKDO));
            pcs.firePropertyChange("cantMove", null, cantMove&allAtStart);
            if (allAtStart && cantMove) {
                getCurrentPlayer().addTurn(-1);
                nextTurn();
            }
        }
        return tossResult;
    }


    /* 말 관련 함수
     * updateCurrentToken: 현재 플레이어의 말 중 완주하지 않은 첫 번째 말을 가져옴
     * selectPosition: UI에서 보드 클릭할 때의 상호작용에서 사용
     * selectToken: UI에서 말 클릭할 때의 상호작용에서 사용
     * applyMoveTo: 선택된 말에 윷 던지기 결과를 적용
     * handleStacking: 말을 업을 때 사용
     * handleCapturing: 말을 잡을 때 사용
     * getTokensAt: 어떤 노드에 있는 모든 말을 반환하는 helper 함수
     */
    private void updateCurrentToken() {
        for (Token token : getCurrentPlayer().getTokens()) {
            if (!token.isFinished()) {
                currentToken = token;
                return;
            }
        }
    }

    public Position selectPosition(int pid) {
        return board.getPositions().get(pid);
    }

    public void selectToken(int tokenIndex) {
        // 본인의 말만 선택 가능
        if (currentPlayerId == tokenIndex/10) {
            currentToken = players.get(currentPlayerId).getTokens().get(tokenIndex % 10);
            System.out.printf("[Game] Token%d 선택됨. (현재 위치: %d)\n", currentToken.getId(), currentToken.getPosition().getId());
        }
    }

    public boolean applyMoveTo(Position dest) {
        for (TossResult result : YutResults) {
            if (rules.checkMove(currentToken.getPosition(), dest, result)) {
                // 도착 노드에 있는 말 찾기
                currentMove = result;
                capturedTokens.clear();
                List<Token> tokensOnDest = getTokensAt(dest);

                // 말 먼저 이동
                currentToken.moveTo(dest);
                currentToken.getOwner().addTurn(-1); // 움직였으면 현재 턴 수에서 -1
                YutResults.remove(result);

                // 도착 칸이 도착점이면 종료 여부 확인 후 종료 처리
                if (dest.isGoal()) {
                    currentToken.setFinished(true);
                    pcs.firePropertyChange("tokenFinished", null, currentToken);
                    nextTurn();
                    checkPlayerWin();

                    return true;
                }

                // 말 잡기/업기 확인 (현재 플레이어 소유의 임시 말 생성)
                Token tempToken = new Token(50, getCurrentPlayer());
                tempToken.moveTo(dest);

                if (!tokensOnDest.isEmpty()) {
                    handleCapturing(tempToken, dest);
                    handleStacking(tempToken, dest);
                }

                // UI 갱신용 이벤트
                pcs.firePropertyChange("tokenMoved", null, currentToken);
                for (Token t : currentToken.getStackedTokens()) {
                    pcs.firePropertyChange("tokenMoved", null, t);
                }

                nextTurn();
                return true;
            }
        }
        return false;
    }

    public void handleCapturing(Token token, Position position) {
        // 도착 노드에 있는 말 확인
        List<Token> capturableTokens = getTokensAt(position);

        // 잡을 수 있는 말을 찾고, 처리
        for (Token targetToken: capturableTokens) {
            if (rules.canCapture(token, targetToken, currentMove)) {
                capturedTokens.add(targetToken);
                capturedTokens.addAll(targetToken.getStackedTokens());
                capturableTokens.removeAll(targetToken.getStackedTokens());

                System.out.printf("[Game] Token%d", targetToken.getId());
                for (Token stackedToken: targetToken.getStackedTokens()) {
                    System.out.printf(", Token%d", stackedToken.getId());
                }
                System.out.println("을(를) 잡았습니다.");

                if (currentMove.getValue() < 4) {
                    pcs.firePropertyChange("turnsLeft", null, 0); // showTossButton() 실행
                }
            }
        }
        capturedTokens.forEach(Token::reset);
        pcs.firePropertyChange("capturedTokens", null, capturedTokens); // UI: 업힌 말 모두 리셋
    }

    public void handleStacking(Token carrier, Position position) {
        // 도착 노드에 있는 말 확인
        List<Token> stackableTokens = getTokensAt(position);

        // 업을 수 있는 말을 찾고, 처리
        for (Token other : stackableTokens) {
            if (rules.canStack(carrier, other)) {
                currentToken.stackWith(other);
                System.out.printf("[Game] Token%d와 Token%d을(를) 업었습니다.\n", currentToken.getId(), other.getId());
            }
        }
    }

    public List<Token> getTokensAt(Position position) {
        List<Token> tokens = new ArrayList<>();
        for (Player player : players) {
            for (Token t : player.getTokens()) {
                if (t.getId() != currentToken.getId() && t.getPosition().getId() == position.getId() && !t.isFinished()) {
                    tokens.add(t);
                }
            }
        }
        return tokens;
    }

    public int checkAutoControl() {
        // 현재 상태 업데이트 및 관련 변수 초기화
        List<Token> tokens = getCurrentPlayer().getTokens();

        boolean allStacked = currentToken.getStackedTokens().size() == (getCurrentPlayer().getRemainingTokens()-1);
        boolean allAtStart = tokens.stream().allMatch(t -> t.getPosition().isStart());

        System.out.println(allAtStart + " " + allStacked + " " + YutResults.size());

        // 보드에 말이 없거나, 남은 말이 하나거나, 남은 말이 모두 업혀 있는 상태라면 자동으로 이동
        if ((allAtStart || allStacked || getCurrentPlayer().getRemainingTokens() == 1) && !YutResults.isEmpty()) {
            if (YutResults.get(0).getValue() < 4 || allStacked) {
                // 빽도는 보드에 말이 있는 경우에만 작동하고, 없는 경우엔 바로 다음 턴으로 넘어감
                if (!allAtStart && YutResults.get(0) == TossResult.BACKDO) {
                    return currentToken.getPosition().getPrevPositions().get(0).getId();
                }

                if (YutResults.get(0).getValue() > 0) {
                    List<Position> positions = currentToken.getPosition().getNextPositions();
                    boolean flag = currentToken.getPosition().getId() == 25 && shapeType == 4;
                    Position q = null;
                    for (Position p : positions) {
                        q = p;
                        for (int i = 1; i < YutResults.get(0).getValue(); i++) {
                            if (q.isGoal()) {
                                return q.getId();
                            }
                            if (shapeType == 4 && q.getId() == 25) { flag = true; }
                            if (flag && q.getId() == 28) {
                                System.out.println("hi");
                                q = q.getNextPositions().get(1);
                                flag = false;
                            }
                            else { q = q.getNextPositions().get(0); }
                        }
                    }
                    return q.getId();
                }
            }
        }
        return -1;
    }

    // getters
    public Player getPrevPlayer() { return players.get((currentPlayerId-1+playerCount)%playerCount); }
    public Player getCurrentPlayer() { return players.get(currentPlayerId); }
    public List<Player> getPlayers() { return players; }
    public Position idToPosition(int id) { return board.getPositions().get(id); }

}