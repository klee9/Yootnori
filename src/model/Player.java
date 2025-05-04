package model;

import java.util.List;

public class Player {
    private int id;
    private String name;
    private List<Token> tokens;
    private boolean has_finished;

    public List<Token> getTokens() {
        return tokens;
    }

    public void resetTokens() {
        tokens.clear();
    }

    public int getId() {
        return id;
    }
}