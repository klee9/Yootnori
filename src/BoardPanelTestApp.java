package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// BoardPanelFX 테스트하기 위한 임시 endpoint
public class BoardPanelTestApp extends Application {
  private static final int BOARD_SIZE   = 600;
  private static final int WINDOW_MARGIN = 40;
  private static final String SHAPE_TYPE = "오각형"; // "사각형", "오각형", "육각형"으로 바꿔서 테스트

  @Override
  public void start(Stage primaryStage) {
    BoardPanelFX boardPanelFX = new BoardPanelFX(2, 2, SHAPE_TYPE, null);
    boardPanelFX.setPrefSize(BOARD_SIZE, BOARD_SIZE);
    boardPanelFX.setMaxSize(BOARD_SIZE, BOARD_SIZE);
    boardPanelFX.setClickable(false);

    StackPane root = new StackPane(boardPanelFX);
    root.setPadding(new Insets(WINDOW_MARGIN));

    Scene scene = new Scene(root);
    primaryStage.setTitle("BoardPanelFX 테스트");
    primaryStage.setScene(scene);
    primaryStage.setWidth(BOARD_SIZE);
    primaryStage.setHeight(BOARD_SIZE);
    primaryStage.show();
  }

  public static void main(String[] args) { launch(args); }
}
