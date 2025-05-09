package ui;

import javax.swing.*;

import controller.GameController;
import model.Player;
import model.TossResult;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControlPanel extends JPanel {
    private GameController controller;
    private BoardPanel board;

    JButton randomYutButton = new JButton("랜덤 윷 던지기");
    JButton fixedYutButton = new JButton("지정 윷 던지기");
    private JLabel resultLabel;

    public ControlPanel(GameController controller, BoardPanel board) {
        this.controller = controller;
        this.board = board;

        setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        // 클릭 이벤트는 이후 연결 예정
        randomYutButton.addActionListener(e -> {
            System.out.println("랜덤 윷 던지기 클릭됨");
            TossResult result = controller.onRandomToss();
            board.showYutImage(result);
            showResultOnly(resultTranslate(result));
        });

        fixedYutButton.addActionListener(e -> {
            System.out.println("지정 윷 던지기 클릭됨");
            JPopupMenu menu = new JPopupMenu();
            for (TossResult res : TossResult.values()) {
                JMenuItem item = new JMenuItem(res.name());
                item.addActionListener(ev -> {
                    TossResult result = controller.onSpecifiedToss(res);
                    board.showYutImage(result);
                    showResultOnly(resultTranslate(result));
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
        remove(resultLabel);
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
        // 버튼 숨기기
        randomYutButton.setVisible(false);
        fixedYutButton .setVisible(false);

        // 결과 라벨 생성 및 추가
        resultLabel = new JLabel(result);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(new Color(255, 240, 200));
        resultLabel.setBorder(BorderFactory.createLineBorder(new Color(0xC3864A)));
        resultLabel.setPreferredSize(new Dimension(75, 50));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가로 중앙 정렬
        resultLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(resultLabel);

        revalidate();
        repaint();
    }

    public void displayThrowResult(TossResult result) {
        // TODO Auto-generated method stub

    }

    public void updateTurnLabel(Player player) {
        // TODO Auto-generated method stub

    }
}