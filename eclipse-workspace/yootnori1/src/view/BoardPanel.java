package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    private int playerCount;
    private int tokenCount;
    private String shapeType;

    private final int NODE_SIZE = 28;
    private final int GRID = 6;
    private final int PADDING = 40;

    private List<Point> nodeGridPositions;

    public BoardPanel(int playerCount, int tokenCount, String shapeType) {
        this.playerCount = playerCount;
        this.tokenCount = tokenCount;
        this.shapeType = shapeType;

        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);

        nodeGridPositions = createNodePositions();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!shapeType.equals("사각형")) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cellSize = (getHeight() - 2 * PADDING) / (GRID - 1);

        for (Point gridPos : nodeGridPositions) {
            int x = PADDING + gridPos.x * cellSize - NODE_SIZE / 2;
            int y = PADDING + gridPos.y * cellSize - NODE_SIZE / 2;

            g2.setColor(Color.BLACK);
            g2.drawOval(x, y, NODE_SIZE, NODE_SIZE);

            if (isImportant(gridPos)) {
                int inner = NODE_SIZE - 6;
                g2.drawOval(x + 3, y + 3, inner, inner);
            }
        }

        // 출발 위치 표시
        Point start = new Point(5, 5); // 오른쪽 아래
        int sx = PADDING + start.x * cellSize;
        int sy = PADDING + start.y * cellSize;

        g2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        g2.drawString("출발", sx - 10, sy + 35);
    }

    private List<Point> createNodePositions() {
        List<Point> nodes = new ArrayList<>();

        // 바깥 사각형 - 시계방향 20개
        for (int i = 0; i <= 5; i++) nodes.add(new Point(i, 0));        // 위쪽
        for (int i = 1; i <= 5; i++) nodes.add(new Point(5, i));        // 오른쪽
        for (int i = 4; i >= 0; i--) nodes.add(new Point(i, 5));        // 아래쪽
        for (int i = 4; i >= 1; i--) nodes.add(new Point(0, i));        // 왼쪽

        // 대각선 ↘
        nodes.add(new Point(1, 1));
        nodes.add(new Point(2, 2));
        nodes.add(new Point(3, 3));
        nodes.add(new Point(4, 4));

        // 대각선 ↙
        nodes.add(new Point(4, 1));
        nodes.add(new Point(3, 2));
        nodes.add(new Point(2, 3));
        nodes.add(new Point(1, 4));

        // 중앙 (2,2) 중복 방지
        Point center = new Point(2, 2);
        if (!nodes.contains(center)) nodes.add(center);

        return nodes;
    }

    private boolean isImportant(Point p) {
        return p.equals(new Point(0, 0)) ||
               p.equals(new Point(5, 0)) ||
               p.equals(new Point(0, 5)) ||
               p.equals(new Point(5, 5)) ||
               p.equals(new Point(2, 2));
    }
}
