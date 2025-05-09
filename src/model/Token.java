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
    private final Point2D.Double startPosition; // 말이 잡혔을 때 처음 위치로 돌아가야 됨
    private final Position initialPosition; // 말이 잡히는 기능 구현하기 위한 논리적인 시작 위치

    //  Player에서 Token을 생성할 때 사용하는 생성자
    public Token(int id, Color color, Player owner, Board board, Point2D.Double startPosition, Position initialPosition) {
        this.id = id;
        this.color = color;
        this.owner = owner;
        this.position = board.getStartPosition();
        this.isFinished = false;     // 초기 상태는 미완료
        this.stackedTokens = new ArrayList<>();
        this.startPosition=startPosition;
        this.initialPosition=initialPosition;
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
        if (this.id == other.id || stackedTokens.contains(other)) { return; }
        // 자기 자신을 제외하고 업기 (circular reference 방지 위함)
        this.stackedTokens.add(other);
        this.stackedTokens.addAll(other.getStackedTokens().stream().filter(token -> token != this).toList());
        other.stackedTokens.add(this);
        other.stackedTokens.addAll(this.getStackedTokens().stream().filter(token -> token != other).toList());
    }

    public void moveTo(Position newPos) {
        this.position = newPos;
        for (Token t : stackedTokens) {
            t.position = newPos;
        }
    }

    // getters
    public boolean isFinished() { return isFinished; }
    public Point2D.Double getStartPosition() {return startPosition;}
    public int getId() { return id; }
    public Position getPosition() { return position; }
    public Player getOwner() { return owner; }
    public List<Token> getStackedTokens(){ return stackedTokens; }
}

