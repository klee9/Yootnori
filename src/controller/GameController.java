package controller;

import model.*;
import ui.swing.ControlPanel;
import ui.swing.MainWindow;
import ui.swing.PlayerInfoPanel;
import ui.swing.TokenPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class GameController implements GameEventListener {
    private final MainWindow mainWindow;
    private PlayerInfoPanel infoPanel;
    private ControlPanel control;
    private TokenPanel tokenPanel;
    private final Game game;

    public GameController (MainWindow mainWindow, Game game) {
        this.game = game;
        this.mainWindow = mainWindow;
        game.addPropertyChangeListener(this::handleGameEvent);
    }

    // PropertyChangeListener 관련 함수
    private void handleGameEvent(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "winner" -> handleVictory((Player) evt.getNewValue());
            case "nextTurn" -> handleNextTurn();
            case "cantMove" -> handleTurnTransition();
            case "turnsLeft" -> handleTurnsLeft((int) evt.getNewValue());
            case "tokenMoved" -> handleTokenMoved((Token) evt.getNewValue());
            case "tokenFinished" -> handleTokenFinished((Token) evt.getNewValue());
            case "capturedTokens" -> handleTokenCaptured((List<Token>) evt.getNewValue());
        }
    }

    private void handleTokenFinished(Token token) {
        tokenPanel.updateTokenPosition(token.getId(), -1);
        for (Token t : token.getStackedTokens()) {
            tokenPanel.updateTokenPosition(t.getId(), -1);
        }
    }

    private void handleNextTurn() {
        infoPanel.updateCurrentPlayer(game.getCurrentPlayer().getId(), game.getPrevPlayer().getRemainingTokens(), game.getCurrentPlayer().getRemainingTokens());
        control.showTossButtons();
    }

    private void handleTurnTransition() {
        Timer timer = new Timer(1500, e -> { control.showTossButtons(); });
        timer.setRepeats(false);
        timer.start();
    }

    private void handleTurnsLeft(int remainingTurns) {
        if (remainingTurns == 0) {
            control.showTossButtons();
        }
    }

    private void handleVictory(Player winner) {
        mainWindow.showEndScreen(winner);
    }

    private void handleTokenCaptured(List<Token> capturedTokens) {
        for (Token t : capturedTokens) {
            System.out.println("captured token: " + t.getId());
            tokenPanel.updateTokenPosition(t.getId(), t.getInitialCoordinates());
        }
    }

    private void handleTokenMoved(Token token) {
        Position p = token.getPosition();
        int nodeIdx = p == null ? -1 : p.getId();
        tokenPanel.updateTokenPosition(token.getId(), nodeIdx);
    }

    // 게임 흐름 관련 로직
    @Override
    public void onGameStart(int playerCount, int tokenCount, String shapeType) {
        int shape;
        switch (shapeType) {
            case "사각형" -> shape = 4;
            case "오각형" -> shape = 5;
            case "육각형" -> shape = 6;
            default -> shape = 1;
        }
        game.startGame(playerCount, tokenCount, shape);
    }

    @Override
    public void onConfirmRestart() { game.restartGame(); }

    /* 윷 관련 로직
     * onRandomThrow(): 랜덤 윷 던지기 버튼 클릭 시 실행
     * onSpecifiedThrow(): 지정 윷 던지기 버튼 클릭 시 실행
     */
    @Override
    public TossResult onRandomToss() { return game.randomThrow(); }
    @Override
    public TossResult onSpecifiedToss(TossResult tossResult) { return game.specifiedThrow(tossResult); }

    /* 말 관련 로직
     * onAutoControl: 말을 자동으로 이동할 때 사용
     * onClickToken: 이동할 말 누를 시 실행
     * onMoveTokens: 말을 이동할 때 사용
     * onClickPosition: 말 이동 시 칸 누르면 실행
     */
    @Override
    public int onAutoControl() { return game.checkAutoControl(); }
    @Override
    public void onClickToken(int tokenIndex) { game.selectToken(tokenIndex); }
    @Override
    public boolean onMoveTokens(Position destination) { return game.applyMoveTo(destination); }
    @Override
    public Position onClickPosition(int positionIndex) { return game.selectPosition(positionIndex); }

    // setters
    public void setInfoPanel(PlayerInfoPanel infoPanel) { this.infoPanel = infoPanel; }
    public void setTokenPanel(TokenPanel tokenPanel) { this.tokenPanel = tokenPanel; }
    public void setControlPanel(ControlPanel control) { this.control = control; }

    // getters
    public int getCurrentPlayerId() { return game.getCurrentPlayer().getId(); }
    public Position findPositionById(int id) { return game.idToPosition(id); }
    public Token findTokenById(int id) {
        return game.getPlayers().stream()
                .flatMap(player -> player.getTokens().stream())
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

}