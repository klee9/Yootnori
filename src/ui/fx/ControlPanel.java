package ui.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import controller.GameController;
import model.Player;
import model.Token;
import model.TossResult;
import ui.swing.BoardPanel;
import ui.swing.TokenPanel;

import java.util.ArrayList;
import java.util.List;


public class ControlPanel extends HBox {
    private final GameController controller;
    private final BoardPanel board;
    private TokenPanel tokenPanel;

    Button randomYutButton = new Button("랜덤 윷 던지기");
    Button fixedYutButton = new Button("지정 윷 던지기");

    private List<Label> resultLabels = new ArrayList<>();
    private TossResult tossResult;

    public ControlPanel(GameController controller, BoardPanel board) {
        this.controller = controller;
        this.board = board;
        setSpacing(12);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));
        randomYutButton.setOnAction(e -> handleRandomToss());
        fixedYutButton.setOnAction(e -> showFixedTossMenu());

        getChildren().addAll(randomYutButton, fixedYutButton);
    }

    private void handleRandomToss() {
        System.out.println("랜덤 윷 던지기 클릭됨");
        tossResult = controller.onRandomToss();
        board.showYutImage(tossResult);
        showResultOnly(resultTranslate(tossResult));
        //가능한 경로를 가져와서 마지막 경로 선택
        autoMoveIfPossible();
    }

    private void showFixedTossMenu() {
        System.out.println("지정 윷 던지기 클릭됨");
        ContextMenu menu = new ContextMenu();
        for (TossResult res : TossResult.values()) {
            MenuItem item = new MenuItem(res.name());
            item.setOnAction(ev -> {
                tossResult = controller.onSpecifiedToss(res);
                board.showYutImage(tossResult);
                showResultOnly(resultTranslate(tossResult));
                //가능한 경로를 가져와서 마지막 경로 선택
                autoMoveIfPossible();
            });
            menu.getItems().add(item);
        }
        menu.show(fixedYutButton, Side.BOTTOM, 0, 0);
    }

    private void autoMoveIfPossible() {
        int autoMove = controller.onAutoControl();
        if (autoMove != -1) {
            int posId = board.autoClickPosition(autoMove);
            int tokenId = controller.getCurrentTokenId();
            List<Token> stacked = controller.findTokenById(tokenId).getStackedTokens();
            tokenPanel.updateTokenPosition(tokenId, posId);
            for (Token t : stacked) {
                tokenPanel.updateTokenPosition(t.getId(), posId);
            }
            controller.onMoveTokens(controller.findPositionById(posId));
        }
    }

    private String resultTranslate(TossResult res) {
        return switch (res) {
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
            case BACKDO -> "빽도";
        };
    }

    private void showResultOnly(String result) {
        if (tossResult != TossResult.YUT && tossResult != TossResult.MO) {
            hideTossButtons();
        }

        //결과 라벨 생성 및 추가
        Label lbl = new Label(result);
        lbl.setFont(Font.font("맑은 고딕", 24));
        lbl.setPrefSize(75, 50);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-background-color:#FFF0C8; -fx-border-color:#C3864A; -fx-border-width:1;");
        resultLabels.add(lbl);
        getChildren().add(lbl);
    }

    private void hideTossButtons() { randomYutButton.setVisible(false); fixedYutButton.setVisible(false); }
    public void showTossButtons() { randomYutButton.setVisible(true); fixedYutButton.setVisible(true); resultLabels.forEach(this.getChildren()::remove); resultLabels.clear(); }
    public void setTokenPanel(TokenPanel tokenPanel) { this.tokenPanel = tokenPanel; }
    public void displayThrowResult(TossResult result){}
    public void updateTurnLabel(Player player){}
}