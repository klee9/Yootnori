package view;

import java.awt.BorderLayout;

import javax.swing.*;
import controller.GameController;
import controller.GameControllerImpl;

public class MainWindow extends JFrame {
    private GameController controller;
    private StartPanel startPanel;

    public MainWindow(GameController controller) {
        this.controller = controller;

        setTitle("윷놀이 게임");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        showStartScreen();
    }

    private void showStartScreen() {
        startPanel = new StartPanel(e -> onStartGame());
        setContentPane(startPanel);
        revalidate();
    }

    private void onStartGame() { // 게임 시작 버튼 클릭 시 실행
        int players = startPanel.getSelectedPlayerCount();
        int tokens = startPanel.getSelectedTokenCount();
        String shape = startPanel.getSelectedShapeType();

        controller.startGame(players, tokens, shape);
    }
    
    public void showGameScreen(int playerCount, int tokenCount, String shapeType) {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        BoardPanel boardPanel = new BoardPanel(playerCount, tokenCount, shapeType);  // 윷놀이 판
        ControlPanel controlPanel = new ControlPanel(controller);  // 윷 버튼 등

        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        setContentPane(gamePanel);
        revalidate();
        repaint();
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. MainWindow 먼저 생성 (Controller 없이)
            MainWindow window = new MainWindow(null);

            // 2. 진짜 GameControllerImpl 생성 (MainWindow를 전달)
            GameController controller = new GameControllerImpl(window);

            // 3. MainWindow에 Controller 설정
            window.setController(controller);

            // 4. 보여주기
            window.setVisible(true);
        });
    }

}
