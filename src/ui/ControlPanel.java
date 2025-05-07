package ui;

import javax.swing.*;

import controller.GameController;
import model.Player;
import model.TossResult;

import java.awt.*;

public class ControlPanel extends JPanel {
  private final BoardPanel board;
  boolean isTokenCaptured = true; // !!!!더미 값!!!! -> 테스트하려고 무조건 업는 상황으로 설정해둠

  JButton randomYutButton = new JButton("랜덤 윷 던지기");
  JButton fixedYutButton = new JButton("지정 윷 던지기");
  private JLabel resultLabel;

  private JPanel questionPanel;

  public ControlPanel(GameController controller, BoardPanel board) {
      this.board = board;

      setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        // 클릭 이벤트는 이후 연결 예정
        randomYutButton.addActionListener(_ -> {
          System.out.println("랜덤 윷 던지기 클릭됨");
          TossResult result = controller.RandomToss();
          board.showYutImage(result);

          hideTossButtons();
          showResultOnly(result);

          if (isTokenCaptured) {
            selectCaptureToken();
          } else {
            startRestoreTimer();
          }
        });

        fixedYutButton.addActionListener(_ -> {
          System.out.println("지정 윷 던지기 클릭됨");
          JPopupMenu menu = new JPopupMenu();
          for (TossResult res : TossResult.values()) {
            JMenuItem item = new JMenuItem(res.name());
            item.addActionListener(_ -> {
              TossResult result = controller.SpecificToss(res);
              board.showYutImage(result);

              hideTossButtons();
              showResultOnly(result);

              if (isTokenCaptured) {
                selectCaptureToken();
              } else {
                startRestoreTimer();
              }
            });
            menu.add(item);
          }
          menu.show(fixedYutButton, 0, fixedYutButton.getHeight());
        });

        add(randomYutButton);
        add(fixedYutButton);
    }

  private void hideTossButtons() {
    remove(randomYutButton);
    remove(fixedYutButton);
  }

  private void showTossButtons() {
    add(randomYutButton);
    add(fixedYutButton);
    revalidate();
    repaint();
  }

  private void showResultOnly(TossResult result) {
    resultLabel = new JLabel(result.toString());
    resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    resultLabel.setOpaque(true);
    resultLabel.setBackground(new Color(255, 240, 200));
    resultLabel.setBorder(BorderFactory.createLineBorder(new Color(0xC3864A)));
    resultLabel.setPreferredSize(new Dimension(50, 50));
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    resultLabel.setVerticalAlignment(SwingConstants.CENTER);
    add(resultLabel);

    revalidate();
    repaint();
  }

  private void resetToTossButtons() {
    if (questionPanel != null) questionPanel.setVisible(false);
    remove(questionPanel);
    remove(resultLabel);
    showTossButtons();
  }

  // controller 연결 안 돼서 임시로 boolean 타입 사용
  private void selectCaptureToken() {
    questionPanel = new JPanel(new FlowLayout());
    questionPanel.add(new JLabel("말을 업으시겠습니까?"));
    JButton yesButton;
    questionPanel.add(yesButton = new JButton("예"));
    JButton noButton;
    questionPanel.add(noButton = new JButton("아니요"));
    add(questionPanel);
    revalidate();
    repaint();

    yesButton.addActionListener(_ -> {resetToTossButtons(); board.removeYutImage();});
    noButton.addActionListener(_ -> {resetToTossButtons(); board.removeYutImage();});
  }

  private void startRestoreTimer() {
    Timer t = new Timer(3000, _ -> {
      remove(resultLabel);
      showTossButtons();
    });
    t.setRepeats(false);
    t.start();
  }

  public void displayThrowResult(TossResult result) {
		// TODO Auto-generated method stub

	}

	public void updateTurnLabel(Player player) {
		// TODO Auto-generated method stub

	}
}