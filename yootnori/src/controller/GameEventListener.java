package controller;

import model.Position;
import model.Token;
import model.TossResult;

public interface GameEventListener {
    void onSelectPlayerCount(int playerCount);
    void onSelectTokenCount(int tokenCount);
    void onSelectShape(String shape);
    void onGameStart(int playerCount, int tokenCount, String shapeType);

    TossResult onRandomToss();
    TossResult onSpecifiedToss(TossResult tossResult);

    Position onClickPosition(int positionIndex);
    void onClickToken(int tokenIndex);
    boolean onMoveTokens(Position destination);
    void onStackTokens(Token token, Position position);
    void onConfirmCaptureTokens(Token token, Position position);

    int onUpdateCurrentPlayer();
    int onUpdateRemainingTokens();
    void onConfirmExit();
    void onConfirmRestart();
}