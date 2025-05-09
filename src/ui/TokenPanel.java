package ui;

import java.awt.geom.Point2D.Double;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import controller.GameController;
import model.Position;
import model.Token;

public class TokenPanel extends JComponent {
    private final BoardPanel board;
    private final ControlPanel control;
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

    private Integer pendingCarrier = null;
    private Integer baseToken = null;

    public TokenPanel(BoardPanel board, ControlPanel control, StartPanel startPanel, GameController controller) {
        this.board = board;
        this.control = control;
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

                // 토큰 아닌 곳 클릭 → 예약 취소하고 보드에 이벤트 넘김
                if (tokenId == -1) {
                    pendingCarrier = null;
                    board.dispatchEvent(
                        SwingUtilities.convertMouseEvent(TokenPanel.this, e, board)
                    );
                    return;
                }

                // 첫 번째 클릭 → 캐리어 토큰 예약
                if (pendingCarrier == null) {
                    pendingCarrier = tokenId;
                    System.out.println("[TokenPanel] 캐리어 예약: 토큰 " + pendingCarrier);
                    return;
                }

                // 두 번째 클릭 → 베이스 토큰으로 stacking
                int baseId = tokenId;
                if (baseId == pendingCarrier) {
                    System.out.println("[TokenPanel] 동일 토큰 선택, 스태킹 취소");
                    pendingCarrier = null;
                    return;
                }

                // 보드 밖 토큰은 베이스 토큰으로 사용할 수 없음
                int baseNodeIndex = board.getNodeIndexAt(
                    (Point2D.Double) tokenPositions.get(tokens.indexOf(baseId))
                );
                if (baseNodeIndex == 0) {
                    System.out.println("[TokenPanel] 노드 밖 토큰은 베이스로 선택할 수 없습니다: 토큰 " + baseId);
                    return;
                }

                Token carrier = controller.findTokenById(pendingCarrier);
                Position basePos = controller.findTokenById(baseId).getPosition();
                controller.onStackTokens(carrier, basePos);
                System.out.println("[TokenPanel] 토큰 " + pendingCarrier +
                    "을(를) 토큰 " + baseId + " 위에 업음");

                pendingCarrier = null;
                board.setClickable(false);
                control.showTossButtons();
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
                    int tx = (int) pi.getX() + diameter / 2 + 10;
                    int ty = (int) pi.getY() - diameter / 2 + fm.getAscent();
                    g2.drawString(label, tx, ty);
                }
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

            // token 이동 끝난 후 던지기 버튼 다시 보여줌 -> YutResults가 empty이면 다시 보여줌
//            if (control != null && control.getLabelsSize() == 1) {
//                control.showTossButtons();
//            }
        });
    }

    // Overloading: 잡혔을 때 시작 위치로 토큰을 보낼 때만 사용
    public void updateTokenPosition(int tokenId, Point2D.Double position) {
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);
        tokenPositions.set(idx, position);

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