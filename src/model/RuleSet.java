package model;

import java.util.List;

public class RuleSet {
    public boolean checkMove(Position currPos, Position nextPos, TossResult tossResult) {
        System.out.println("[RuleSet] " + currPos.getId() + "에서 " + nextPos.getId() + "로 이동 가능 여부 확인 중...");
        // 백도
        if (tossResult == TossResult.BACKDO) {
            return currPos.getPrevPositions().contains(nextPos);
        }

        // 도개걸윷모
        else {
            List<Position> nextPositions = currPos.getNextPositions(); // for square boards
            if (currPos.isCenter() && currPos.getId()/7 != 4) {        // for non-square boards
                nextPositions = currPos.getNextPositions().subList(0, 1);
            }

            for (Position p : nextPositions) {
                Position q = p;
                for (int i = 1; i < tossResult.getValue(); i++) {
                    if (q.isGoal()) { return true; }
                    q = q.getNextPositions().get(0);
                }
                if (q.getId() == nextPos.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canStack(Token tokenA, Token tokenB) {
        if (tokenA.getStackedTokens().contains(tokenB)) {
            return false;
        }
        return tokenA.getId() != tokenB.getId() && tokenA.getOwner() == tokenB.getOwner() && tokenA.getPosition() == tokenB.getPosition();
    }

    public boolean canCapture(Token tokenA, Token tokenB) {
        boolean result = tokenA.getOwner() != tokenB.getOwner() && tokenA.getPosition().getId() == tokenB.getPosition().getId();
        if (result) {
            tokenA.getOwner().addTurn(1);
        }
        return result;
    }
}