package model;

import java.util.List;

public class RuleSet {
    public boolean getAdditionalTurn(int throwResult, int  moveResult) {
        return ((throwResult == 4 || throwResult == 5) && moveResult != 7) || (throwResult < 4 && moveResult == 7); // -1~5: 윷 던지기 결과, 6: 업기, 7: 잡기라고 가정
    }

    public boolean checkToss(int tossResult) {
        return tossResult >= -1 && tossResult <= 5;
    }

    public boolean checkMove(Position currPos, Position nextPos, int tossResult) {
        // 백도
        if (tossResult == -1) {
            return currPos.getPrevPositions().contains(nextPos);
        }

        // 도개걸윷모
        else {
            List<Position> nextPositions = currPos.getNextPositions(); // for sqaure boards
            if (currPos.isCenter() && currPos.getId()/7 != 4) {     // for non-square boards
                nextPositions = currPos.getNextPositions().subList(0, 1);
            }

            for (Position p : nextPositions) {
                for (int i = 1; i < tossResult; i++) {
                    p = p.getNextPositions().get(0);
                    if (p.isGoal()) {
                        return true;
                    }
                }
                if (p.getId() == nextPos.getId()) {
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
        boolean result = tokenA.getOwner() != tokenB.getOwner() && tokenA.getPosition() == tokenB.getPosition();
        if (result) {
            tokenA.getOwner().addTurn(1);
        }
        return result;
    }
}
