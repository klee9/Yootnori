package ui.fx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ui.interfaces.PlayerInfoPanel;

public class PlayerInfoPanelFX extends HBox implements PlayerInfoPanel {
    private Label[] playerLabels;
    private final Color[] colors = {
            Color.CORNFLOWERBLUE,
            Color.LIGHTCORAL,
            Color.GOLD,
            Color.LIGHTGREEN
    };

    public PlayerInfoPanelFX(int playerCount, int tokenCount) {
        this.setSpacing(10); // Label 사이의 간격이 10
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: white;");

        playerLabels = new Label[playerCount];

        for (int i = 0; i < playerCount; i++) {
            Label label = new Label();
            label.setFont(Font.font("맑은 고딕", 14));
            label.setTextFill(Color.BLACK);
            label.setAlignment(Pos.CENTER);
            label.setMinWidth(120);
            label.setMinHeight(60);
            label.setBackground(new Background(new BackgroundFill(colors[i], CornerRadii.EMPTY, null)));

            if (i == 0) {
                label.setText("Player " + (i + 1) + " ◀\n남은 말: " + tokenCount + "개");
                label.setStyle(label.getStyle() + " -fx-font-weight: bold;");
            } else {
                label.setText("Player " + (i + 1) + "\n남은 말: " + tokenCount + "개");
            }

            playerLabels[i] = label;
            this.getChildren().add(label);
        }
    }

    @Override
    public void updateCurrentPlayer(int playerIndex, int remainingPrev, int remainingNext) {
        System.out.println("[InfoPanel] Player " + (playerIndex + 1) + "의 정보가 최신화되었습니다.");
        int prevIndex = (playerIndex - 1 + playerLabels.length) % playerLabels.length;
        playerLabels[prevIndex].setText("Player " + (prevIndex + 1) + "\n남은 말: " + remainingPrev + "개");
        playerLabels[playerIndex].setStyle("-fx-font-weight: normal;");

        playerLabels[playerIndex].setText("Player " + (playerIndex + 1) + " ◀\n남은 말: " + remainingNext + "개");
        playerLabels[playerIndex].setStyle("-fx-font-weight: bold;");
    }
}