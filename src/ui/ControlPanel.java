package ui;

import javax.swing.*;

import controller.GameController;
import model.Player;
import model.TossResult;

import java.awt.*;

public class ControlPanel extends JPanel {
	private GameController controller;
  private BoardPanel board;

  JButton randomYutButton = new JButton("랜덤 윷 던지기");
  JButton fixedYutButton = new JButton("지정 윷 던지기");
  private JLabel resultLabel;
	
    public ControlPanel(GameController controller, BoardPanel board) {
    	this.controller=controller;
      this.board = board;

      setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        // 클릭 이벤트는 이후 연결 예정
        randomYutButton.addActionListener(e -> {
          System.out.println("랜덤 윷 던지기 클릭됨");
          TossResult result = controller.RandomToss();
          board.showYutImage(result);
          showResultOnly(result);
        });

        fixedYutButton.addActionListener(e -> {
          System.out.println("지정 윷 던지기 클릭됨");
          JPopupMenu menu = new JPopupMenu();
          for (TossResult res : TossResult.values()) {
            JMenuItem item = new JMenuItem(res.name());
            item.addActionListener(ev -> {
              TossResult result = controller.SpecificToss(res);
              board.showYutImage(result);
              showResultOnly(result);
            });
            menu.add(item);
          }
          menu.show(fixedYutButton, 0, fixedYutButton.getHeight());
        });

        add(randomYutButton);
        add(fixedYutButton);
    }

  private void showResultOnly(TossResult result) {
    // 버튼 제거
    remove(randomYutButton);
    remove(fixedYutButton);

    // 결과 라벨 생성 및 추가
    resultLabel = new JLabel(result.toString());
    resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    resultLabel.setOpaque(true);
    resultLabel.setBackground(new Color(255, 240, 200));
    resultLabel.setBorder(BorderFactory.createLineBorder(new Color(0xC3864A)));
    resultLabel.setPreferredSize(new Dimension(50, 50));
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가로 중앙 정렬
    resultLabel.setVerticalAlignment(SwingConstants.CENTER);
    add(resultLabel);

    revalidate();
    repaint();

    // 3초 후 다시 버튼 보이기
    Timer timer = new Timer(3000, e -> {
      remove(resultLabel);
      add(randomYutButton);
      add(fixedYutButton);
      revalidate();
      repaint();
    });
    timer.setRepeats(false);
    timer.start();
  }

	public void displayThrowResult(TossResult result) {
		// TODO Auto-generated method stub

	}

	public void updateTurnLabel(Player player) {
		// TODO Auto-generated method stub

	}
}