package ui.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import controller.GameController;
import model.TossResult;
import ui.interfaces.ControlPanel;

import java.util.ArrayList;
import java.util.List;


public class ControlPanelFX extends HBox implements ControlPanel {
    private final GameController controller;
    private final BoardPanelFX board;
    private TokenPanelFX tokenPanel;

    Button randomYutButton = new Button("랜덤 윷 던지기");
    Button fixedYutButton = new Button("지정 윷 던지기");

    private List<Label> resultLabels = new ArrayList<>();
    private TossResult tossResult;

    public ControlPanelFX(GameController controller, BoardPanelFX board) {
        this.controller = controller;
        this.board = board;
        setSpacing(12);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));

        setPrefHeight(90);
        setMinHeight(90);
        setMaxHeight(90);

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
            controller.onMoveTokens(controller.findPositionById(posId));
        }
    }

    @Override
    public String resultTranslate(TossResult res) {
        return switch (res) {
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
            case BACKDO -> "빽도";
        };
    }

    @Override
    public void showResultOnly(String result) {
        if (tossResult != TossResult.YUT && tossResult != TossResult.MO) {
            hideTossButtons();
        }

        //결과 라벨 생성 및 추가
        Label lbl = new Label(result);
        lbl.setFont(Font.font("맑은 고딕", 24));
        lbl.setPrefSize(75, 40);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-background-color:#FFF0C8; -fx-border-color:#C3864A; -fx-border-width:1;");
        resultLabels.add(lbl);
        getChildren().add(lbl);
    }

    private void hideTossButtons() { randomYutButton.setVisible(false); fixedYutButton.setVisible(false); }
    public void showTossButtons() { randomYutButton.setVisible(true); fixedYutButton.setVisible(true); resultLabels.forEach(this.getChildren()::remove); resultLabels.clear(); }
    public void setTokenPanel(TokenPanelFX tokenPanel) { this.tokenPanel = tokenPanel; }
}