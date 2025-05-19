package ui.interfaces;

import controller.GameController;
import model.Player;

public interface MainWindow {
    // 게임 설정
    int promptPlayerCount();                       // 참여자 수 선택
    int promptTokenCount();                        // 말 개수 선택
    String promptBoardShapeSelection();            // 판 모양 선택 (사각형, 오각형, 육각형)
    void setController(GameController controller); // controller 설정

    // 화면 출력
    void showStartScreen();
    void showGameScreen(int playerCount, int tokenCount, String shapeType);  // 판을 처음 표시할 때 (display 말고 이걸로)

    // 게임 종료 및 재시작
    void restartGame();
    void showEndScreen(Player winner);         // 승자 화면 표시
}
