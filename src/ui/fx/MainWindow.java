package ui.fx;

import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Player;
import model.Token;
import model.TossResult;
import ui.UiInterface;

import java.io.IOException;
import java.util.Optional;

public class MainWindow extends Application implements UiInterface {

    private StartPanel startPanel;
    private TokenPanel tokenPanel;
    private ControlPanel controlPanel;
    private GameController gameController;
    private BoardPanel boardPanel;
    private PlayerInfoPanel playerInfoPanel;

    public MainWindow() {
        Stage stage = new Stage();
        stage.setTitle("윷놀이 게임");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {

    }

    @Override
    public int promptPlayerCount() {
        return 0;
    }

    @Override
    public int promptTokenCount() {
        return 0;
    }

    @Override
    public String promptBoardShapeSelection() {
        return "";
    }

    @Override
    public void showGameScreen(int playerCount, int tokenCount, String shapeType) {

    }

    @Override
    public void showPlayerTurn(Player player) {

    }

    @Override
    public void showThrowResult(TossResult result) {

    }

    @Override
    public void updatePosition(Token token) {

    }

    @Override
    public void showEndScreen(Player winner) {
    }

    public static void main(String[] args) { launch(); }
}
