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

    public MainWindow() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

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
        tokenPanel = new TokenPanel(boardPanel, startPanel, controller);
        boardPanel.setTokenPanel(tokenPanel);
        controlPanel = new ControlPanel(controller, boardPanel);
        PlayerInfoPanel infoPanel = new PlayerInfoPanel(playerCount, tokenCount);

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

        infoPanel.updateCurrentPlayer(controller.onUpdateCurrentPlayer(), controller.onUpdateRemainingTokens());
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
        JOptionPane.showMessageDialog(this, winner.getName() + "님이 승리하셨습니다!");
    }
}