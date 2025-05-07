package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Token {
    private int id;
    private Color color;
    private Position position;
    private boolean isFinished;
    private Player owner;
    private List<Token> stackedTokens;  //말을 업을 때 사용하는 리스트

    //  Player에서 Token을 생성할 때 사용하는 생성자
    public Token(int id, Color color, Player owner) {
        this.id = id;
        this.color = color;
        this.owner = owner;
        this.position = null;        // 아직 시작 위치 없음
        this.isFinished = false;     // 초기 상태는 미완료
        this.stackedTokens = new ArrayList<>();
    }

    public void setFinished(boolean finished) {
        List<Token> stackedTokens = this.stackedTokens;
        this.isFinished = finished;
        for (Token token : stackedTokens) {
            token.isFinished = finished;
        }
    }

    public void reset() {
        this.position = null;       // 시작 위치로 초기화 (출발점 없음)
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

    public int getId() { return id; }
    public Position getPosition() { return position; }
    public Player getOwner() { return owner; }
    public List<Token> getStackedTokens(){ return stackedTokens; }
}
