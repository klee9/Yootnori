package ui;

import model.Player;
import model.Token;
import model.TossResult;

public interface UiInterface {

    // 게임 설정
    int promptPlayerCount();           // 참여자 수 선택
    int promptTokenCount();            // 말 개수 선택
    String promptBoardShapeSelection(); // 판 모양 선택 (사각형, 오각형, 육각형)
    
    // 화면 출력
    void showGameScreen(int playerCount, int tokenCount, String shapeType);  // 판을 처음 표시할 때 (display 말고 이걸로)

    // 게임 진행
    void showPlayerTurn(Player player);        // 현재 턴 표시
    void showThrowResult(TossResult result);   // 윷 결과 표시
    void updatePosition(Token token);          // 말 이동 처리

    // 게임 종료
    void showEndScreen(Player winner);         // 승자 화면 표시
}

