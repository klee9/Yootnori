import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ui.BoardPanel;
import ui.ControlPanel;
import ui.PlayerInfoPanel;
import ui.StartPanel;
import ui.TokenPanel;

public class Main extends Application {
    private BorderPane root;
    private Scene scene;
    private StartPanel startPanel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("윷놀이 게임");
        primaryStage.setResizable(false);

        root = new BorderPane();
        showStartScreen();

        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showStartScreen() {
        startPanel = new StartPanel(() -> {
            int playerCount = startPanel.getSelectedPlayerCount();
            int tokenCount = startPanel.getSelectedTokenCount();
            String shapeType = startPanel.getSelectedShapeType();
            showGameScreen(playerCount, tokenCount, shapeType);
        });
        root.setCenter(startPanel);
    }

    private void showGameScreen(int playerCount, int tokenCount, String shapeType) {
        GameController dummyController = null;

        BoardPanel boardPanel = new BoardPanel(playerCount, tokenCount, shapeType, dummyController);
        boardPanel.setPrefSize(800, 400);
        TokenPanel tokenPanel = new TokenPanel(boardPanel, null, startPanel, dummyController);
        tokenPanel.setPrefSize(800, 400);
        boardPanel.setTokenPanel(tokenPanel);

        ControlPanel controlPanel = new ControlPanel(dummyController, boardPanel);
        PlayerInfoPanel infoPanel = new PlayerInfoPanel(playerCount, tokenCount);

        StackPane layered = new StackPane();
        layered.setPrefSize(800, 400);
        layered.getChildren().addAll(boardPanel, tokenPanel);
        // 1번 방법: 토큰 패널 초기 한 번 강제 리페인트
        tokenPanel.repaint();

        BorderPane gamePane = new BorderPane();
        gamePane.setTop(infoPanel);
        gamePane.setCenter(layered);
        gamePane.setBottom(controlPanel);

        root.setCenter(gamePane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
