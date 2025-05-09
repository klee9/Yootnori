package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String name;
    private List<Token> tokens;
    private int turns = 1;

    // 플레이어 생성자 플레이어 이름 ,플레이어 ID, 말의 개수, 말의 모양(type)로 생성한다.
    public Player(String name, int playerId, int tokenCount, Color color, Board board) {
        this.name = name;
        this.playerId = playerId;
        this.tokens = new ArrayList<>();
        
        double startX = 800 - 150;
        double startY = 600 - 350;
        int diameter = 24;

        for (int i = 0; i < tokenCount; i++) {
            int tokenId = playerId * 10 + i;

            double x = startX + diameter * i + i * 8;
            double y = startY + diameter * playerId + playerId * 15;

            Point2D.Double startPos = new Point2D.Double(x, y);
            Position initialPosition=board.getStartPosition();

            tokens.add(new Token(tokenId, color, this, board, startPos, initialPosition));
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

    public int getRemainingTokens() {
        int cnt = 0;
        for (Token token : tokens) {
            if (!token.isFinished()) {
                cnt++;
            }
        }
        return cnt;
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