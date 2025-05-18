package model;

import java.util.List;

public class RuleSet {
    int shapeType;
    public RuleSet(String shapeType) {
        switch (shapeType) {
            case "사각형" -> this.shapeType = 4;
            case "오각형" -> this.shapeType = 5;
            case "육각형" -> this.shapeType = 6;
            default -> this.shapeType = 1;
        }
    }

    public boolean checkMove(Position currPos, Position nextPos, TossResult tossResult) {
        if (tossResult == TossResult.BACKDO) {
            return currPos.getPrevPositions().contains(nextPos);
        }
        else {
            List<Position> nextPositions = currPos.getNextPositions();
            if ((shapeType == 4 && currPos.getId() == 28) || (currPos.getId()%5 == 0)) {
                nextPositions = currPos.getNextPositions();
            }
            else if (currPos.getId() == 7*shapeType) {
                nextPositions = List.of(nextPositions.get(1));
            }
            else {
                nextPositions = List.of(nextPositions.get(0));
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
        return tokenA.getId() != tokenB.getId() && tokenA.getOwner() == tokenB.getOwner() && tokenA.getPosition().getId() == tokenB.getPosition().getId() && tokenB.getPosition().getId() != 0;
    }

    public boolean canCapture(Token tokenA, Token tokenB, TossResult tossResult) {
        boolean result = tokenA.getOwner() != tokenB.getOwner() && tokenA.getPosition().getId() == tokenB.getPosition().getId() && tokenB.getPosition().getId() != 0;
        if (result && tossResult.getValue() < 4) {
            tokenA.getOwner().addTurn(1);
        }
        return result;
    }
}