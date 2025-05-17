package ui;

import javax.swing.*;

import controller.GameController;
import model.Player;
import model.Token;
import model.TossResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel {
    private GameController controller;
    private BoardPanel board;
    private TokenPanel tokenPanel;

    JButton randomYutButton = new JButton("랜덤 윷 던지기");
    JButton fixedYutButton = new JButton("지정 윷 던지기");
    private JLabel resultLabel;
    private List<JLabel> resultLabels = new ArrayList<>();
    private TossResult tossResult;

    public ControlPanel(GameController controller, BoardPanel board) {
        this.controller = controller;
        this.board = board;

        setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        randomYutButton.addActionListener(e -> {
            System.out.println("랜덤 윷 던지기 클릭됨");
            tossResult = controller.onRandomToss();
            board.showYutImage(tossResult);
            showResultOnly(resultTranslate(tossResult));
            // 가능한 경로를 가져와서 마지막 경로 선택
            int autoMove = controller.onAutoControl();
            if (autoMove != -1) {
                int posId = board.autoClickPosition(autoMove);
                controller.onMoveTokens(controller.findPositionById(posId));
            }
        });

        fixedYutButton.addActionListener(e -> {
            System.out.println("지정 윷 던지기 클릭됨");
            JPopupMenu menu = new JPopupMenu();
            for (TossResult res : TossResult.values()) {
                JMenuItem item = new JMenuItem(res.name());
                item.addActionListener(ev -> {
                    tossResult = controller.onSpecifiedToss(res);
                    board.showYutImage(tossResult);
                    showResultOnly(resultTranslate(tossResult));
                    // 가능한 경로를 가져와서 마지막 경로 선택
                    int autoMove = controller.onAutoControl();
                    if (autoMove != -1) {
                        int posId = board.autoClickPosition(autoMove);
                        controller.onMoveTokens(controller.findPositionById(posId));
                    }
                });
                menu.add(item);
            }
            menu.show(fixedYutButton, 0, fixedYutButton.getHeight());
        });

        add(randomYutButton);
        add(fixedYutButton);
    }

    // 던지기 버튼 모두 숨기는 메소드
    private void hideTossButtons() {
        randomYutButton.setVisible(false);
        fixedYutButton.setVisible(false);
        revalidate();
        repaint();
    }

    // 던지기 버튼 모두 보이게 하는 메소드
    public void showTossButtons() {
        randomYutButton.setVisible(true);
        fixedYutButton.setVisible(true);
        for (JLabel label : resultLabels) {
            remove(label);
        }
        revalidate();
        repaint();
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
        // 윷, 모가 아니면 버튼 숨기기
        if (tossResult != TossResult.YUT && tossResult != TossResult.MO) {
            randomYutButton.setVisible(false);
            fixedYutButton .setVisible(false);
        }

        // 결과 라벨 생성 및 추가
        resultLabel = new JLabel(result);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(new Color(255, 240, 200));
        resultLabel.setBorder(BorderFactory.createLineBorder(new Color(0xC3864A)));
        resultLabel.setPreferredSize(new Dimension(75, 50));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가로 중앙 정렬
        resultLabel.setVerticalAlignment(SwingConstants.CENTER);
        resultLabels.add(resultLabel);
        add(resultLabel);

        revalidate();
        repaint();
    }

//    public void promptCapture() {
//        Object[] options = {"예", "아니오"};
//        int choice = JOptionPane.showOptionDialog(null, "말을 잡으시겠습니까?", "말 잡기",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//
//        if (choice == 0) {
//            controller.onConfirmCapture(true);
//            tokenPanel.setDrawOnSide(false);
//        }
//        else if (choice == 1) {
//            tokenPanel.setDrawOnSide(true);
//            tokenPanel.updateTokenPosition(controller.getCurrentTokenId(), controller.getCurrentTokenPos());
//        }
//    }
//
//    public void promptStack() {
//        Object[] options = {"예", "아니오"};
//        int choice = JOptionPane.showOptionDialog(null, "말을 업으시겠습니까?", "말 업기",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//
//        if (choice == 0) {
//            controller.onConfirmStack(true);
//            //tokenPanel.setDrawOnSide(false);
//        }
//        else if (choice == 1) { tokenPanel.setDrawOnSide(true); }
//    }

    public void setTokenPanel(TokenPanel tokenPanel) { this.tokenPanel = tokenPanel; }
    public int getLabelsSize() { return resultLabels.size(); }
    public void displayThrowResult(TossResult result) {}
    public void updateTurnLabel(Player player) {}
}