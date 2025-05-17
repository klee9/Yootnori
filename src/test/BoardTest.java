package test;

import model.Board;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BoardTest {
    private Board board1, board2, board3;

    @BeforeEach
    void setUp() {
        // 사각형, 오각형, 육각형 보드 생성
        board1 = new Board(4);
        board2 = new Board(5);
        board3 = new Board(6);
    }

    @Test
    void testGetPositions() {
        // 각 보드의 노드 정보 불러오기
        List<Position> squarePositions = board1.getPositions();
        List<Position> pentagonPositions = board2.getPositions();
        List<Position> hexagonPositions = board3.getPositions();

        // 각 보드의 노드 개수 확인 (7*(꼭짓점 개수) + 1)
        assertEquals(29, squarePositions.size());
        assertEquals(36, pentagonPositions.size());
        assertEquals(43, hexagonPositions.size());
    }

    @Test
    void testGetStartPosition() {
        // 각 보드의 시작 위치 불러오기
        Position squareStartPosition = board1.getStartPosition();
        Position pentagonStartPosition = board2.getStartPosition();
        Position hexagonStartPosition = board3.getStartPosition();

        // 각 보드의 시작 위치가 0인지 확인
        assertEquals(0, squareStartPosition.getId());
        assertEquals(0, pentagonStartPosition.getId());
        assertEquals(0, hexagonStartPosition.getId());
    }
}