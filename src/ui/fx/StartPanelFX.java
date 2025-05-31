package ui.fx;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ui.interfaces.StartPanel;

public class StartPanelFX extends GridPane implements StartPanel {
    private final ComboBox<String> shapeCombo = new ComboBox<>();
    private final ComboBox<Integer> playerCombo = new ComboBox<>();
    private final ComboBox<Integer> tokenCombo = new ComboBox<>();

    public StartPanelFX(Runnable onStart) {
        setPadding(new Insets(20));
        setHgap(100);
        setVgap(12);
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        setAlignment(Pos.CENTER);

        // 이미지 (첫 행, 3개 컬럼 병합)
        Image img = new Image(getClass().getResource("/images/start_yut.png").toExternalForm(), 160, 160, true, true);
        ImageView imgView = new ImageView(img);
        add(imgView, 0, 0, 3, 1);  // columnSpan = 3
        setHalignment(imgView, HPos.CENTER);

        // 콤보박스 아이템 설정
        shapeCombo.getItems().addAll("사각형", "오각형", "육각형");
        playerCombo.getItems().addAll(2, 3, 4);
        tokenCombo.getItems().addAll(2, 3, 4, 5);

        shapeCombo.setValue("사각형");
        playerCombo.setValue(2);
        tokenCombo.setValue(2);

        styleComboBox(shapeCombo);
        styleComboBox(playerCombo);
        styleComboBox(tokenCombo);

        // 윷놀이 판: 라벨 + 콤보박스
        Label shapeLabel = new Label("윷놀이 판:");
        shapeLabel.setPrefWidth(80);
        shapeLabel.setAlignment(Pos.CENTER_RIGHT);
        add(shapeLabel, 0, 1);
        add(shapeCombo, 1, 1);

        // 참여자 수: 라벨 + 콤보박스
        Label playerLabel = new Label("참여자 수:");
        playerLabel.setPrefWidth(80);
        playerLabel.setAlignment(Pos.CENTER_RIGHT);
        add(playerLabel, 0, 2);
        add(playerCombo, 1, 2);

        // 말 개수: 라벨 + 콤보박스
        Label tokenLabel = new Label("말 개수:");
        tokenLabel.setPrefWidth(80);
        tokenLabel.setAlignment(Pos.CENTER_RIGHT);
        add(tokenLabel, 0, 3);
        add(tokenCombo, 1, 3);

        //시작 버튼
        Button startButton = new Button("게임 시작");
        startButton.setFont(Font.font("맑은 고딕", 16));
        startButton.setPrefSize(140, 40);
        startButton.setStyle("-fx-background-color: #FFAAAA;");
        startButton.setOnAction(e -> onStart.run());
        add(startButton, 0, 4, 3, 1);
        setHalignment(startButton, HPos.CENTER);
    }

    @Override
    public void styleComboBox(Object comboBox) {
        if (comboBox instanceof ComboBox<?> combo) {
            combo.setPrefSize(120, 30);
            combo.setStyle("-fx-background-color: #FFE6B4;");
        }
    }

    @Override
    public int getSelectedPlayerCount() { return playerCombo.getValue(); }
    @Override
    public int getSelectedTokenCount() { return tokenCombo.getValue(); }
    @Override
    public String getSelectedShapeType() { return shapeCombo.getValue(); }
}