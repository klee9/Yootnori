package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String name;
    private List<Token> tokens;
    private int turns = 1;

    // 플레이어 생성자 플레이어 이름 ,플레이어 ID, 말의 개수, 말의 모양(type)로 생성한다.
    public Player(String name, int playerId, int tokenCount, Color color) {
        this.name = name;
        this.playerId = playerId;
        this.tokens = new ArrayList<>();
        for (int i = 0; i < tokenCount; i++){ // 말(token)을 tokens에 개수만큼 추가한다.
            tokens.add(new Token(playerId*10 + i, color,this));
        }
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void removeToken(Token token) {
        this.tokens.remove(token);
    }

    public void resetTokens() {
        for (Token token : tokens) {
            token.reset(); // 각 토큰 초기화
        }
    }

    public void addTurn(int count) {
        this.turns += count;
    }

    public boolean hasFinished() {
        for (Token token : tokens) {
            if (!token.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public String getName() { return name; }
    public int getPlayerId() { return playerId; }
    public int getTurns() { return turns; }
    public List<Token> getTokens(){ return tokens; }
}
