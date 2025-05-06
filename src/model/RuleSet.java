package model;

import java.util.List;

public class RuleSet {
    public boolean getAdditionalTurn(int tossResult, int  moveResult) {
        return tossResult == 4 || tossResult == 5 || moveResult == 7; // -1~5: 윷 던지기 결과, 6: 업기, 7: 잡기라고 가정
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
            if (currPos.getIsCenter() && currPos.getId()/7 != 4) {     // for non-square boards
                nextPositions = currPos.getNextPositions().subList(0, 1);
            }

            for (Position p : nextPositions) {
                for (int i = 1; i < tossResult; i++) {
                    p = p.getNextPositions().get(0);
                    if (p.getIsGoal()) {
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
        return tokenA.getOwner() == tokenB.getOwner() && tokenA.getPosition() == tokenB.getPosition();
    }

    public boolean canCapture(Token tokenA, Token tokenB) {
        return tokenA.getOwner() != tokenB.getOwner() && tokenA.getPosition() == tokenB.getPosition();
    }
}
