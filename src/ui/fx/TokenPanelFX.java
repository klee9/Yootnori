package ui.fx;

import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ui.interfaces.TokenPanel;

import java.util.ArrayList;
import java.util.List;

public class TokenPanelFX extends Pane implements TokenPanel {

    private final Canvas canvas = new Canvas();
    private final BoardPanelFX board;
    private final MainWindowFX mainWindow;

    private final List<Integer> nodeIndices = new ArrayList<>();
    private final List<Integer> tokens = new ArrayList<>();
    private final List<Point2D> tokenPositions = new ArrayList<>();

    private boolean clickable = true;
    private final int diameter = 24;

    private final Color[] colors = {
            Color.CORNFLOWERBLUE,
            Color.LIGHTCORAL,
            Color.GOLD,
            Color.LIGHTGREEN
    };

    private int playerCount;
    private int tokenCount;
    private int clickedToken = 0;

    public TokenPanelFX(BoardPanelFX board, MainWindowFX mainWindow, int playerCount, int tokenCount) {
        this.board = board;
        this.mainWindow = mainWindow;
        this.playerCount = playerCount;
        this.tokenCount = tokenCount;

        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // 크기가 바뀔 때마다 리페인트
        canvas.widthProperty().addListener((obs, oldW, newW) -> repaint());
        canvas.heightProperty().addListener((obs, oldH, newH) -> repaint());

        //UI가 완전히 로드된 직후 한 번만 리페인트
        Platform.runLater(this::repaint);

        // 초기 토큰 위치 설정
        double startX = 650;
        double startY = 250;

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int tokenId = 10 * i + j;
                tokens.add(tokenId);
                tokenPositions.add(new Point2D(startX + diameter * j + j * 8, startY + diameter * i + i * 15));
            }
        }

        setOnMouseClicked(this::handleMouseClick);
        setOnKeyPressed(this::handleKeyPress);
        repaint();
    }

    private void handleMouseClick(MouseEvent e) {
        int tokenId = getClickedToken(e);
        setClickable(false);

        if (tokenId != -1) {
            mainWindow.onClickToken(tokenId);
        } else {
            board.fireEvent(e); // 보드로 이벤트 전달
        }
    }

    @Override
    public void updateTokenPosition(int tokenId, int positionId) {
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);

        if (positionId == -1) {
            tokenPositions.set(idx, new Point2D(50, 600));
            repaint();
            return;
        }

        int cellSize = board.getCellSize();
        int offsetX = board.getOffsetX();
        int offsetY = board.getOffsetY();
        Point2D grid = board.getNodePosition(positionId);

        // BoardPanel 의 toPixelX, toPixelY 호출값 동일하게 유지하기 위해 int 명시적 캐스팅
        int x = (int)board.toPixelX(grid.getX(), cellSize, offsetX);
        int y = (int)board.toPixelY(grid.getY(), cellSize, offsetY);

        tokenPositions.set(idx, new Point2D(x - diameter / 2.0, y - diameter / 2.0));
        repaint();
    }

    @Override
    public void updateTokenPosition(int tokenId, double x, double y) {
        Point2D position = new Point2D(x, y);
        int idx = (tokenId / 10) * tokenCount + (tokenId % 10);
        tokenPositions.set(idx, position);
        repaint();
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public int getCurrentToken() {
        return clickedToken;
    }

    // 원래 코드에서 paintComponent 역할
    public void repaint() {
        GraphicsContext g2 = canvas.getGraphicsContext2D();
        g2.clearRect(0, 0, getWidth(), getHeight());

        // 말 그리기
        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int x = (int) tokenPositions.get(i * tokenCount + j).getX();
                int y = (int) tokenPositions.get(i * tokenCount + j).getY();
                g2.setFill(colors[i]);
                g2.fillOval(x, y, diameter, diameter);
            }
        }

        // 겹침 수 표기
        g2.setFill(Color.BLACK);
        g2.setFont(javafx.scene.text.Font.font("SansSerif", javafx.scene.text.FontWeight.BOLD, 12));

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < tokenCount; j++) {
                int idx = i * tokenCount + j;
                Point2D pi = tokenPositions.get(idx);

                int overlap = 0;
                for (int jj = 0; jj < tokenCount; jj++) {
                    if (jj == j) continue;
                    int otherIdx = i * tokenCount + jj;
                    Point2D pj = tokenPositions.get(otherIdx);
                    if (pi.distance(pj) < 1e-3) overlap++;
                }

                if (overlap > 0) {
                    String label = "+" + overlap;
                    double tx = pi.getX() + diameter / 2 + 2;
                    double ty = pi.getY() - diameter / 2 + 12;
                    g2.fillText(label, tx, ty);
                }
            }
        }
    }

    @Override
    public int getClickedToken(Object evt) {
        if (evt instanceof javafx.scene.input.MouseEvent e) {
            if (!clickable) return -1;

            Point2D clickPoint = new Point2D(e.getX(), e.getY());

            for (int i = 0; i < tokens.size(); i++) {
                Point2D token = tokenPositions.get(i);
                if (clickPoint.distance(token) < diameter) {
                    int tokenId = tokens.get(i);
                    int currentPlayerId = mainWindow.getCurrentPlayerId();
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
        return -1;
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.R) {
            clickable = true;
        }
    }
}
