package controller;

import model.Position;
import model.Token;

public interface GameEventListener {
    void onSelectPlayerCount(int playerCount);
    void onSelectTokenCount(int tokenCount);
    void onSelectShape(int shape);

    void onRandomThrow();
    void onSpecifiedThrow(int throwResult);

    void onClickPosition(int positionIndex);
    void onClickToken(int tokenIndex);
    void onMoveTokens(Token token, Position destination, int throwResult);
    void onStackTokens(Token tokenA, Token tokenB);
    void onConfirmCaptureTokens(Token tokenA, Token tokenB);

    void onConfirmExit();
    void onConfirmRestart();
}
