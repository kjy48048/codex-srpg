package com.game.srpg.model;

import java.util.UUID;

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
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean isWalkable() {
        return walkable && occupiedUnit == null;
    }
    
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
    
    public int getMovementCost() {
        return movementCost;
    }
    
    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }
    
    public Unit getUnit() {
        return occupiedUnit;
    }
    
    public void setUnit(Unit unit) {
        this.occupiedUnit = unit;
    }
}
