package ui.swing;

import javax.swing.*;

import controller.GameController;
import model.TossResult;
import ui.interfaces.ControlPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanelSwing extends JPanel implements ControlPanel {
    JButton randomYutButton = new JButton("랜덤 윷 던지기");
    JButton fixedYutButton = new JButton("지정 윷 던지기");
    private final List<JLabel> resultLabels = new ArrayList<>();
    private TossResult tossResult;

    public ControlPanelSwing(MainWindowSwing mainWindow, BoardPanelSwing board) {
        setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        randomYutButton.addActionListener(e -> {
            System.out.println("랜덤 윷 던지기 클릭됨");
            tossResult = mainWindow.onRandomToss();
            board.showYutImage(tossResult);
            showResultOnly(resultTranslate(tossResult));
            // 가능한 경로를 가져와서 마지막 경로 선택
            int autoMove = mainWindow.onAutoControl();
            if (autoMove != -1) {
                int pid = board.autoClickPosition(autoMove);
                mainWindow.onMoveTokens(pid);
            }
        });

        fixedYutButton.addActionListener(e -> {
            System.out.println("지정 윷 던지기 클릭됨");
            JPopupMenu menu = new JPopupMenu();
            for (TossResult res : TossResult.values()) {
                JMenuItem item = new JMenuItem(res.name());
                item.addActionListener(ev -> {
                    tossResult = mainWindow.onSpecifiedToss(res);
                    board.showYutImage(tossResult);
                    showResultOnly(resultTranslate(tossResult));
                    // 가능한 경로를 가져와서 마지막 경로 선택
                    int autoMove = mainWindow.onAutoControl();
                    if (autoMove != -1) {
                        int pid = board.autoClickPosition(autoMove);
                        mainWindow.onMoveTokens(pid);
                    }
                });
                menu.add(item);
            }
            menu.show(fixedYutButton, 0, fixedYutButton.getHeight());
        });

        add(randomYutButton);
        add(fixedYutButton);
    }

    // 던지기 버튼 모두 보이게 하는 메소드
    @Override
    public void showTossButtons() {
        randomYutButton.setVisible(true);
        fixedYutButton.setVisible(true);
        for (JLabel label : resultLabels) {
            remove(label);
        }
        revalidate();
        repaint();
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
        // 윷, 모가 아니면 버튼 숨기기
        if (tossResult != TossResult.YUT && tossResult != TossResult.MO) {
            randomYutButton.setVisible(false);
            fixedYutButton .setVisible(false);
        }

        // 결과 라벨 생성 및 추가
        JLabel resultLabel = new JLabel(result);
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
}