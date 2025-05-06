package model;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private int id;
    private boolean isStart = false;
    private boolean isGoal = false;
    private boolean isCenter = false;
    private List<Position> prevPositions;
    private List<Position> nextPositions;

    public Position(int id, int shapeType) {
        this.id = id;
        this.nextPositions = new ArrayList<Position>();
        this.prevPositions = new ArrayList<Position>();
        if (id == 0) {
            this.isStart = true;
            this.isGoal = true;
        }
        if (id == 7*shapeType) {
            this.isCenter = true;
        }
    }

    public void addPrevPosition(Position node) {
        if (node != null) {
            prevPositions.add(node);
        }
    }

    public void addNextPosition(Position node) {
        if (node != null) {
            nextPositions.add(node);
        }
    }

    public int getId() { return id; }

    public boolean isCenter() { return isCenter; }
    public boolean isStart() { return isStart; }
    public boolean isGoal() { return isGoal; }

    public List<Position> getPrevPositions() { return prevPositions; }
    public List<Position> getNextPositions() { return nextPositions; }
}
