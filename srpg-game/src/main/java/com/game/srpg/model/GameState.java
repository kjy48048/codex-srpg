package com.game.srpg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameState {
    private String id;
    private int width;
    private int height;
    private Map<String, Tile> tiles;
    private List<Unit> playerUnits;
    private List<Unit> enemyUnits;
    private boolean playerTurn;
    
    public GameState(int width, int height) {
        this.id = UUID.randomUUID().toString();
        this.width = width;
        this.height = height;
        this.tiles = new HashMap<>();
        this.playerUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.playerTurn = true;
        
        initializeMap();
        initializeUnits();
    }
    
    private void initializeMap() {
        // 맵 초기화
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = new Tile(x, y);
                tiles.put(x + "," + y, tile);
            }
        }
    }
    
    private void initializeUnits() {
        // 플레이어 유닛 초기화
        Unit warrior = new Unit("전사", UnitType.WARRIOR, 1, 1);
        warrior.setMaxHp(100);
        warrior.setCurrentHp(100);
        warrior.setAttackPower(20);
        warrior.setMoveRange(3);
        warrior.setAttackRange(1);
        playerUnits.add(warrior);
        
        Unit archer = new Unit("궁수", UnitType.ARCHER, 2, 2);
        archer.setMaxHp(70);
        archer.setCurrentHp(70);
        archer.setAttackPower(15);
        archer.setMoveRange(3);
        archer.setAttackRange(3);
        playerUnits.add(archer);
        
        Unit mage = new Unit("마법사", UnitType.MAGE, 3, 3);
        mage.setMaxHp(60);
        mage.setCurrentHp(60);
        mage.setAttackPower(25);
        mage.setMoveRange(2);
        mage.setAttackRange(2);
        playerUnits.add(mage);
        
        // 타일에 유닛 배치
        getTile(1, 1).setUnit(warrior);
        getTile(2, 2).setUnit(archer);
        getTile(3, 3).setUnit(mage);
        
        // 적 유닛 초기화
        Unit enemyWarrior = new Unit("적 전사", UnitType.WARRIOR, 8, 8);
        enemyWarrior.setMaxHp(100);
        enemyWarrior.setCurrentHp(100);
        enemyWarrior.setAttackPower(20);
        enemyWarrior.setMoveRange(3);
        enemyWarrior.setAttackRange(1);
        enemyUnits.add(enemyWarrior);
        
        Unit enemyArcher = new Unit("적 궁수", UnitType.ARCHER, 7, 7);
        enemyArcher.setMaxHp(70);
        enemyArcher.setCurrentHp(70);
        enemyArcher.setAttackPower(15);
        enemyArcher.setMoveRange(3);
        enemyArcher.setAttackRange(3);
        enemyUnits.add(enemyArcher);
        
        // 타일에 적 유닛 배치
        getTile(8, 8).setUnit(enemyWarrior);
        getTile(7, 7).setUnit(enemyArcher);
    }
    
    public Tile getTile(int x, int y) {
        return tiles.get(x + "," + y);
    }
    
    public void endTurn() {
        playerTurn = !playerTurn;
    }
    
    public List<Tile> calculateMoveRange(Unit unit) {
        List<Tile> tilesInRange = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();
        List<MoveNode> queue = new ArrayList<>();
        
        queue.add(new MoveNode(unit.getX(), unit.getY(), 0));
        visited.put(unit.getX() + "," + unit.getY(), true);
        
        while (!queue.isEmpty()) {
            MoveNode current = queue.remove(0);
            
            if (current.distance <= unit.getMoveRange()) {
                // 시작 위치가 아니면 이동 범위에 추가
                if (current.x != unit.getX() || current.y != unit.getY()) {
                    tilesInRange.add(getTile(current.x, current.y));
                }
                
                // 상하좌우 타일 확인
                int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                
                for (int[] dir : directions) {
                    int newX = current.x + dir[0];
                    int newY = current.y + dir[1];
                    
                    // 맵 범위 내에 있는지 확인
                    if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                        String key = newX + "," + newY;
                        Tile tile = getTile(newX, newY);
                        
                        // 방문하지 않았고, 이동 가능하며, 유닛이 없는 타일인지 확인
                        if (!visited.containsKey(key) && tile.isWalkable() && tile.getUnit() == null) {
                            visited.put(key, true);
                            queue.add(new MoveNode(newX, newY, current.distance + tile.getMovementCost()));
                        }
                    }
                }
            }
        }
        
        return tilesInRange;
    }
    
    // 이동 노드 내부 클래스
    private static class MoveNode {
        int x;
        int y;
        int distance;
        
        MoveNode(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }
    
    // Getter와 Setter
    public String getId() {
        return id;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Map<String, Tile> getTiles() {
        return tiles;
    }
    
    public List<Unit> getPlayerUnits() {
        return playerUnits;
    }
    
    public List<Unit> getEnemyUnits() {
        return enemyUnits;
    }
    
    public boolean isPlayerTurn() {
        return playerTurn;
    }
}
