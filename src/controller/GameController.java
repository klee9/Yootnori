package controller;

import ui.MainWindow;
import model.Game;
import model.Token;
import model.Position;

// Gets the user gestures from the UI and runs model's functions.
// Works as a mediator between Game and UI

public class GameController implements GameEventListener {
    private MainWindow mainWindow;
    private Game game;

    public GameController (MainWindow mainWindow) {
        this.mainWindow = mainWindow;
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
    public void onSelectShape(int shape) { game.setBoardShape(shape); }

    /* 윷 관련 로직
     * onRandomThrow(): 랜덤 윷 던지기 버튼 클릭 시 실행
     * onSpecifiedThrow(): 지정 윷 던지기 버튼 클릭 시 실행
     */
    @Override
    public void onRandomThrow() { game.randomThrow(); }
    @Override
    public void onSpecifiedThrow(int throwResult) { game.specifiedThrow(throwResult); }

    /* 말 관련 로직
     * onClickPosition: 말 이동 시 칸 누르면 실행
     * onClickToken: 이동할 말 누를 시 실행
     * onMoveTokens: 이동 로직. 위 함수를 파라미터로 사용
     * onStackTokensYes: 말 업겠다고 하면 실행
     * onCaptureTokensYes: 말 잡겠다고 하면 실행
     */
    @Override
    public void onClickPosition(int positionIndex) { game.selectPosition(positionIndex); } // TODO: 눌렀을 때 어떤 행동을 해야 하는지 판단
    @Override
    public void onClickToken(int tokenIndex) { game.selectToken(tokenIndex); }
    @Override
    public void onMoveTokens(Token token, Position destination, int throwResult) { game.applyMoveTo(token, destination, throwResult); }
    @Override
    public void onStackTokens(Token tokenA, Token tokenB) { game.handleStacking(tokenA, tokenB); }
    @Override
    public void onConfirmCaptureTokens(Token tokenA, Token tokenB) { game.handleCapturing(tokenA, tokenB); }

    /* 게임 흐름 로직
     * onExitYes(): 게임 종료 버튼 누르면 실행
     * onRestartYes(): 재시작 버튼 누르면 실행
     */
    @Override
    public void onConfirmExit() { game.endGame(); } // TODO: 게임 종료 거절 버튼을 누르면 UI상에서 뒤로가기만 수행하면 될 것 같아요
    @Override
    public void onConfirmRestart() { game.restartGame(); }

}
