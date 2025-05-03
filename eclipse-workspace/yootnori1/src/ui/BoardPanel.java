package ui;

import javax.swing.*;

import model.Token;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    private final int NODE_SIZE = 28;
    private final int IMPORTANT_EXTRA = 6;
    private final int GRID = 6;
    private final int PADDING = 40;
    private final double CENTER = (GRID - 1) / 2.0;
    private final double TOLERANCE = 0.01;
    private String shapeType;

    private List<Point2D.Double> nodeGridPositions;
    private List<Point2D.Double[]> lineConnections;
    private List<Integer> importantIndices = new ArrayList<>();

    public BoardPanel(int playerCount, int tokenCount, String shapeType) {
        this.shapeType = shapeType;
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);

        switch (shapeType) {
            case "사각형" -> {
                nodeGridPositions = createSquareNodePositions();
                lineConnections = createSquareLineConnections();
            }
            case "오각형" -> {
                nodeGridPositions = createPentagonNodePositions();
                lineConnections = createPentagonLineConnections();
            }
            case "육각형" -> {
                nodeGridPositions = createHexagonNodePositions();
                lineConnections = createHexagonLineConnections();
            }
            default -> throw new IllegalArgumentException("지원하지 않는 shapeType: " + shapeType);
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

        g2.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", Font.BOLD, 12));
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth("\uCD9C\uBC1C");
        int textHeight = metrics.getAscent();
        g2.drawString("\uCD9C\uBC1C", x - textWidth / 2, y + textHeight / 2);
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
        return Math.abs(p1.x - p2.x) < TOLERANCE && Math.abs(p1.y - p2.y) < TOLERANCE;
    }

    private int toPixelX(double x, int cellSize, int offsetX) {
        return (int)(offsetX + x * cellSize);
    }

    private int toPixelY(double y, int cellSize, int offsetY) {
        return (int)(offsetY + y * cellSize);
    }

    private Point2D.Double getStartPosition() {
        return switch (shapeType) {
            case "사각형" -> new Point2D.Double(GRID - 1, GRID - 1);
            case "오각형" -> nodeGridPositions.get(4);
            case "육각형" -> nodeGridPositions.get(11);
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
            nodes.add(new Point2D.Double(r * (GRID - 1), r * (GRID - 1)));         // ↘
            nodes.add(new Point2D.Double(r * (GRID - 1), (1 - r) * (GRID - 1)));   // ↙
        }

        // 중심 노드 (정확히 GRID 중심)
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
        int nodesPerEdge = 4;
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
                nodes.add(node);
                if (j == 0) importantIndices.add(nodes.size() - 1);
            }
        }

        Point2D.Double center = new Point2D.Double(centerX, centerY);
        nodes.add(center);
        importantIndices.add(nodes.size() - 1);

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

    private List<Point2D.Double[]> createPentagonLineConnections() {
        List<Point2D.Double[]> lines = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            lines.add(new Point2D.Double[]{
                nodeGridPositions.get(i),
                nodeGridPositions.get((i + 1) % 20)
            });
        }

        int centerIdx = 20;
        int innerStartIdx = 21;

        for (int i = 0; i < 5; i++) {
            int tip = i * 4;
            int inner1 = innerStartIdx + i * 2;
            int inner2 = innerStartIdx + i * 2 + 1;

            lines.add(new Point2D.Double[]{nodeGridPositions.get(centerIdx), nodeGridPositions.get(inner1)});
            lines.add(new Point2D.Double[]{nodeGridPositions.get(inner1), nodeGridPositions.get(inner2)});
            lines.add(new Point2D.Double[]{nodeGridPositions.get(inner2), nodeGridPositions.get(tip)});
        }

        return lines;
    }

    private List<Point2D.Double> createHexagonNodePositions() {
        List<Point2D.Double> nodes = new ArrayList<>();
        importantIndices.clear();

        int sides = 6;
        int nodesPerEdge = 4;
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
                nodes.add(node);
                if (j == 0) importantIndices.add(nodes.size() - 1); // 꼭짓점
            }
        }

        Point2D.Double center = new Point2D.Double(centerX, centerY);
        nodes.add(center);
        importantIndices.add(nodes.size() - 1);

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

    private List<Point2D.Double[]> createHexagonLineConnections() {
        List<Point2D.Double[]> lines = new ArrayList<>();

        int outer = 24; // 6 * 4
        for (int i = 0; i < outer; i++) {
            lines.add(new Point2D.Double[]{
                nodeGridPositions.get(i),
                nodeGridPositions.get((i + 1) % outer)
            });
        }

        int centerIdx = outer;
        int innerStartIdx = outer + 1;

        for (int i = 0; i < 6; i++) {
            int tip = i * 4;
            int inner1 = innerStartIdx + i * 2;
            int inner2 = innerStartIdx + i * 2 + 1;

            lines.add(new Point2D.Double[]{nodeGridPositions.get(centerIdx), nodeGridPositions.get(inner1)});
            lines.add(new Point2D.Double[]{nodeGridPositions.get(inner1), nodeGridPositions.get(inner2)});
            lines.add(new Point2D.Double[]{nodeGridPositions.get(inner2), nodeGridPositions.get(tip)});
        }

        return lines;
    }

    public void moveToken(Token token) {
		// TODO Auto-generated method stub

	}
}