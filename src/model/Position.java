package model;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private final int id;
    private final boolean isStart;
    private final boolean isGoal;
    private final List<Position> prevPositions;
    private final List<Position> nextPositions;

    public Position(int id) {
        this.id = id;
        this.nextPositions = new ArrayList<>();
        this.prevPositions = new ArrayList<>();
        this.isStart = id == 0;
        this.isGoal = id == 0;
    }

    public void addPrevPosition(Position node) { prevPositions.add(node); }
    public void addNextPosition(Position node) { nextPositions.add(node); }

    public boolean isStart() { return isStart; }
    public boolean isGoal() { return isGoal; }

    public int getId() { return id; }
    public List<Position> getPrevPositions() { return prevPositions; }
    public List<Position> getNextPositions() { return nextPositions; }
}