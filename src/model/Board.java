package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int shapeType;
    private List<Position> positions;

    public Board(int shapeType) {
        this.shapeType = shapeType;
        this.positions = new ArrayList<>();

        // initialize board
        getBoardNodes(shapeType);
    }

    // for debugging purposes
    public List<Position> getPositions() {
        return positions;
    }


    public Position getStartPosition() {
        return positions.get(0); // assuming the first element is the starting position
    }

    public Position getGoalPosition() {
        return positions.get(7 * shapeType); // assuming the last element is the goal
    }

    public void getBoardNodes(int shape) {
        // add positions
        for (int i = 0; i < 7*shape + 1; i++) {
            positions.add(new Position(i, shape));
        }

        // add connections (prev, next positions)
        int[][] connections = null;
        int square = 4, pentagon = 5, hexagon = 6;
        if (shape == square) {
            connections = new int[][] {
                    {0,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,22}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,24}, {11,12}, {12,13}, {13,14}, {14,15},
                    {15,16}, {16,17}, {17,18}, {18,19}, {19,0}, {20,0}, {21,20}, {22,23}, {23,28}, {24,25}, {25,28}, {26,15}, {27,26}, {28,21}, {28, 27}
            };
        }

        else if (shape == pentagon) {
            connections = new int[][] {
                    {0,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,27}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,29}, {11,12}, {12,13},
                    {13,14}, {14,15}, {15,16}, {15,31}, {16,17}, {17,18}, {18,19}, {19,20}, {20,21}, {20,33}, {21,22}, {22,23}, {23,24},
                    {24,0}, {25,0}, {26,25}, {27,28}, {28,35}, {29,30}, {30,35}, {31,32}, {32,35}, {33,20}, {34,33}, {35,26}, {35,34}
            };
        }

        else if (shape == hexagon) {
            connections = new int[][] {
                    {0,1}, {1,2}, {2,3}, {3,4}, {4,5}, {5,6}, {5,32}, {6,7}, {7,8}, {8,9}, {9,10}, {10,11}, {10,34}, {11,12}, {12,13}, {13,14},
                    {14,15}, {15,16}, {15,36}, {16,17}, {17,18}, {18,19}, {19,20}, {20,21}, {20,38}, {21,22}, {22,23}, {23,24}, {24,25}, {25,26},
                    {26,27}, {27,28}, {28,29}, {29,0}, {30,0}, {31,30}, {32,33}, {33,42}, {34,35}, {35,42}, {36,37}, {37,42}, {38,39}, {39,42},
                    {40,25}, {41,40}, {42,31}, {42,41}
            };
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
