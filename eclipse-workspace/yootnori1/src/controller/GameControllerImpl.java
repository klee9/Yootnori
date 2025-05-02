package controller;

import view.MainWindow;

public class GameControllerImpl implements GameController {
 private MainWindow mainWindow;

 public GameControllerImpl(MainWindow mainWindow) {
     this.mainWindow = mainWindow;
 }

 @Override
 public void startGame(int playerCount, int tokenCount, String shapeType) {
     System.out.println("게임 시작!");
     // 나중엔 여기서 Game 모델 초기화하고
     mainWindow.showGameScreen(playerCount, tokenCount, shapeType);
 }
}