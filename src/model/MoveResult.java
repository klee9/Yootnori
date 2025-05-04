package model;

public class MoveResult {
    private Token token;
    private boolean captured;
    private boolean extra_turn;
    private boolean goal;

    public boolean isCaptured() {
        return captured;
    }

    public boolean hasExtraTurn() {
        return extra_turn;
    }

    public boolean isGoal() {
        return goal;
    }
}