package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int shape_type;
    private List<Position> positions;

    private final int SQUARE = 4;
    private final int PENTAGON = 5;
    private final int HEXAGON = 6;

    public Board(int _shape_type) {
        this.shape_type = _shape_type;
        this.positions = new ArrayList<>();

        // initialize board
        getBoardNodes(shape_type);
    }

    public Position getStartPosition() {
        return positions.get(0); // assuming the first element is the starting position
    }

    public Position getGoalPosition() {
        return positions.get(positions.size() - 1); // assuming the last element is the goal
    }

    public void getBoardNodes(int shape) {
        // add positions
        for (int i = 0; i < 7*shape + 1; i++) {
            positions.add(new Position(i));
        }

        // add connections (prev, next positions)
        int[][] connections = null;
        if (shape == SQUARE) {
            connections = new int[][] {
                    {0,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,22}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,24}, {11,12}, {12,13}, {13,14},
                    {14,15}, {16,17}, {17,18}, {18,19}, {19,0}, {20,0}, {21,20}, {22,23}, {23,28}, {24,25}, {25,28}, {26,15}, {27,26}, {28,21}
            };
        }

        else if (shape == PENTAGON) {
            connections = new int[][] {{1,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,22}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,24}, {11,12}, {12,13}, {13,14},
                    {14,15}, {16,17}, {17,18}, {18,19}, {19,0}, {20,0}, {21,20}, {22,23}, {23,28}, {24,25}, {25,28}, {26,15}, {27,26}, {28,21}
            }; // TODO: 칸 순서 인덱스 통일
        }

        else if (shape == HEXAGON) {
            connections = new int[][] {
                    {2,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,22}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,24}, {11,12}, {12,13}, {13,14},
                    {14,15}, {16,17}, {17,18}, {18,19}, {19,0}, {20,0}, {21,20}, {22,23}, {23,28}, {24,25}, {25,28}, {26,15}, {27,26}, {28,21}
            }; // TODO: 칸 순서 인덱스 통일
        }

        assert(connections != null);
        for (int[] connection : connections) {
            Position a = positions.get(connection[0]);
            Position b = positions.get(connection[1]);
            a.addNextPosition(b);
            b.addPrevPosition(a);
        }
    }
}
