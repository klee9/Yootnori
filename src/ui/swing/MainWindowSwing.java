package ui.swing;

import model.Player;

import javax.swing.*;

import controller.GameController;
import ui.interfaces.MainWindow;

import java.awt.*;

public class MainWindowSwing extends JFrame implements MainWindow {

    private GameController controller;
    private StartPanelSwing startPanel;
    private TokenPanelSwing tokenPanel;
    private BoardPanelSwing boardPanel;
    private ControlPanelSwing controlPanel;
    private PlayerInfoPanelSwing infoPanel;

    public MainWindowSwing() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        showStartScreen();
    }

    @Override
    public void restartGame() {
        showStartScreen();
    }

    @Override
    public void showStartScreen() {
        startPanel = new StartPanelSwing(e -> {
            int players = promptPlayerCount();
            int tokens = promptTokenCount();
            String shape = promptBoardShapeSelection();

            controller.onGameStart(players, tokens, shape);
            showGameScreen(players,tokens,shape); // 판 그리기 화면으로 전환
        });
        setContentPane((JPanel) startPanel);
        revalidate();
        repaint();
    }

    @Override
    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public int promptPlayerCount() {
        return startPanel.getSelectedPlayerCount();
    }

    @Override
    public int promptTokenCount() {
        return startPanel.getSelectedTokenCount();
    }

    @Override
    public String promptBoardShapeSelection() {
        return startPanel.getSelectedShapeType();
    }

    @Override
    public void showGameScreen(int playerCount, int tokenCount, String shapeType) {
        boardPanel = new BoardPanelSwing(shapeType, controller);
        controlPanel = new ControlPanelSwing(controller, boardPanel);
        tokenPanel = new TokenPanelSwing(boardPanel, controlPanel, startPanel, controller);
        boardPanel.setTokenPanel(tokenPanel);
        controlPanel.setTokenPanel(tokenPanel);
        infoPanel = new PlayerInfoPanelSwing(playerCount, tokenCount);
        controller.setInfoPanel(infoPanel);
        controller.setTokenPanel(tokenPanel);
        controller.setControlPanel(controlPanel);

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(boardPanel.getPreferredSize());

        boardPanel.setBounds(0, 0, 800, 400);
        tokenPanel.setBounds(0, 0, 800, 400);

        layered.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        layered.add(tokenPanel, JLayeredPane.PALETTE_LAYER);

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(infoPanel, BorderLayout.NORTH);
        gamePanel.add(layered, BorderLayout.CENTER);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    @Override
    public void showEndScreen(Player winner) {
        int choice = JOptionPane.showOptionDialog(
                this,
                winner.getName() + "님이 승리하셨습니다!\n게임을 다시 시작하시겠습니까?",
                "게임 종료",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"다시 시작", "종료"},
                "다시 시작"
        );

        if (choice == JOptionPane.YES_OPTION) {
            controller.onConfirmRestart();
            restartGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}