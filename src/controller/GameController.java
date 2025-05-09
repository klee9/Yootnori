package controller;

import model.*;
import ui.ControlPanel;
import ui.MainWindow;
import ui.PlayerInfoPanel;
import ui.TokenPanel;

import javax.swing.*;
import java.util.List;

// Gets the user gestures from the UI and runs model's functions.
// Works as a mediator between Game and UI

public class GameController implements GameEventListener {
    private MainWindow mainWindow;
    private PlayerInfoPanel infoPanel;
    private ControlPanel control;
    private TokenPanel tokenPanel;
    private Game game;

    public GameController (MainWindow mainWindow, Game game) {
        this.mainWindow = mainWindow;
        this.game = game;

        game.addPropertyChangeListener(evt -> {
            if ("tokenFinished".equals(evt.getPropertyName())) {
                int tokenId = (int) evt.getNewValue();
                tokenPanel.updateTokenPosition(tokenId, -1);
            }
        });

        game.addPropertyChangeListener(evt -> {
            if ("nextTurn".equals(evt.getPropertyName())) {
                infoPanel.updateCurrentPlayer(game.getCurrentPlayer().getPlayerId(), game.getPrevPlayer().getRemainingTokens(), game.getCurrentPlayer().getRemainingTokens());
                control.showTossButtons();
            }
        });

        game.addPropertyChangeListener(evt -> {
            if ("cantMove".equals(evt.getPropertyName())) {
                boolean cantMove = (boolean) evt.getNewValue();
                Timer timer = new Timer(3000, e -> {
                    control.showTossButtons();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        game.addPropertyChangeListener(evt -> {
            if ("turnsLeft".equals(evt.getPropertyName())) {
                int turnsLeft = (int) evt.getNewValue();
                if (turnsLeft == 0) {
                    control.showTossButtons();
                }
            }
        });

        game.addPropertyChangeListener(evt -> {
            if ("winner".equals(evt.getPropertyName())) {
                Player winner = (Player) evt.getNewValue();
                mainWindow.showEndScreen(winner);
            }
        });
    }

    /* 게임 시작 로직
     * onSelectPlayerCount: 플레이어 수 설정 시 실행
     * onSelectTokenCount: 플레이어가 사용할 토큰 수 설정 시 실행
     * onSelectShape: 보드 모양 설정 시 실행
     */
    @Override
    public void onSelectPlayerCount(int playerCount) { game.setPlayerCount(playerCount); }
    @Override
    public void onSelectTokenCount(int tokenCount) { game.setTokenCount(tokenCount); }
    @Override
    public void onSelectShape(String shape) { game.setBoardShape(shape); }
    @Override
    public void onGameStart(int playerCount, int tokenCount, String shapeType) { game.startGame(playerCount, tokenCount, shapeType); }

    /* 윷 관련 로직
     * onRandomThrow(): 랜덤 윷 던지기 버튼 클릭 시 실행
     * onSpecifiedThrow(): 지정 윷 던지기 버튼 클릭 시 실행
     */
    @Override
    public TossResult onRandomToss() { return game.randomThrow(); }
    @Override
    public TossResult onSpecifiedToss(TossResult tossResult) { return game.specifiedThrow(tossResult); }

    /* 말 관련 로직
     * onClickPosition: 말 이동 시 칸 누르면 실행
     * onClickToken: 이동할 말 누를 시 실행
     * onMoveTokens: 이동 로직. 위 함수를 파라미터로 사용
     * onStackTokensYes: 말 업겠다고 하면 실행
     * onCaptureTokensYes: 말 잡겠다고 하면 실행
     */
    @Override
    public Position onClickPosition(int positionIndex) { return game.selectPosition(positionIndex); } // TODO: 눌렀을 때 어떤 행동을 해야 하는지 판단
    @Override
    public void onClickToken(int tokenIndex) { game.selectToken(tokenIndex); }
    @Override
    public boolean onMoveTokens(Position destination) {
        boolean moveSuccess = game.applyMoveTo(destination);

        if (moveSuccess) {
            List<Token> captured = game.getCapturedTokens();
            for (Token t : captured) {
                tokenPanel.updateTokenPosition(t.getId(), t.getStartPosition());
            }
        }
        return moveSuccess;
    }
    @Override
    public void onStackTokens(Token token, Position position) { game.handleStacking(token, position); }
    @Override
    public void onConfirmCaptureTokens(Token token, Position position) { game.handleCapturing(token, position); }

    /* 게임 흐름 로직
     * onExitYes(): 게임 종료 버튼 누르면 실행
     * onRestartYes(): 재시작 버튼 누르면 실행
     */
    @Override
    public boolean onNextTurn() { return game.getCurrentPlayer().getTurns() == 0; }
    @Override
    public int onUpdateCurrentPlayer() { return game.getCurrentPlayer().getPlayerId(); }
    @Override
    public int onUpdateRemainingTokens() { return game.getCurrentPlayer().getRemainingTokens(); }
    @Override
    public void onConfirmExit() { game.endGame(); } // TODO: 게임 종료 거절 버튼을 누르면 UI상에서 뒤로가기만 수행하면 될 것 같아요
    @Override
    public void onConfirmRestart() { game.restartGame(); }

    public void setInfoPanel(PlayerInfoPanel infoPanel) { this.infoPanel = infoPanel; }
    public void setTokenPanel(TokenPanel tokenPanel) { this.tokenPanel = tokenPanel; }
    public void setControlPanel(ControlPanel control) { this.control = control; }
    public int getCurrentPlayerId() { return game.getCurrentPlayer().getPlayerId(); }
}