package controller;

import model.Position;
import model.TossResult;
import ui.interfaces.ControlPanel;
import ui.interfaces.PlayerInfoPanel;
import ui.interfaces.TokenPanel;

public interface GameEventListener {
    void onGameStart(int playerCount, int tokenCount, String shapeType);
    void onConfirmRestart();

    TossResult onRandomToss();
    TossResult onSpecifiedToss(TossResult tossResult);

    void onClickToken(int tokenIndex);
    int onAutoControl();
    Position findPositionById(int id);
    Position onClickPosition(int positionIndex);
    boolean onMoveTokens(Position destination);

    void setControlPanel(ControlPanel controlPanel);
    void setInfoPanel(PlayerInfoPanel infoPanel);
    void setTokenPanel(TokenPanel tokenPanel);

    int getCurrentPlayerId();
}