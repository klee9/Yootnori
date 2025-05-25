package ui.fx;

import controller.GameEventListener;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import model.Game;
import model.Player;

import controller.GameController;
import model.TossResult;
import ui.interfaces.MainWindow;

import java.util.Optional;

public class MainWindowFX extends Application implements MainWindow {

    private Scene scene;
    private Stage primaryStage;
    private BorderPane root;


    private StartPanelFX startPanel;
    private BoardPanelFX boardPanel;
    private ControlPanelFX controlPanel;
    private TokenPanelFX tokenPanel;
    private PlayerInfoPanelFX infoPanel;

    private Game game;
    private GameEventListener controller;

    @Override
    public void init() {
        game = new Game();
        controller = new GameController(this, game);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("윷놀이 게임");
        primaryStage.setResizable(false);

        root = new BorderPane();
        showStartScreen();

        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void restartGame() {
        showStartScreen();
    }

    @Override
    public void showStartScreen() {
        startPanel = new StartPanelFX(() -> {
            int playerCount = startPanel.getSelectedPlayerCount();
            int tokenCount = startPanel.getSelectedTokenCount();
            String shapeType = startPanel.getSelectedShapeType();
            controller.onGameStart(playerCount, tokenCount, shapeType);
            showGameScreen(playerCount, tokenCount, shapeType);
        });
        root.setCenter(startPanel);
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
        boardPanel = new BoardPanelFX(shapeType, this);
        controlPanel = new ControlPanelFX(this, boardPanel);
        tokenPanel = new TokenPanelFX(boardPanel, this, playerCount, tokenCount);
        infoPanel = new PlayerInfoPanelFX(playerCount, tokenCount);

        boardPanel.setTokenPanel(tokenPanel);
        controlPanel.setTokenPanel(tokenPanel);

        controller.setInfoPanel(infoPanel);
        controller.setTokenPanel(tokenPanel);
        controller.setControlPanel(controlPanel);

        StackPane layered = new StackPane();
        layered.setPrefSize(800, 400);
        layered.setMinSize(800, 400);
        layered.setMaxSize(800, 400);
        layered.getChildren().addAll(boardPanel, tokenPanel);

        tokenPanel.repaint();

        BorderPane gamePane = new BorderPane();
        gamePane.setTop(infoPanel);
        gamePane.setCenter(layered);
        gamePane.setBottom(controlPanel);

        root.setCenter(gamePane);
    }

    @Override
    public void showEndScreen(Player winner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("게임 종료");
        alert.setHeaderText(winner.getName() + "님이 승리하셨습니다!");
        alert.setContentText("게임을 다시 시작하시겠습니까?");

        ButtonType restartButton = new ButtonType("다시 시작");
        ButtonType exitButton = new ButtonType("종료");

        alert.getButtonTypes().setAll(restartButton, exitButton);

        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == restartButton) {
            controller.onConfirmRestart();
            restartGame();
        } else if (choice.isPresent() && choice.get() == exitButton) {
            primaryStage.close();
        }
    }

    public void run() { launch(); }

    public void onClickToken(int tid) { controller.onClickToken(tid); }
    public boolean onMoveTokens(int pid) { return controller.onMoveTokens(controller.onClickPosition(pid)); }

    public int onAutoControl() { return controller.onAutoControl(); }
    public int getCurrentPlayerId() { return controller.getCurrentPlayerId(); }

    public TossResult onRandomToss() { return controller.onRandomToss(); }
    public TossResult onSpecifiedToss(TossResult tossResult) { return controller.onSpecifiedToss(tossResult); }
}
