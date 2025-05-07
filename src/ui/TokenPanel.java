package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TokenPanel extends JComponent {
  private final BoardPanel board;
  private final List<Integer> dummyNodeIndices = new ArrayList<>();
  private final List<Color>   dummyColors      = new ArrayList<>();

  public TokenPanel(BoardPanel board) {
    this.board = board;
    setOpaque(false);

    // 여기에 더미 값 넣어서 테스트하면 됨
    dummyNodeIndices.add(2); // e.g. .add(3), .add(n)
    dummyColors.add(Color.YELLOW); // e.g. Color.RED, Color.BLUE
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    int cellSize = board.getCellSize();
    int offsetX = board.getOffsetX();
    int offsetY = board.getOffsetY();

    int idx   = dummyNodeIndices.get(0);
    Color color = dummyColors.get(0);
    Point2D.Double grid = board.getNodePosition(idx);

    int x = board.toPixelX(grid.x, cellSize, offsetX);
    int y = board.toPixelY(grid.y, cellSize, offsetY);

    g2.setColor(color);
    g2.fillOval(x - 12, y - 12, 24, 24);
  }
}
