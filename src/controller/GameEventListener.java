package controller;

import model.Position;
import model.TossResult;

public interface GameEventListener {
    void onGameStart(int playerCount, int tokenCount, String shapeType);
    void onConfirmRestart();

    TossResult onRandomToss();
    TossResult onSpecifiedToss(TossResult tossResult);

    void onClickToken(int tokenIndex);
    int onAutoControl();
    Position onClickPosition(int positionIndex);
    boolean onMoveTokens(Position destination);
}