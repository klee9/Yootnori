package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import javax.swing.*;

import controller.GameController;
import model.Token;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import model.TossResult;

public class BoardPanel extends JPanel {

    private final int NODE_SIZE = 28;
    private final int IMPORTANT_EXTRA = 6;
    private final int GRID = 6;
    private final int PADDING = 40;
    private final String shapeType;
    private JLabel yutImageLabel;
    private Timer  yutTimer;

    private final List<Point2D.Double> nodeGridPositions;
    private final List<Point2D.Double[]> lineConnections;
    private final List<Integer> importantIndices = new ArrayList<>();
    private final List<Point2D.Double> actualNodePositions = new ArrayList<>();

    private int[] idToRaw = null;
    private int[] rawToId = null;
    private boolean clickable = false;
    private TokenPanel tokenPanel;

    public BoardPanel(int playerCount, int tokenCount, String shapeType, GameController controller) {
        this.shapeType = shapeType;
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocusInWindow();

        List<Point2D.Double> raw;
        List<Point2D.Double[]> rawLines;

        switch (shapeType) {
            case "사각형" -> {
                raw = createSquareNodePositions();
                rawLines = createSquareLineConnections();
            }
            case "오각형" -> {
                raw = createPentagonNodePositions();
                rawLines = createPentagonLineConnections(raw);
            }
            case "육각형" -> {
                raw = createHexagonNodePositions();
                rawLines = createHexagonLineConnections(raw);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 shapeType: " + shapeType);
        }

        // 보드 모양에 따라서 매핑 추가
        switch (shapeType) {
            case "사각형" -> {
                idToRaw = new int[]{10,9,8,7,6,5,4,3,2,1,0,19,18,17,16,15,14,13,12,11,26,24,27,25,20,22,21,23,28};
            }
            case "오각형" -> {
                idToRaw = new int[]{5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,0,1,2,3,4,29,28,31,30,33,32,35,34,27,26,25};
            }
            case "육각형" -> {
                idToRaw = new int[]{15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,38,37,40,39,42,41,32,31,34,33,36,35,30};
            }
            default -> throw new IllegalArgumentException("지원하지 않는 shapeType: " + shapeType);
        }

        int n = raw.size();
        rawToId = new int[n];
        for (int i = 0; i < n; i++)
            rawToId[idToRaw[i]] = i;

        nodeGridPositions = new ArrayList<>(n);
        for (int id = 0; id < n; id++) {
            nodeGridPositions.add(raw.get(idToRaw[id]));
        }

        lineConnections = new ArrayList<>(rawLines.size());
        for (Point2D.Double[] edge : rawLines) {
            int fromRaw = raw.indexOf(edge[0]);
            int toRaw = raw.indexOf(edge[1]);
            int fromId = rawToId[fromRaw];
            int toId = rawToId[toRaw];
            lineConnections.add(new Point2D.Double[]{
                    nodeGridPositions.get(fromId),
                    nodeGridPositions.get(toId)
            });
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickable) {
                    int posId = handleNodeClick(e);
                    int currentToken = tokenPanel.getCurrentToken();
                    List<Token> stackedTokens = controller.findTokenById(currentToken).getStackedTokens();

                    if (currentToken == -1) {
                        System.out.println("[BoardPanel] 잘못된 토큰 선택. 현재 플레이어의 토큰이 아닙니다.");
                        tokenPanel.setClickable(true);
                        return;
                    }

                    boolean moveResult = controller.onMoveTokens(controller.onClickPosition(posId));
                    if (!moveResult) {
                        System.out.println("[BoardPanel] 해당 위치로 움직일 수 없습니다. 다시 시도하세요.");
                    } else {
                        System.out.println("[BoardPanel] 말을 " + posId + "번째 칸으로 이동했습니다.");
                        //tokenPanel.updateTokenPosition(currentToken, posId);
//                        controller.onStackTokens(controller.findTokenById(currentToken), controller.onClickPosition(posId));
//                        for (Token t : stackedTokens) {
//                            tokenPanel.updateTokenPosition(t.getId(), posId);
//                        }
                        tokenPanel.setClickable(true);
                        tokenPanel.repaint();
                        setClickable(false);
                    }
                }
            }
        });
    }

    public void showYutImage(TossResult result) {
        String fileName = switch (result) {
            case DO -> "do.png";
            case GAE -> "gae.png";
            case GEOL -> "geol.png";
            case YUT -> "yut.png";
            case MO -> "mo.png";
            case BACKDO -> "backdo.png";
        };

        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/images/" + fileName)));
        Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon resized = new ImageIcon(scaled);

        if (yutImageLabel != null) {
            remove(yutImageLabel);
        }
        yutImageLabel = new JLabel(resized);

        // 중앙 위치 계산
        int pw = getWidth(), ph = getHeight();
        int iw = resized.getIconWidth(), ih = resized.getIconHeight();
        yutImageLabel.setBounds((pw - iw)/2, (ph - ih)/2, iw, ih);

        setLayout(null);
        add(yutImageLabel);
        revalidate();
        repaint();

        if (yutTimer != null && yutTimer.isRunning()) {
            yutTimer.stop();
        }
        yutTimer = new Timer(3000, e -> removeYutImage());
        yutTimer.setRepeats(false);
        yutTimer.start();
    }

    public void removeYutImage() {
        if (yutTimer != null && yutTimer.isRunning()) {
            yutTimer.stop();
        }
        if (yutImageLabel != null) {
            remove(yutImageLabel);
            yutImageLabel = null;
            revalidate();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cellSize = Math.min(getHeight() - 2 * PADDING, getWidth() - 2 * PADDING) / (GRID - 1);
        int offsetX = (getWidth() - cellSize * (GRID - 1)) / 2;
        int offsetY = (getHeight() - cellSize * (GRID - 1)) / 2;

        for (Point2D.Double p : nodeGridPositions) {
            boolean important = isImportant(p);
            int size = NODE_SIZE + (important ? IMPORTANT_EXTRA : 0);
            int x = toPixelX(p.x, cellSize, offsetX) - size / 2;
            int y = toPixelY(p.y, cellSize, offsetY) - size / 2;
            actualNodePositions.add(new Point2D.Double(x, y));
            g2.setColor(Color.BLACK);
            g2.drawOval(x, y, size, size);
            if (important) {
                int inner = size - 8;
                g2.drawOval(x + (size - inner) / 2, y + (size - inner) / 2, inner, inner);
            }
        }

        for (Point2D.Double[] pair : lineConnections) {
            drawLineEdgeToEdge(g2, pair[0], pair[1], cellSize, offsetX, offsetY);
        }

        Point2D.Double startPos = getStartPosition();
        int x = toPixelX(startPos.x, cellSize, offsetX);
        int y = toPixelY(startPos.y, cellSize, offsetY);

        g2.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth("출발");
        int textHeight = metrics.getAscent();
        g2.drawString("출발", x - textWidth / 2, y + textHeight / 2);
    }

    private void drawLineEdgeToEdge(Graphics2D g2, Point2D.Double from, Point2D.Double to, int cellSize, int offsetX, int offsetY) {
        int x1 = toPixelX(from.x, cellSize, offsetX);
        int y1 = toPixelY(from.y, cellSize, offsetY);
        int x2 = toPixelX(to.x, cellSize, offsetX);
        int y2 = toPixelY(to.y, cellSize, offsetY);

        double dx = x2 - x1, dy = y2 - y1;
        double dist = Math.hypot(dx, dy);

        double r1 = (isImportant(from) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;
        double r2 = (isImportant(to) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;

        int newX1 = (int)(x1 + dx * r1 / dist);
        int newY1 = (int)(y1 + dy * r1 / dist);
        int newX2 = (int)(x2 - dx * r2 / dist);
        int newY2 = (int)(y2 - dy * r2 / dist);

        g2.drawLine(newX1, newY1, newX2, newY2);
    }

    private boolean isImportant(Point2D.Double p) {
        return importantIndices.stream().anyMatch(idx -> isClose(p, nodeGridPositions.get(idx)));
    }

    private boolean isClose(Point2D.Double p1, Point2D.Double p2) {
        double TOLERANCE = 0.01;
        return Math.abs(p1.x - p2.x) < TOLERANCE && Math.abs(p1.y - p2.y) < TOLERANCE;
    }

    public int getNodeIndexAt(Point2D.Double clickPoint) {
        for (int i = 0; i < actualNodePositions.size(); i++) {
            Point2D.Double node = actualNodePositions.get(i);
            if (clickPoint.distance(node) < NODE_SIZE) {
                return i;
            }
        }
        // 보드 밖이면 출발 지점(=노드 0)으로 간주
        return 0;
    }

    int toPixelX(double x, int cellSize, int offsetX) {
        return (int)(offsetX + x * cellSize);
    }

    int toPixelY(double y, int cellSize, int offsetY) {
        return (int)(offsetY + y * cellSize);
    }

    private Point2D.Double getStartPosition() {
        return switch (shapeType) {
            case "사각형" -> new Point2D.Double(GRID - 1, GRID - 1);
            case "오각형" -> nodeGridPositions.get(0);
            case "육각형" -> nodeGridPositions.get(0);
            default -> new Point2D.Double(0, 0);
        };
    }

    private List<Point2D.Double> createSquareNodePositions() {
        List<Point2D.Double> nodes = new ArrayList<>();
        importantIndices.clear();  // 기존 중요 노드 목록 초기화

        // 외곽
        for (int i = 0; i <= GRID - 1; i++) nodes.add(new Point2D.Double(i, 0));                // 상단
        for (int i = 1; i <= GRID - 1; i++) nodes.add(new Point2D.Double(GRID - 1, i));         // 우측
        for (int i = GRID - 2; i >= 0; i--) nodes.add(new Point2D.Double(i, GRID - 1));         // 하단
        for (int i = GRID - 2; i >= 1; i--) nodes.add(new Point2D.Double(0, i));                // 좌측

        // 꼭짓점 4개
        importantIndices.add(0);                                      // (0, 0)
        importantIndices.add(GRID - 1);                               // (5, 0)
        importantIndices.add(2 * GRID - 2);                           // (5, 5)
        importantIndices.add(3 * GRID - 3);                           // (0, 5)

        // 대각선
        for (int i = 1; i <= 5; i++) {
            double r = i / 6.0;
            if (i == 3) continue;
            nodes.add(new Point2D.Double(r * (GRID - 1), r * (GRID - 1)));         // ↘
            nodes.add(new Point2D.Double(r * (GRID - 1), (1 - r) * (GRID - 1)));   // ↙
        }

        // 중심 노드 (정확히 GRID 중심)
        double CENTER = (GRID - 1) / 2.0;
        nodes.add(new Point2D.Double(CENTER, CENTER));
        importantIndices.add(nodes.size() - 1);

        return nodes;
    }

    private List<Point2D.Double[]> createSquareLineConnections() {
        List<Point2D.Double[]> lines = new ArrayList<>();

        // 외곽
        for (int i = 0; i < GRID - 1; i++) lines.add(new Point2D.Double[]{new Point2D.Double(i, 0), new Point2D.Double(i + 1, 0)});
        for (int i = 0; i < GRID - 1; i++) lines.add(new Point2D.Double[]{new Point2D.Double(GRID - 1, i), new Point2D.Double(GRID - 1, i + 1)});
        for (int i = GRID - 1; i > 0; i--) lines.add(new Point2D.Double[]{new Point2D.Double(i, GRID - 1), new Point2D.Double(i - 1, GRID - 1)});
        for (int i = GRID - 1; i >= 1; i--) lines.add(new Point2D.Double[]{new Point2D.Double(0, i), new Point2D.Double(0, i - 1)});

        // 대각선 ↘
        for (int i = 0; i < 6; i++) {
            double x1 = i / 6.0 * (GRID - 1);
            double y1 = i / 6.0 * (GRID - 1);
            double x2 = (i + 1) / 6.0 * (GRID - 1);
            double y2 = (i + 1) / 6.0 * (GRID - 1);
            lines.add(new Point2D.Double[]{new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)});
        }

        // 대각선 ↙
        for (int i = 0; i < 6; i++) {
            double x1 = i / 6.0 * (GRID - 1);
            double y1 = (1 - i / 6.0) * (GRID - 1);
            double x2 = (i + 1) / 6.0 * (GRID - 1);
            double y2 = (1 - (i + 1) / 6.0) * (GRID - 1);
            lines.add(new Point2D.Double[]{new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)});
        }

        return lines;
    }

    private List<Point2D.Double> createPentagonNodePositions() {
        List<Point2D.Double> nodes = new ArrayList<>();
        importantIndices.clear();

        int outerCount = 5;
        int nodesPerEdge = 6;
        double radius = 3.0;
        double centerX = 2.5, centerY = 2.8;

        List<Point2D.Double> corners = new ArrayList<>();
        for (int i = 0; i < outerCount; i++) {
            double angle = Math.toRadians(90 + i * 72);
            double x = centerX + radius * Math.cos(angle);
            double y = centerY - radius * Math.sin(angle);
            corners.add(new Point2D.Double(x, y));
        }

        for (int i = 0; i < outerCount; i++) {
            Point2D.Double start = corners.get(i);
            Point2D.Double end = corners.get((i + 1) % outerCount);
            for (int j = 0; j < nodesPerEdge; j++) {
                double t = j / (double)(nodesPerEdge - 1);
                double x = start.x * (1 - t) + end.x * t;
                double y = start.y * (1 - t) + end.y * t;
                Point2D.Double node = new Point2D.Double(x, y);
                if (!nodes.contains(node)) nodes.add(node);
                if (j == 0) importantIndices.add(nodes.size() - 1);
            }
        }

        Point2D.Double center = new Point2D.Double(centerX, centerY);
        nodes.add(center);
        importantIndices.add(35);

        for (Point2D.Double corner : corners) {
            for (int j = 1; j <= 2; j++) {
                double t = j / 3.0;
                double x = centerX * (1 - t) + corner.x * t;
                double y = centerY * (1 - t) + corner.y * t;
                nodes.add(new Point2D.Double(x, y));
            }
        }
        return nodes;
    }

    private List<Point2D.Double[]> createPentagonLineConnections(List<Point2D.Double> raw) {
        List<Point2D.Double[]> lines = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            lines.add(new Point2D.Double[]{ raw.get(i), raw.get((i + 1) % 25) });
        }
        int center = 25, start = 26;
        for (int i = 0; i < 5; i++) {
            int tip = i * 5, i1 = start + 2 * i, i2 = i1 + 1;
            lines.add(new Point2D.Double[]{ raw.get(center), raw.get(i1) });
            lines.add(new Point2D.Double[]{ raw.get(i1), raw.get(i2) });
            lines.add(new Point2D.Double[]{ raw.get(i2), raw.get(tip) });
        }
        return lines;
    }

    private List<Point2D.Double> createHexagonNodePositions() {
        List<Point2D.Double> nodes = new ArrayList<>();
        importantIndices.clear();

        int sides = 6;
        int nodesPerEdge = 6;
        double radius = 3.0;
        double centerX = 2.5, centerY = 2.5;

        List<Point2D.Double> corners = new ArrayList<>();
        for (int i = 0; i < sides; i++) {
            double angle = Math.toRadians(i * 60);
            double x = centerX + radius * Math.cos(angle);
            double y = centerY - radius * Math.sin(angle);
            corners.add(new Point2D.Double(x, y));
        }

        for (int i = 0; i < sides; i++) {
            Point2D.Double start = corners.get(i);
            Point2D.Double end = corners.get((i + 1) % sides);
            for (int j = 0; j < nodesPerEdge; j++) {
                double t = j / (double)(nodesPerEdge - 1);
                double x = start.x * (1 - t) + end.x * t;
                double y = start.y * (1 - t) + end.y * t;
                Point2D.Double node = new Point2D.Double(x, y);
                if (!nodes.contains(node)) nodes.add(node);
                if (j == 0) importantIndices.add(nodes.size() - 1); // 꼭짓점
            }
        }

        Point2D.Double center = new Point2D.Double(centerX, centerY);
        nodes.add(center);
        importantIndices.add(42);

        for (Point2D.Double corner : corners) {
            for (int j = 1; j <= 2; j++) {
                double t = j / 3.0;
                double x = centerX * (1 - t) + corner.x * t;
                double y = centerY * (1 - t) + corner.y * t;
                nodes.add(new Point2D.Double(x, y));
            }
        }

        return nodes;
    }

    private List<Point2D.Double[]> createHexagonLineConnections(List<Point2D.Double> raw) {
        List<Point2D.Double[]> lines = new ArrayList<>();
        int outer = 30;
        for (int i = 0; i < outer; i++) {
            lines.add(new Point2D.Double[]{ raw.get(i), raw.get((i + 1) % outer) });
        }
        int start = outer + 1;
        for (int i = 0; i < 6; i++) {
            int tip = i * 5, i1 = start + 2 * i, i2 = i1 + 1;
            lines.add(new Point2D.Double[]{ raw.get(outer), raw.get(i1) });
            lines.add(new Point2D.Double[]{ raw.get(i1), raw.get(i2) });
            lines.add(new Point2D.Double[]{ raw.get(i2), raw.get(tip) });
        }
        return lines;
    }

    public int getCellSize() {
        return Math.min(getHeight() - 2 * PADDING,
                getWidth()  - 2 * PADDING) / (GRID - 1);
    }

    public int getOffsetX() {
        int cellSize = getCellSize();
        return (getWidth()  - cellSize * (GRID - 1)) / 2;
    }

    public int getOffsetY() {
        int cellSize = getCellSize();
        return (getHeight() - cellSize * (GRID - 1)) / 2;
    }

    public Point2D.Double getNodePosition(int idx) {
        return nodeGridPositions.get(idx);
    }

    public void moveToken(Token token) {
        // TODO Auto-generated method stub

    }

    public int handleNodeClick(MouseEvent e) {
        Point2D.Double clickPoint = new Point2D.Double(e.getX(), e.getY());
        for (int i = 0; i < nodeGridPositions.size(); i++) {
            Point2D.Double node = actualNodePositions.get(i);
            if (clickPoint.distance(node.x, node.y) < NODE_SIZE) {
                System.out.println(i + "번째 칸 클릭됨");
                return i;
            }
        }
        return idToRaw[0];
    }

    public int autoClickPosition(int positionId) {
        Point2D.Double pos = getNodePosition(positionId);

        int cellSize = getCellSize();
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        int x = toPixelX(pos.x, cellSize, offsetX);
        int y = toPixelY(pos.y, cellSize, offsetY);

        MouseEvent clickEvent = new MouseEvent(
                this,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                x, y,
                1,
                false
        );

        dispatchEvent(clickEvent);
        return positionId;
    }

    public void setClickable(boolean clickable) { this.clickable = clickable; }
    public void setTokenPanel(TokenPanel tokenPanel) { this.tokenPanel = tokenPanel; }
}

