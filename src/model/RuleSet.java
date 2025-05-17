package model;

import java.util.List;

public class RuleSet {
    int shapeType;

    public RuleSet(int shapeType) {
        this.shapeType = shapeType;
    }

    public boolean checkMove(Position currPos, Position nextPos, TossResult tossResult) {
        // 빽도가 나오면, 현재 노드의 이전 노드에 이동하고자 하는 노드가 있다면 true
        if (tossResult == TossResult.BACKDO) {
            return currPos.getPrevPositions().contains(nextPos);
        }

        // 이동 가능한 노드 파악
        List<Position> nextPositions = currPos.getNextPositions();
        if ((shapeType == 4 && currPos.getId() == 28) || (currPos.getId() != 7 * shapeType && currPos.getId() % 5 == 0)) {
            nextPositions = currPos.getNextPositions();
        }
        else if (currPos.getId() == 7 * shapeType) {
            nextPositions = List.of(nextPositions.get(1));
        }
        else {
            nextPositions = List.of(nextPositions.get(0));
        }

        // currPos -> newPos로 이동할 수 있는지 확인
        for (Position p : nextPositions) {
            Position q = p;
            for (int i = 1; i < tossResult.getValue(); i++) {
                if (q.isGoal()) {
                    return true;
                }
                q = q.getNextPositions().get(0);
            }
            if (q.getId() == nextPos.getId()) {
                return true;
            }
        }
        return false;
    }

    // 말을 업을 수 있는지 확인
    public boolean canStack(Token tokenA, Token tokenB) {
        // 1. 서로 다른 아이디
        // 2. 같은 소유자
        // 3. 같은 위치
        // 4. 시작/도착점이 아니어야 함
        return tokenA.getId() != tokenB.getId() && tokenA.getOwner() == tokenB.getOwner()
                && tokenA.getPosition().getId() == tokenB.getPosition().getId() && tokenB.getPosition().getId() != 0;
    }

    // 말을 잡을 수 있는지 확인
    public boolean canCapture(Token tokenA, Token tokenB, TossResult tossResult) {
        // 1. 서로 다른 소유자
        // 2. 같은 위치
        // 3. 시작/도착점이 아니어야 함
        boolean result = tokenA.getOwner() != tokenB.getOwner() && tokenA.getPosition().getId() == tokenB.getPosition().getId() && tokenB.getPosition().getId() != 0;

        // 윷 혹은 모로 잡지 않았다면 턴 추가
        if (result && tossResult.getValue() < 4) {
            tokenA.getOwner().addTurn(1);
        }
        return result;
    }
}