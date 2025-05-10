package ui;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {
    private JLabel[] playerLabels;
    private final Color[] colors = {
            new Color(100, 149, 237), // 파랑
            new Color(240, 128, 128), // 빨강
            new Color(255, 215, 0),   // 노랑
            new Color(144, 238, 144)  // 연두
    };

    public PlayerInfoPanel(int playerCount, int tokenCount) {
        setLayout(new GridLayout(1, playerCount));
        setBackground(Color.WHITE);

        playerLabels = new JLabel[playerCount];
        for (int i = 0; i < playerCount; i++) {
            JLabel label = new JLabel("Player " + (i + 1) + "<br>남은 말: " + tokenCount + "개");
            label.setOpaque(true);
            Color[] colors = {
                    new Color(100, 149, 237), // 파랑
                    new Color(240, 128, 128), // 빨강
                    new Color(255, 215, 0),   // 노랑
                    new Color(144, 238, 144)  // 연두
            };
            label.setBackground(colors[i]);
            label.setForeground(Color.BLACK);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            label.setText("<html>Player " + (i + 1) + "<br>남은 말: " + tokenCount + "개</html>");

            playerLabels[i] = label;
            add(label);
        }
    }

    // 말 개수 업데이트용 (추후 사용)
    public void updateTokenCount(int playerIndex, int remaining) {
        playerLabels[playerIndex].setText(
                "<html>Player " + (playerIndex + 1) + "<br>남은 말: " + remaining + "개</html>"
        );
    }

    public void updateCurrentPlayer(int playerIndex, int remaining) {
        playerLabels[(playerIndex-1+playerLabels.length) % playerLabels.length].setText(
                "<html>Player " + ((playerIndex-1+playerLabels.length) % playerLabels.length + 1) + "<br>남은 말: " + remaining + "개</html>");
        playerLabels[playerIndex].setText("<html><b>Player " + (playerIndex + 1) + "◀<br>남은 말: " + remaining + "개</b></html>");
    }
}