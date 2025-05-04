package model;

import java.util.ArrayList;
import java.util.List;

public class Token {
    private int id;
    private TokenType type;
    private Position position;
    private boolean isFinished;
    private Player owner;
    private List<Token> stackedTokens;  //말을 업을 때 사용하는 리스트

    //  Player에서 Token을 생성할 때 사용하는 생성자
    public Token(int id, TokenType type, Player owner) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.position = null;        // 아직 시작 위치 없음
        this.isFinished = false;     // 초기 상태는 미완료
        this.stackedTokens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Player getOwner() {
        return owner;
    }

    public void reset() {
        this.position = null;       // 시작 위치로 초기화 (출발점 없음)
        this.isFinished = false;    // 다시 완주하지 않은 상태로 초기화
    }

    public List<Token> getStackedTokens(){
        return stackedTokens;
    }

    public void stackWith(Token other) { // 말을 업음
        this.stackedTokens.add(other);
        this.stackedTokens.addAll(other.getStackedTokens()); // 업힌 말이 업고 있는 다른 말들을 업는다.
        other.getStackedTokens().clear();
    }

    public void moveTo(Position newPos) {
        this.position = newPos;
        for (Token t : stackedTokens) {
            t.position = newPos;
        }
    }

}


