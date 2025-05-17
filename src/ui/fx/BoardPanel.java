package ui.fx;
//
//import controller.GameController;
//import javafx.animation.PauseTransition;
//import javafx.geometry.Point2D;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.util.Duration;
//import model.Token;
//import model.TossResult;
//
//import java.util.*;
//

public class BoardPanel {

}

//public class BoardPanel {
//    private final int NODE_SIZE = 28;
//    private final int IMPORTANT_EXTRA = 6;
//    private final int GRID = 6;
//    private final int PADDING = 40;
//
//    private final String shapeType;
//    private ImageView yutImageView;
//    private PauseTransition yutTimer;
//
//    private final List<Point2D> nodeGridPositions;
//    private final List<Point2D[]> lineConnections;
//    private final List<Integer> importantIndices = new ArrayList<>();
//    private final List<Point2D> actualNodePositions = new ArrayList<>();
//
//    private int[] idToRaw;
//    private int[] rawToId;
//
//    private boolean clickable = false;
//    private TokenPanel tokenPanel;
//    private final GameController controller;
//
//    private final Canvas canvas = new Canvas();
//
//    public BoardPanel(int playerCount, int tokenCount, String shapeType, GameController controller) {
//        this.shapeType = shapeType;
//        this.controller = controller;
//
//        setPrefSize(800, 800);
//
//        List<Point2D> raw;
//        List<Point2D[]> rawLines;
//
//        switch (shapeType) {
//            case "사각형" -> {
//                raw = createSquareNodePositions();
//                rawLines = createSquareLineConnections();
//            }
//            case "오각형" -> {
//                raw = createPentagonNodePositions();
//                rawLines = createPentagonLineConnections(raw);
//            }
//            case "육각형" -> {
//                raw = createHexagonNodePositions();
//                rawLines = createHexagonLineConnections(raw);
//            }
//            default -> throw new IllegalArgumentException("지원하지 않는 shapeType: " + shapeType);
//        }
//
//        switch (shapeType) {
//            case "사각형" -> idToRaw = new int[]{10,9,8,7,6,5,4,3,2,1,0,19,18,17,16,15,14,13,12,11,26,24,27,25,20,22,21,23,28};
//            case "오각형" -> idToRaw = new int[]{5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,0,1,2,3,4,29,28,31,30,33,32,35,34,27,26,25};
//            case "육각형" -> idToRaw = new int[]{15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,38,37,40,39,42,41,32,31,34,33,36,35,30};
//        }
//        rawToId = new int[raw.size()];
//        for (int i = 0; i < idToRaw.length; i++) rawToId[idToRaw[i]] = i;
//
//        nodeGridPositions = new ArrayList<>(raw.size());
//        for (int id = 0; id < raw.size(); id++) nodeGridPositions.add(raw.get(idToRaw[id]));
//
//        lineConnections = new ArrayList<>(rawLines.size());
//        for (Point2D[] edge : rawLines) {
//            int fromRaw = raw.indexOf(edge[0]);
//            int toRaw = raw.indexOf(edge[1]);
//            lineConnections.add(new Point2D[]{
//                    nodeGridPositions.get(rawToId[fromRaw]),
//                    nodeGridPositions.get(rawToId[toRaw])
//            });
//        }
//
//        getChildren().add(canvas);
//        canvas.widthProperty().bind(widthProperty());
//        canvas.heightProperty().bind(heightProperty());
//
//        widthProperty().addListener((obs, o, n) -> drawBoard());
//        heightProperty().addListener((obs, o, n) -> drawBoard());
//
//        drawBoard();
//
//        setOnMouseClicked(this::handleNodeClickInternal);
//    }
//
//    public void showYutImage(TossResult result) {
//        String fileName = switch (result) {
//            case DO -> "do.png";
//            case GAE -> "gae.png";
//            case GEOL -> "geol.png";
//            case YUT -> "yut.png";
//            case MO -> "mo.png";
//            case BACKDO -> "backdo.png";
//        };
//
//        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/" + fileName)).toExternalForm(),
//                250, 250, true, true);
//
//        if (yutImageView != null) getChildren().remove(yutImageView);
//        yutImageView = new ImageView(image);
//
//        double iw = image.getWidth();
//        double ih = image.getHeight();
//        yutImageView.setLayoutX((getWidth() - iw) / 2);
//        yutImageView.setLayoutY((getHeight() - ih) / 2);
//        getChildren().add(yutImageView);
//
//        if (yutTimer != null) yutTimer.stop();
//        yutTimer = new PauseTransition(Duration.seconds(3));
//        yutTimer.setOnFinished(e -> removeYutImage());
//        yutTimer.playFromStart();
//    }
//
//    public void removeYutImage() {
//        if (yutTimer != null) yutTimer.stop();
//        if (yutImageView != null) {
//            getChildren().remove(yutImageView);
//            yutImageView = null;
//        }
//    }
//
//    public int getCellSize() {
//        return (int) Math.min(getHeight() - 2 * PADDING, getWidth() - 2 * PADDING) / (GRID - 1);
//    }
//
//    public int getOffsetX() {
//        int cell = getCellSize();
//        return (int) ((getWidth() - cell * (GRID - 1)) / 2);
//    }
//
//    public int getOffsetY() {
//        int cell = getCellSize();
//        return (int) ((getHeight() - cell * (GRID - 1)) / 2);
//    }
//
//    public Point2D getNodePosition(int idx) { return nodeGridPositions.get(idx); }
//
//    public void moveToken(Token token) { /* TODO */ }
//
//    public int autoClickPosition(int positionId) {
//        Point2D pos = getNodePosition(positionId);
//        int cell = getCellSize();
//        int offsetX = getOffsetX();
//        int offsetY = getOffsetY();
//        double x = toPixelX(pos.getX(), cell, offsetX);
//        double y = toPixelY(pos.getY(), cell, offsetY);
//
//        MouseEvent evt = new MouseEvent(MouseEvent.MOUSE_CLICKED,
//                x, y,
//                x, y,
//                MouseButton.PRIMARY, 1,
//                false, false, false, false,
//                true, false, false, true,
//                true, false, null);
//        fireEvent(evt);
//        System.out.println("[BoardPanel] Programmatically clicked position: " + positionId);
//        return positionId;
//    }
//
//    public void setClickable(boolean clickable) { this.clickable = clickable; }
//    public void setTokenPanel(TokenPanelFX tokenPanel) { this.tokenPanel = tokenPanel; }
//
//    private void drawBoard() {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.clearRect(0, 0, getWidth(), getHeight());
//        gc.setLineWidth(2);
//        gc.setFont(new Font("맑은 고딕", 12));
//
//        int cellSize = getCellSize();
//        int offsetX = getOffsetX();
//        int offsetY = getOffsetY();
//
//        actualNodePositions.clear();
//
//        for (Point2D grid : nodeGridPositions) {
//            boolean important = isImportant(grid);
//            int size = NODE_SIZE + (important ? IMPORTANT_EXTRA : 0);
//            double x = toPixelX(grid.getX(), cellSize, offsetX) - size / 2.0;
//            double y = toPixelY(grid.getY(), cellSize, offsetY) - size / 2.0;
//            actualNodePositions.add(new Point2D(x, y));
//
//            gc.setStroke(Color.BLACK);
//            gc.strokeOval(x, y, size, size);
//            if (important) {
//                int inner = size - 8;
//                gc.strokeOval(x + (size - inner) / 2.0, y + (size - inner) / 2.0, inner, inner);
//            }
//        }
//
//        for (Point2D[] pair : lineConnections) {
//            drawLineEdgeToEdge(gc, pair[0], pair[1], cellSize, offsetX, offsetY);
//        }
//
//        Point2D start = getStartPosition();
//        double sx = toPixelX(start.getX(), cellSize, offsetX);
//        double sy = toPixelY(start.getY(), cellSize, offsetY);
//        gc.fillText("출발", sx - gc.getFont().getSize(), sy + gc.getFont().getSize() / 2.0);
//    }
//
//    private void drawLineEdgeToEdge(GraphicsContext gc, Point2D from, Point2D to,
//                                    int cellSize, int offsetX, int offsetY) {
//        double x1 = toPixelX(from.getX(), cellSize, offsetX);
//        double y1 = toPixelY(from.getY(), cellSize, offsetY);
//        double x2 = toPixelX(to.getX(), cellSize, offsetX);
//        double y2 = toPixelY(to.getY(), cellSize, offsetY);
//
//        double dx = x2 - x1;
//        double dy = y2 - y1;
//        double dist = Math.hypot(dx, dy);
//
//        double r1 = (isImportant(from) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;
//        double r2 = (isImportant(to) ? NODE_SIZE + IMPORTANT_EXTRA : NODE_SIZE) / 2.0 + 1;
//
//        double newX1 = x1 + dx * r1 / dist;
//        double newY1 = y1 + dy * r1 / dist;
//        double newX2 = x2 - dx * r2 / dist;
//        double newY2 = y2 - dy * r2 / dist;
//
//        gc.strokeLine(newX1, newY1, newX2, newY2);
//    }
//
//    private void handleNodeClickInternal(MouseEvent e) {
//        if (!clickable) return;
//        int posId = handleNodeClick(e);
//        int currentToken = tokenPanel != null ? tokenPanel.getCurrentToken() : -1;
//        if (currentToken == -1) {
//            System.out.println("[BoardPanel] 잘못된 토큰 선택. 현재 플레이어의 토큰이 아닙니다.");
//            if (tokenPanel != null) tokenPanel.setClickable(true);
//            return;
//        }
//
//        List<Token> stacked = controller.findTokenById(currentToken).getStackedTokens();
//        boolean ok = controller.onMoveTokens(controller.onClickPosition(posId));
//        if (!ok) {
//            System.out.println("[BoardPanel] 해당 위치로 움직일 수 없습니다. 다시 시도하세요.");
//        } else {
//            System.out.println("[BoardPanel] 말을 " + posId + "번째 칸으로 이동했습니다.");
//            tokenPanel.updateTokenPosition(currentToken, posId);
//            controller.onStackTokens(controller.findTokenById(currentToken), controller.onClickPosition(posId));
//            for (Token t : stacked) tokenPanel.updateTokenPosition(t.getId(), posId);
//            tokenPanel.setClickable(true);
//            tokenPanel.repaint();
//            setClickable(false);
//        }
//    }
//
//    public int handleNodeClick(MouseEvent e) {
//        Point2D clickPoint = new Point2D(e.getX(), e.getY());
//        for (int i = 0; i < nodeGridPositions.size(); i++) {
//            Point2D node = actualNodePositions.get(i);
//            if (clickPoint.distance(node) < NODE_SIZE) {
//                System.out.println(i + "번째 칸 클릭됨");
//                return i;
//            }
//        }
//        return idToRaw[0];
//    }
//
//    private double toPixelX(double x, int cellSize, int offsetX) { return offsetX + x * cellSize; }
//    private double toPixelY(double y, int cellSize, int offsetY) { return offsetY + y * cellSize; }
//
//    private Point2D getStartPosition() {
//        return switch (shapeType) {
//            case "사각형" -> new Point2D(GRID - 1, GRID - 1);
//            case "오각형", "육각형" -> nodeGridPositions.get(0);
//            default -> new Point2D(0, 0);
//        };
//    }
//
//    private boolean isImportant(Point2D p) {
//        return importantIndices.stream().anyMatch(idx -> isClose(p, nodeGridPositions.get(idx)));
//    }
//
//    private boolean isClose(Point2D p1, Point2D p2) {
//        double tol = 0.01;
//        return Math.abs(p1.getX() - p2.getX()) < tol && Math.abs(p1.getY() - p2.getY()) < tol;
//    }
//
//    private List<Point2D> createSquareNodePositions() {
//        List<Point2D> nodes = new ArrayList<>();
//        importantIndices.clear();
//
//        // 외곽
//        for (int i = 0; i <= GRID - 1; i++) nodes.add(new Point2D(i, 0));
//        for (int i = 1; i <= GRID - 1; i++) nodes.add(new Point2D(GRID - 1, i));
//        for (int i = GRID - 2; i >= 0; i--) nodes.add(new Point2D(i, GRID - 1));
//        for (int i = GRID - 2; i >= 1; i--) nodes.add(new Point2D(0, i));
//
//        // 꼭짓점
//        importantIndices.addAll(List.of(0, GRID - 1, 2 * GRID - 2, 3 * GRID - 3));
//
//        // 대각선
//        for (int i = 1; i <= 5; i++) {
//            double r = i / 6.0;
//            if (i == 3) continue;
//            nodes.add(new Point2D(r * (GRID - 1), r * (GRID - 1)));   // ↘
//            nodes.add(new Point2D(r * (GRID - 1), (1 - r) * (GRID - 1))); // ↙
//        }
//
//        // 중심
//        double c = (GRID - 1) / 2.0;
//        nodes.add(new Point2D(c, c));
//        importantIndices.add(nodes.size() - 1);
//
//        return nodes;
//    }
//
//    private List<Point2D[]> createSquareLineConnections() {
//        List<Point2D[]> lines = new ArrayList<>();
//        // 외곽
//        for (int i = 0; i < GRID - 1; i++) lines.add(new Point2D[]{new Point2D(i, 0), new Point2D(i + 1, 0)});
//        for (int i = 0; i < GRID - 1; i++) lines.add(new Point2D[]{new Point2D(GRID - 1, i), new Point2D(GRID - 1, i + 1)});
//        for (int i = GRID - 1; i > 0; i--) lines.add(new Point2D[]{new Point2D(i, GRID - 1), new Point2D(i - 1, GRID - 1)});
//        for (int i = GRID - 1; i >= 1; i--) lines.add(new Point2D[]{new Point2D(0, i), new Point2D(0, i - 1)});
//
//        // 대각선 ↘ / ↙
//        for (int i = 0; i < 6; i++) {
//            double x1 = i / 6.0 * (GRID - 1);
//            double y1 = i / 6.0 * (GRID - 1);
//            double x2 = (i + 1) / 6.0 * (GRID - 1);
//            double y2 = (i + 1) / 6.0 * (GRID - 1);
//            lines.add(new Point2D[]{new Point2D(x1, y1), new Point2D(x2, y2)});
//        }
//        for (int i = 0; i < 6; i++) {
//            double x1 = i / 6.0 * (GRID - 1);
//            double y1 = (1 - i / 6.0) * (GRID - 1);
//            double x2 = (i + 1) / 6.0 * (GRID - 1);
//            double y2 = (1 - (i + 1) / 6.0) * (GRID - 1);
//            lines.add(new Point2D[]{new Point2D(x1, y1), new Point2D(x2, y2)});
//        }
//        return lines;
//    }
//
//    private List<Point2D> createPentagonNodePositions() {
//        List<Point2D> nodes = new ArrayList<>();
//        importantIndices.clear();
//
//        int outerCount    = 5;
//        int nodesPerEdge  = 6;
//        double radius     = 3.0;
//        double centerX    = 2.5, centerY = 2.8;
//
//        List<Point2D> corners = new ArrayList<>();
//        for (int i = 0; i < outerCount; i++) {
//            double angle = Math.toRadians(90 + i * 72);
//            double x = centerX + radius * Math.cos(angle);
//            double y = centerY - radius * Math.sin(angle);
//            corners.add(new Point2D(x, y));
//        }
//
//        for (int i = 0; i < outerCount; i++) {
//            Point2D start = corners.get(i);
//            Point2D end   = corners.get((i + 1) % outerCount);
//            for (int j = 0; j < nodesPerEdge; j++) {
//                double t = j / (double)(nodesPerEdge - 1);
//                double x = start.getX() * (1 - t) + end.getX() * t;
//                double y = start.getY() * (1 - t) + end.getY() * t;
//                Point2D node = new Point2D(x, y);
//                if (!nodes.contains(node)) nodes.add(node);
//                if (j == 0) importantIndices.add(nodes.size() - 1);
//            }
//        }
//
//        // 중심
//        Point2D center = new Point2D(centerX, centerY);
//        nodes.add(center);
//        importantIndices.add(35);
//
//        // 중심‑꼭짓점 간의 2개 노드씩
//        for (Point2D corner : corners) {
//            for (int j = 1; j <= 2; j++) {
//                double t = j / 3.0;
//                double x = centerX * (1 - t) + corner.getX() * t;
//                double y = centerY * (1 - t) + corner.getY() * t;
//                nodes.add(new Point2D(x, y));
//            }
//        }
//        return nodes;
//    }
//
//    private List<Point2D[]> createPentagonLineConnections(List<Point2D> raw) {
//        List<Point2D[]> lines = new ArrayList<>();
//        for (int i = 0; i < 25; i++) lines.add(new Point2D[]{ raw.get(i), raw.get((i + 1) % 25) });
//        int center = 25, start = 26;
//        for (int i = 0; i < 5; i++) {
//            int tip = i * 5;
//            int i1  = start + 2 * i;
//            int i2  = i1 + 1;
//            lines.add(new Point2D[]{ raw.get(center), raw.get(i1) });
//            lines.add(new Point2D[]{ raw.get(i1), raw.get(i2) });
//            lines.add(new Point2D[]{ raw.get(i2), raw.get(tip) });
//        }
//        return lines;
//    }
//
//    private List<Point2D> createHexagonNodePositions() {
//        List<Point2D> nodes = new ArrayList<>();
//        importantIndices.clear();
//
//        int sides = 6;
//        int nodesPerEdge = 6;
//        double radius = 3.0;
//        double centerX = 2.5, centerY = 2.5;
//
//        List<Point2D> corners = new ArrayList<>();
//        for (int i = 0; i < sides; i++) {
//            double angle = Math.toRadians(i * 60);
//            double x = centerX + radius * Math.cos(angle);
//            double y = centerY - radius * Math.sin(angle);
//            corners.add(new Point2D(x, y));
//        }
//
//        for (int i = 0; i < sides; i++) {
//            Point2D start = corners.get(i);
//            Point2D end   = corners.get((i + 1) % sides);
//            for (int j = 0; j < nodesPerEdge; j++) {
//                double t = j / (double)(nodesPerEdge - 1);
//                double x = start.getX() * (1 - t) + end.getX() * t;
//                double y = start.getY() * (1 - t) + end.getY() * t;
//                Point2D node = new Point2D(x, y);
//                if (!nodes.contains(node)) nodes.add(node);
//                if (j == 0) importantIndices.add(nodes.size() - 1);
//            }
//        }
//
//        Point2D center = new Point2D(centerX, centerY);
//        nodes.add(center);
//        importantIndices.add(42);
//
//        // 중심‑꼭짓점 간 2개 노드씩
//        for (Point2D corner : corners) {
//            for (int j = 1; j <= 2; j++) {
//                double t = j / 3.0;
//                double x = centerX * (1 - t) + corner.getX() * t;
//                double y = centerY * (1 - t) + corner.getY() * t;
//                nodes.add(new Point2D(x, y));
//            }
//        }
//        return nodes;
//    }
//
//    private List<Point2D[]> createHexagonLineConnections(List<Point2D> raw) {
//        List<Point2D[]> lines = new ArrayList<>();
//        int outer = 30;
//        for (int i = 0; i < outer; i++) {
//            lines.add(new Point2D[]{ raw.get(i), raw.get((i + 1) % outer) });
//        }
//        int start = outer + 1;
//        for (int i = 0; i < 6; i++) {
//            int tip = i * 5;
//            int i1 = start + 2 * i;
//            int i2 = i1 + 1;
//            lines.add(new Point2D[]{ raw.get(outer), raw.get(i1) });
//            lines.add(new Point2D[]{ raw.get(i1), raw.get(i2) });
//            lines.add(new Point2D[]{ raw.get(i2), raw.get(tip) });
//        }
//        return lines;
//    }
//}
