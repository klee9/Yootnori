package test;

import java.util.List;

import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private Position position1, position2, position3;

    @BeforeEach
    void setUp() {
        // 시작/도착점 노드 하나(position1)와 일반 노드 2개(position2, 3) 생성
        position1 = new Position(0);
        position2 = new Position(1);
        position3 = new Position(2);
    }

    @Test
    void testIsStart() {
        assertTrue(position1.isStart());
        assertFalse(position2.isStart());
        assertFalse(position3.isStart());
    }

    @Test
    void testIsGoal() {
        assertTrue(position1.isGoal());
        assertFalse(position2.isGoal());
        assertFalse(position3.isGoal());
    }

    @Test
    void testAddPrevPosition() {
        // position1의 이전 노드 집합에 position2, 3 추가
        position1.addPrevPosition(position2);
        position1.addPrevPosition(position3);

        // position1의 이전 노드 집합의 크기와 원소 확인
        List<Position> prevPositions = position1.getPrevPositions();
        assertEquals(2, prevPositions.size());
        assertTrue(prevPositions.contains(position2));
        assertTrue(prevPositions.contains(position3));
    }

    @Test
    void testAddNextPosition() {
        // position1의 다음 노드 집합에 position2, 3 추가
        position1.addNextPosition(position2);
        position1.addNextPosition(position3);

        // position1의 다음 노드 집합의 크기와 원소 확인
        List<Position> nextPositions = position1.getNextPositions();
        assertEquals(2, nextPositions.size());
        assertTrue(nextPositions.contains(position2));
        assertTrue(nextPositions.contains(position3));
    }

    @Test
    void testGetId() {
        // 기본적인 getter 함수 테스트
        assertEquals(0, position1.getId());
        assertEquals(1, position2.getId());
        assertEquals(2, position3.getId());
    }

    @Test
    void testGetPrevPositions() {
        // position1의 이전 노드 집합에 position2, 3 추가
        position1.addPrevPosition(position2);
        position1.addPrevPosition(position3);
        List<Position> prevPositions = position1.getPrevPositions();

        // get을 통해 불러온 정보가 기대한 것과 일치하는지 확인
        assertEquals(2, prevPositions.size());
        assertEquals(position2, prevPositions.get(0));
        assertEquals(position3, prevPositions.get(1));
    }

    @Test
    void testGetNextPositions() {
        // position1의 다음 노드 집합에 position2, 3 추가
        position1.addNextPosition(position2);
        position1.addNextPosition(position3);
        List<Position> nextPositions = position1.getNextPositions();

        // get을 통해 불러온 정보가 기대한 것과 일치하는지 확인
        assertEquals(2, nextPositions.size());
        assertEquals(position2, nextPositions.get(0));
        assertEquals(position3, nextPositions.get(1));
    }
}