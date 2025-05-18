package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Token {
    private int id;
    private Color color;
    private Position position;
    private boolean isFinished;
    private Player owner;
    private List<Token> stackedTokens;  //말을 업을 때 사용하는 리스트
    private final Point2D.Double startPosition;
    private final Position initialPosition;

    //  Player에서 Token을 생성할 때 사용하는 생성자
    public Token(int id, Color color, Player owner, Board board, Point2D.Double startPosition, Position initialPosition) {
        this.id = id;
        this.color = color;
        this.owner = owner;
        this.position = board.getStartPosition();
        this.isFinished = false;     // 초기 상태는 미완료
        this.stackedTokens = new ArrayList<>();
        this.startPosition = startPosition;
        this.initialPosition = initialPosition;
    }

    public void setFinished(boolean finished) {
        List<Token> stackedTokens = this.stackedTokens;
        this.isFinished = finished;
        for (Token token : stackedTokens) {
            token.isFinished = finished;
        }
    }

    public void reset() {
        this.position = initialPosition;
        this.isFinished = false;    // 다시 완주하지 않은 상태로 초기화
    }

    public void stackWith(Token other) { // 말을 업음
        if (this.id == other.id) return; // Prevent self-stacking
        if (this.stackedTokens.contains(other) || other.stackedTokens.contains(this)) return;

        if (!this.stackedTokens.contains(other)) {
            this.stackedTokens.add(other);
        }

        if (!other.stackedTokens.contains(this)) {
            other.stackedTokens.add(this);
        }

        List<Token> combinedTokens = new ArrayList<>(this.stackedTokens);
        combinedTokens.addAll(other.getStackedTokens());
        combinedTokens.add(this);
        combinedTokens.add(other);

        for (Token t1 : combinedTokens) {
            for (Token t2 : combinedTokens) {
                if (t1 != t2 && !t1.getStackedTokens().contains(t2)) {
                    t1.getStackedTokens().add(t2);
                }
            }
        }
    }

    public void moveTo(Position newPos) {
        this.position = newPos;
        for (Token t : stackedTokens) {
            System.out.println("[Token]: Moving " + t.getId() + " to " + newPos.getId());
            t.position = newPos;
        }
    }

    // getters
    public boolean isFinished() { return isFinished; }
    public Point2D.Double getStartPosition() { return startPosition; }
    public int getId() { return id; }
    public Position getPosition() { return position; }
    public Player getOwner() { return owner; }
    public List<Token> getStackedTokens(){ return stackedTokens; }
}

