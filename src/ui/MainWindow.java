package ui;

import model.Player;
import model.Token;
import model.TossResult;

import javax.swing.*;

import controller.GameController;

import java.awt.*;

public class MainWindow extends JFrame implements UiInterface {

    private StartPanel startPanel;
    private TokenPanel tokenPanel;
    private ControlPanel controlPanel;
    private GameController controller;
    private BoardPanel boardPanel;
    private PlayerInfoPanel infoPanel;

    public MainWindow() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        showStartScreen();
    }

    private void restartGame() {
        showStartScreen();
    }

    private void showStartScreen() {
        startPanel = new StartPanel(_ -> {
            int players = promptPlayerCount();
            int tokens = promptTokenCount();
            String shape = promptBoardShapeSelection();

            controller.onGameStart(players, tokens, shape);
            showGameScreen(players,tokens,shape); // 판 그리기 화면으로 전환
        });
        setContentPane(startPanel);
        revalidate();
        repaint();
    }

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
        boardPanel = new BoardPanel(playerCount, tokenCount, shapeType, controller);
        controlPanel = new ControlPanel(controller, boardPanel);
        tokenPanel = new TokenPanel(boardPanel, controlPanel, startPanel, controller);
        boardPanel.setTokenPanel(tokenPanel);
        infoPanel = new PlayerInfoPanel(playerCount, tokenCount);
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
    public void showThrowResult(TossResult result) {
        controlPanel.displayThrowResult(result);
    }

    @Override
    public void showPlayerTurn(Player player) {
        controlPanel.updateTurnLabel(player);
    }

    @Override
    public void updatePosition(Token token) {
        tokenPanel.repaint();
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
            controller.onConfirmExit();
            System.exit(0);
        }
    }

    public PlayerInfoPanel getInfoPanel() {
        return infoPanel;
    }
}