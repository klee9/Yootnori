package ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import controller.GameController;
import ui.interfaces.TokenPanel;

public class TokenPanelSwing extends JComponent implements KeyListener, TokenPanel {
    private GameController controller;
    private final BoardPanelSwing board;
    private final ControlPanelSwing control;

    private final List<Integer> tokens = new ArrayList<>();
    private final List<Point2D> tokenPositions = new ArrayList<>();

    private boolean clickable = true;

    private final int diameter = 24;
    private final Color[] colors = {
            new Color(100, 149, 237), // 파랑
            new Color(240, 128, 128), // 빨강
            new Color(255, 215, 0),   // 노랑
            new Color(144, 238, 144)  // 연두
    };

    private int playerCount;
    private int tokenCount;
    private int clickedToken = 0;

    public TokenPanelSwing(BoardPanelSwing board, ControlPanelSwing control, StartPanelSwing startPanel, GameController controller) {
        this.board = board;
        this.control = control;
        this.controller = controller;
        this.playerCount = startPanel.getSelectedPlayerCount();
        this.tokenCount = startPanel.getSelectedTokenCount();
        setOpaque(false);

        // 말 개수만큼 말 및 초기 위치 정보 추가
        double startX = 650;
        double startY = 250;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int tokenId = 10 * i + j;
                tokens.add(tokenId);
                tokenPositions.add(new Point2D.Double(startX + diameter * j + j * 8, startY + diameter * i + i * 15));
            }
        }

        // 토큰을 클릭하면 토큰 패널은 눌리지 않아야 함. 다시 누르고 싶으면 키를 눌러서 하기
        // 이벤트가 보드에 전송되기 때문에 보드의 같은 위치에 토큰이 여러개 있으면 스택해야 함.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tokenId = getClickedToken(e);
                setClickable(false);

                // 토큰 아닌 곳 클릭 → 예약 취소하고 보드에 이벤트 넘김
                if (tokenId != -1) {
                    controller.onClickToken(tokenId);
                }
                else {
                    board.dispatchEvent(
                            SwingUtilities.convertMouseEvent(TokenPanelSwing.this, e, board)
                    );
                }
                //control.showTossButtons();
            }
        });


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int x = (int) tokenPositions.get(i * tokenCount + j).getX();
                int y = (int) tokenPositions.get(i * tokenCount + j).getY();
                g2.setColor(colors[i]);
                g2.fillOval(x, y, 24, 24);
            }
        }
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int idx = i * tokenCount + j;
                Point2D pi = tokenPositions.get(idx);

                int overlap = 0;
                for (int jj = 0; jj < tokenCount; jj++) {
                    if (jj == j) continue;
                    int otherIdx = i * tokenCount + jj;
                    Point2D pj = tokenPositions.get(otherIdx);
                    if (pi.distance(pj) < 1e-3) {
                        overlap++;
                    }
                }

                if (overlap > 0) {
                    String label = "+" + overlap;
                    int tx = (int) pi.getX() + diameter / 2 + 2;
                    int ty = (int) pi.getY() - diameter / 2 + fm.getAscent();
                    g2.drawString(label, tx, ty);
                }
            }
        }
    }

    @Override
    public void updateTokenPosition(int tokenId, int positionId) {
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);

        if (positionId == -1) { // move off-screen
            SwingUtilities.invokeLater(() -> {
                tokenPositions.set(idx, new Point2D.Double(50, 600));
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

        tokenPositions.set(idx, new Point2D.Double(x - diameter / 2.0, y - diameter / 2.0));

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    // Overloading: 잡혔을 때 시작 위치로 토큰을 보낼 때만 사용
    @Override
    public void updateTokenPosition(int tokenId, double x, double y) {
        Point2D position = new Point2D.Double(x, y);
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);
        tokenPositions.set(idx, position);
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    @Override
    public int getClickedToken(Object evt) {
        if (evt instanceof java.awt.event.MouseEvent e) {
            if (clickable) {
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
            }
        }
        return -1;
    }

    @Override
    public int getCurrentToken() {
        return clickedToken;
    }

    @Override
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            clickable = true;
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
