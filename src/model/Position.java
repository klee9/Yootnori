package model;

import java.util.List;

public class Position {
    private int id;
    private boolean isStart = false;
    private boolean isGoal = false;
    private List<Position> prev_positions;
    private List<Position> next_positions;

    public Position(int _id) {
        this.id = _id;
        if (id == 0) {
            this.isStart = true;
            this.isGoal = true;
        }
    }

    public void addPrevPosition(Position node) {
        if (node != null) {
            prev_positions.add(node);
        }
    }

    public void addNextPosition(Position node) {
        if (node != null) {
            next_positions.add(node);
        }
    }
}