package model;

import java.util.List;

public class Token {
    private int id;
    private boolean isFinished;
    private Player owner;
    private Position position;
    private List<Token> stacked_with;

    public void moveTo(Position _position) {
        position = _position;
    }

    public void stackWith(Token token) {
        stacked_with.add(token);
    }
}