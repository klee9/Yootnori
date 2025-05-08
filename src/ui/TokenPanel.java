package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import controller.GameController;

public class TokenPanel extends JComponent {
    private final BoardPanel board;
    private GameController controller;
    private final List<Integer> nodeIndices = new ArrayList<>();
    private final List<Integer> tokens = new ArrayList<>();
    private final List<Point2D> tokenPositions = new ArrayList<>();
    private final int diameter = 24;
    private final Color[] colors = {
            new Color(100, 149, 237), // 파랑
            new Color(240, 128, 128), // 빨강
            new Color(255, 215, 0),   // 노랑
            new Color(144, 238, 144)  // 연두
    };
    private int playerCount;
    private int tokenCount;
    private int clickedToken = -1;

    public TokenPanel(BoardPanel board, StartPanel startPanel, GameController controller) {
        this.board = board;
        this.controller = controller;
        this.playerCount = startPanel.getSelectedPlayerCount();
        this.tokenCount = startPanel.getSelectedTokenCount();
        setOpaque(false);

        // 말 개수만큼 말 및 초기 위치 정보 추가
        double startX = 800 - 150;
        double startY = 600 - 350;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int tokenId = 10*i + j;
                tokens.add(tokenId);
                tokenPositions.add(new Point2D.Double(startX + diameter*j + j*8, startY + diameter*i + i*15));
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tokenId = getClickedToken(e);
                if (tokenId != -1) { // 현재 플레이어가 아니면 실행되지 않도록 해야 함
                    controller.onClickToken(tokenId);
                }
                else {
                    board.dispatchEvent(SwingUtilities.convertMouseEvent(TokenPanel.this, e, board));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int x = (int)tokenPositions.get(i*tokenCount + j).getX();
                int y = (int)tokenPositions.get(i*tokenCount + j).getY();
                g2.setColor(colors[i]);
                g2.fillOval(x, y, 24, 24);
            }
        }

    }

    public void updateTokenPosition(int tokenId, int positionId) {
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);

        if (positionId == -1) { // move to somehwere
            SwingUtilities.invokeLater(() -> {
                tokenPositions.set(idx, new Point2D.Double(50,400));
                revalidate();
                repaint();
            });
            return;
        }
        int cellSize = board.getCellSize();
        int offsetX = board.getOffsetX();
        int offsetY = board.getOffsetY();

        Point2D.Double grid = board.getNodePosition(positionId);

        int x = board.toPixelX(grid.x, cellSize, offsetX);
        int y = board.toPixelY(grid.y, cellSize, offsetY);

        System.out.println("[Controller] 인덱스: " + idx + " 아이디: " + tokenId);
        tokenPositions.set(idx, new Point2D.Double(x-diameter/2.0, y-diameter/2.0));

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    public int getClickedToken(MouseEvent e) {
        Point2D.Double clickPoint = new Point2D.Double(e.getX(), e.getY());
        for (int i = 0; i < tokens.size(); i++) {
            Point2D token = tokenPositions.get(i);
            if (clickPoint.distance(token.getX(), token.getY()) < diameter) {
                int tokenId = tokens.get(i);
                int currentPlayerId = controller.getCurrentPlayerId();
                int tokenOwner = tokenId / 10;
                if (currentPlayerId != tokenOwner) {
                    System.out.println("[TokenPanel] 현재 플레이어의 토큰이 아닙니다. 이동 불가.");
                    return -1;
                }
                System.out.println("[TokenPanel] 토큰" + tokenId + " 선택됨");
                board.setClickable(true);
                clickedToken = tokenId;
                return tokenId;
            }
        }
        return -1;
    }

    public int getCurrentToken() {
        return clickedToken;
    }
}