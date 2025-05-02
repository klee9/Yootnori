package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private JComboBox<String> shapeCombo;
    private JComboBox<Integer> playerCombo;
    private JComboBox<Integer> tokenCombo;

    public StartPanel(ActionListener onStart) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // 컴포넌트 간 기본 간격

        // 윷 이미지
        ImageIcon original = new ImageIcon(getClass().getResource("/start_yut.png"));
        Image scaled = original.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(imageLabel, gbc);

        // 콤보박스들
        shapeCombo = new JComboBox<>(new String[]{"사각형", "오각형", "육각형"});
        playerCombo = new JComboBox<>(new Integer[]{2, 3, 4});
        tokenCombo = new JComboBox<>(new Integer[]{2, 3, 4, 5});
        styleComboBox(shapeCombo);
        styleComboBox(playerCombo);
        styleComboBox(tokenCombo);

        // 윷놀이 판
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("윷놀이 판:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(shapeCombo, gbc);

        // 참여자 수
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("참여자 수:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(playerCombo, gbc);

        // 말 개수
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("말 개수:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(tokenCombo, gbc);

        // 시작 버튼
        JButton startButton = new JButton("게임 시작");
        startButton.setBackground(new Color(255, 170, 170));
        startButton.setPreferredSize(new Dimension(140, 40));
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.addActionListener(onStart);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(startButton, gbc);
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(120, 30));
        combo.setBackground(new Color(255, 230, 180));
        combo.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    }

    public int getSelectedPlayerCount() {
        return (int) playerCombo.getSelectedItem();
    }

    public int getSelectedTokenCount() {
        return (int) tokenCombo.getSelectedItem();
    }

    public String getSelectedShapeType() {
        return (String) shapeCombo.getSelectedItem();
    }
}
