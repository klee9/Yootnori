package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Token {
    private final int id;
    private final Player owner;

    private boolean isFinished;
    private List<Token> stackedTokens;

    private Position position;
    private Position initialPosition;
    private Point2D.Double initialCoordinates;

    public Token(int id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.isFinished = false;
        this.stackedTokens = new ArrayList<>();
    }

    public void setPosition(Position initialPosition) {
        double startX = 650; // TODO: hard-coded -> relative value로 바꾸기
        double startY = 250;

        int diameter = 24;
        int i = id % 10;
        int ownerId = owner.getId();

        double x = startX + diameter * i + i * 8;
        double y = startY + diameter * ownerId + ownerId * 15;

        this.position = initialPosition;
        this.initialPosition = initialPosition;
        this.initialCoordinates = new Point2D.Double(x, y);
    }

    public void moveTo(Position newPos) {
        this.position = newPos;
        for (Token t : this.stackedTokens) {
            t.position = newPos;
        }
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
        for (Token token : this.stackedTokens) {
            token.isFinished = finished;
        }
    }

    public void reset() {
        // 말과 업힌 말 모두 리셋
        this.position = initialPosition;
        this.isFinished = false;

        for (Token token : this.stackedTokens) {
            token.position = initialPosition;
            token.isFinished = false;
        }

        // unstacking (업기 상태 해제)
        for (Token token : this.stackedTokens) {
            token.getStackedTokens().clear();
        }
        this.stackedTokens.clear();
    }

    // 현재 말과 대상 말 및 업힌 말을 모두 셋에 추가
    // ex) 0이 1, 2와 업혀 있고, 1이 3, 4와 업혀 있다면, 전체 셋을 [0, 1, 2, 3, 4]로 초기화. 자기 자신에 해당하는 말을 삭제
    public void stackWith(Token other) {
        Set<Token> stack = new HashSet<>();
        stack.add(this);
        stack.add(other);
        stack.addAll(this.stackedTokens);
        stack.addAll(other.stackedTokens);

        List<Token> combinedStack = new ArrayList<>(stack);

        for (Token t : combinedStack) {
            t.stackedTokens = new ArrayList<>(combinedStack);
            t.stackedTokens.remove(t);
        }
    }

    // getters
    public int getId() { return id; }
    public Player getOwner() { return owner; }
    public boolean isFinished() { return isFinished; }
    public Position getPosition() { return position; }
    public List<Token> getStackedTokens(){ return stackedTokens; }
    public Point2D.Double getInitialCoordinates() { return initialCoordinates; }
}
