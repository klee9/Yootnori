package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String name;
    private List<Token> tokens;
    private int turns;

    public Player(String name, int id, int tokenCount) {
        this.name = name;
        this.id = id;
        this.tokens = new ArrayList<>();
        this.turns = 1;

        for (int i = 0; i < tokenCount; i++) {
            int tokenId = id*10 + i;
            tokens.add(new Token(tokenId,this));
        }
    }

    public void addTurn(int count) {
        this.turns += count;
    }

    public int getRemainingTokens() {
        return (int) tokens.stream().filter(token -> !token.isFinished()).count();
    }

    // getters
    public int getTurns() { return turns; }
    public String getName() { return name; }
    public int getId() { return id; }
    public List<Token> getTokens(){ return tokens; }
}