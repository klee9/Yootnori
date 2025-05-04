package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Token> tokens;
    private int playerId;

    // 플레이어 생성자 플레이어 이름 ,플레이어 ID, 말의 개수, 말의 모양(type)로 생성한다.
    public Player(String name, int playerId, int tokenCount, TokenType type) {
        this.name = name;
        this.playerId = playerId;
        this.tokens = new ArrayList<>();
        for ( int i = 0; i < tokenCount; i++){ // 말(token)을 tokens에 개수만큼 추가한다.
            tokens.add(new Token(i, type,this));
        }
    }

    public String getName() {
        return name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public List<Token> getTokens(){
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void resetTokens() {
        for (Token token : tokens) {
            token.reset(); // 각 토큰 초기화
        }
    }

    public boolean hasFinished() {
        for (Token token : tokens) {
            if (!token.isFinished()) {
                return false;
            }
        }
        return true;
    }

}
