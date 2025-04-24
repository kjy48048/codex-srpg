package com.game.srpg.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GameState의 이동 범위 계산 로직 검증 테스트
 */
class GameStateCalculateRangeTest {
    // 헬퍼: 리스트에 특정 좌표 포함 여부
    private boolean contains(List<Tile> list, int x, int y) {
        return list.stream().anyMatch(t -> t.getX() == x && t.getY() == y);
    }

    @Test
    void testCalculateMoveRangeSimple() {
        GameState state = new GameState(3, 3);
        Unit unit = new Unit("U", UnitType.WARRIOR, 1, 1);
        unit.setMoveRange(1);
        // 유닛 배치
        state.getTile(1, 1).setUnit(unit);

        List<Tile> tiles = state.calculateMoveRange(unit);
        // 상하좌우 4칸만 이동 가능
        assertEquals(4, tiles.size());
        assertTrue(contains(tiles, 0, 1));
        assertTrue(contains(tiles, 2, 1));
        assertTrue(contains(tiles, 1, 0));
        assertTrue(contains(tiles, 1, 2));
    }

    @Test
    void testCalculateMoveRangeObstacle() {
        GameState state = new GameState(3, 3);
        Unit unit = new Unit("U", UnitType.WARRIOR, 1, 1);
        unit.setMoveRange(1);
        state.getTile(1, 1).setUnit(unit);
        // 하나의 타일 이동 불가 처리
        Tile obstacle = state.getTile(2, 1);
        obstacle.setWalkable(false);

        List<Tile> tiles = state.calculateMoveRange(unit);
        assertEquals(3, tiles.size());
        assertFalse(contains(tiles, 2, 1));
    }

    @Test
    void testCalculateMoveRangeOccupancy() {
        GameState state = new GameState(3, 3);
        Unit unit = new Unit("U", UnitType.WARRIOR, 1, 1);
        unit.setMoveRange(1);
        state.getTile(1, 1).setUnit(unit);
        // 인접 타일에 다른 유닛 배치
        Unit other = new Unit("O", UnitType.WARRIOR, 0, 1);
        state.getTile(0, 1).setUnit(other);

        List<Tile> tiles = state.calculateMoveRange(unit);
        assertEquals(3, tiles.size());
        assertFalse(contains(tiles, 0, 1));
    }

    @Test
    void testCalculateMoveRangeRadiusTwo() {
        GameState state = new GameState(5, 5);
        Unit unit = new Unit("U", UnitType.WARRIOR, 2, 2);
        unit.setMoveRange(2);
        state.getTile(2, 2).setUnit(unit);

        List<Tile> tiles = state.calculateMoveRange(unit);
        // 맨해튼 거리 1~2 범위: 4*(1+2) = 12
        assertEquals(12, tiles.size());
    }
}