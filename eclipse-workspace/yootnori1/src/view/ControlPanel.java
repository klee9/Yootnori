package view;
import javax.swing.*;

import controller.GameController;

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
}