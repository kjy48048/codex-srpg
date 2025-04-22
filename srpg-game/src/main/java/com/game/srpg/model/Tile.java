package com.game.srpg.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {
    private String id;
    private int x;
    private int y;
    private boolean walkable;
    private int movementCost;
    private Unit occupiedUnit;

    public Tile(int x, int y) {
        this.id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.walkable = true;
        this.movementCost = 1;
        this.occupiedUnit = null;
    }

    // Lombok @Getter/@Setter로 기본 접근자 생성
    public boolean isWalkable() {
        return walkable && occupiedUnit == null;
    }

    /**
     * 현재 타일에 배치된 유닛을 반환합니다.
     * @return 배치된 Unit 객체, 없으면 null
     */
    public Unit getUnit() {
        return occupiedUnit;
    }

    /**
     * 현재 타일에 유닛을 배치하거나 제거합니다.
     * @param unit 배치할 유닛 객체, null일 경우 제거
     */
    public void setUnit(Unit unit) {
        this.occupiedUnit = unit;
    }

    // todo:
    public boolean isOccupied() { return true; }

    // todo:
    public void setOccupied(boolean test) { }

    // todo:
    public void setDescription(String description) { }
}
