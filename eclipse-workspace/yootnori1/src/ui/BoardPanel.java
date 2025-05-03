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

    private List<Point2D.Double> nodeGridPositions;
    private List<Point2D.Double[]> lineConnections;

    public BoardPanel(int playerCount, int tokenCount, String shapeType) {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
        nodeGridPositions = createNodePositions();
        lineConnections = createLineConnections();
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
            int x = (int)(offsetX + p.x * cellSize - size / 2);
            int y = (int)(offsetY + p.y * cellSize - size / 2);

            g2.setColor(Color.BLACK);
            g2.drawOval(x, y, size, size);

            if (important) {
                int inner = size - 8;
                g2.drawOval(x + (size - inner) / 2, y + (size - inner) / 2, inner, inner);
            }
        }

        // 출발 텍스트
        drawLabel(g2, new Point2D.Double(GRID - 1, GRID - 1), "출발", cellSize, offsetX, offsetY);

        for (Point2D.Double[] pair : lineConnections) {
            drawLineEdgeToEdge(g2, pair[0], pair[1], cellSize, offsetX, offsetY);
        }
    }

    private void drawLabel(Graphics2D g2, Point2D.Double p, String label, int cellSize, int offsetX, int offsetY) {
        int x = (int)(offsetX + p.x * cellSize);
        int y = (int)(offsetY + p.y * cellSize);
        g2.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        g2.drawString(label, x - 10, y + 35);
    }

    private void drawLineEdgeToEdge(Graphics2D g2, Point2D.Double from, Point2D.Double to, int cellSize, int offsetX, int offsetY) {
        int x1 = (int)(offsetX + from.x * cellSize);
        int y1 = (int)(offsetY + from.y * cellSize);
        int x2 = (int)(offsetX + to.x * cellSize);
        int y2 = (int)(offsetY + to.y * cellSize);

        double dx = x2 - x1, dy = y2 - y1;
        double dist = Math.hypot(dx, dy);

        double r1 = (isImportant(from) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;
        double r2 = (isImportant(to) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;

        double ratio1 = r1 / dist, ratio2 = r2 / dist;

        int newX1 = (int)(x1 + dx * ratio1);
        int newY1 = (int)(y1 + dy * ratio1);
        int newX2 = (int)(x2 - dx * ratio2);
        int newY2 = (int)(y2 - dy * ratio2);

        g2.drawLine(newX1, newY1, newX2, newY2);
    }

    private List<Point2D.Double> createNodePositions() {
        List<Point2D.Double> nodes = new ArrayList<>();

        // 외곽
        for (int i = 0; i <= GRID - 1; i++) nodes.add(new Point2D.Double(i, 0));
        for (int i = 1; i <= GRID - 1; i++) nodes.add(new Point2D.Double(GRID - 1, i));
        for (int i = GRID - 2; i >= 0; i--) nodes.add(new Point2D.Double(i, GRID - 1));
        for (int i = GRID - 2; i >= 1; i--) nodes.add(new Point2D.Double(0, i));

        // 대각선
        for (int i = 1; i <= 5; i++) {
            double r = i / 6.0;
            nodes.add(new Point2D.Double(r * (GRID - 1), r * (GRID - 1)));
            nodes.add(new Point2D.Double(r * (GRID - 1), (1 - r) * (GRID - 1)));
        }

        return nodes;
    }

    private List<Point2D.Double[]> createLineConnections() {
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

    private boolean isImportant(Point2D.Double p) {
        return isClose(p, 0, 0) || isClose(p, 0, GRID - 1) || isClose(p, GRID - 1, 0) || isClose(p, GRID - 1, GRID - 1)
            || isClose(p, CENTER, CENTER);
    }

    private boolean isClose(Point2D.Double p, double x, double y) {
        return Math.abs(p.x - x) < TOLERANCE && Math.abs(p.y - y) < TOLERANCE;
    }

	public void moveToken(Token token) {
		// TODO Auto-generated method stub
		
	}
}