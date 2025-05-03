package ui;

import javax.swing.*;

import controller.GameController;
import model.Player;
import model.TossResult;

import java.awt.*;

public class ControlPanel extends JPanel {
	private GameController controller;
	
    public ControlPanel(GameController controller) {
    	this.controller=controller;
    	
        setPreferredSize(new Dimension(800, 100));
        setLayout(new FlowLayout());

        JButton randomYutButton = new JButton("랜덤 윷 던지기");
        JButton fixedYutButton = new JButton("지정 윷 던지기");

        // 클릭 이벤트는 이후 연결 예정
        randomYutButton.addActionListener(e -> {
            System.out.println("랜덤 윷 던지기 클릭됨");
        });

        fixedYutButton.addActionListener(e -> {
            System.out.println("지정 윷 던지기 클릭됨");
        });

        add(randomYutButton);
        add(fixedYutButton);
    }

	public void displayThrowResult(TossResult result) {
		// TODO Auto-generated method stub

	}

	public void updateTurnLabel(Player player) {
		// TODO Auto-generated method stub

	}
}