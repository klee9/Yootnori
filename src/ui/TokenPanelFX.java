package ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

// BoardPanelFX 구현하기 위한 임시 TokenPanelFX
public class TokenPanelFX extends Pane {
  private final Map<Integer, Circle> tokenNodes = new HashMap<>(); // tokenId -> JavaFX node
  private int currentTokenId = -1;
  private boolean clickable = false;

  public int getCurrentToken() { return currentTokenId; }

  public void setCurrentToken(int tokenId) { this.currentTokenId = tokenId; }

  public void setClickable(boolean clickable) { this.clickable = clickable; }

  public void updateTokenPosition(int tokenId, int boardPosId) {
    double spacing = 15;
    double x = 20 + (boardPosId % 20) * spacing;
    double y = 20 + (boardPosId / 20) * spacing;

    Circle node = tokenNodes.computeIfAbsent(tokenId, id -> {
      Circle c = new Circle(6, Color.CORNFLOWERBLUE);
      getChildren().add(c);
      return c;
    });
    node.setCenterX(x);
    node.setCenterY(y);
  }

  public void repaint() { requestLayout(); }
}
