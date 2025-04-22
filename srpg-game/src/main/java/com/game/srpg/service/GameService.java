package com.game.srpg.service;

import com.game.srpg.model.GameState;
import com.game.srpg.model.Tile;
import com.game.srpg.model.Unit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {

    private Map<String, GameState> games = new HashMap<>();

    // 새 게임 생성
    public GameState createNewGame() {
        GameState gameState = new GameState(10, 10);
        games.put(gameState.getId(), gameState);
        return gameState;
    }

    // 게임 상태 조회
    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    // 유닛 이동
    public boolean moveUnit(String gameId, String unitId, int targetX, int targetY) {
        GameState gameState = games.get(gameId);
        if (gameState == null) return false;

        // 유닛 찾기
        Unit unit = findUnitById(gameState, unitId);
        if (unit == null) return false;

        // 이동 가능한 타일인지 확인
        Tile targetTile = gameState.getTile(targetX, targetY);
        if (targetTile == null || !targetTile.isWalkable()) return false;

        // 이동 범위 내인지 확인
        if (!isInMoveRange(gameState, unit, targetX, targetY)) return false;

        // 이전 타일에서 유닛 제거
        Tile currentTile = gameState.getTile(unit.getX(), unit.getY());
        currentTile.setUnit(null);

        // 새 타일로 유닛 이동
        unit.setX(targetX);
        unit.setY(targetY);
        targetTile.setUnit(unit);

        return true;
    }

    // 유닛 공격
    public boolean attackUnit(String gameId, String attackerUnitId, String targetUnitId) {
        GameState gameState = games.get(gameId);
        if (gameState == null) return false;

        // 공격자 유닛 찾기
        Unit attacker = findUnitById(gameState, attackerUnitId);
        if (attacker == null) return false;

        // 대상 유닛 찾기
        Unit target = findUnitById(gameState, targetUnitId);
        if (target == null) return false;

        // 공격 범위 내인지 확인
        if (!isInAttackRange(attacker, target)) return false;

        // 공격 처리
        target.takeDamage(attacker.getAttackPower());

        // 유닛이 죽었는지 확인
        if (!target.isAlive()) {
            // 타일에서 유닛 제거
            Tile targetTile = gameState.getTile(target.getX(), target.getY());
            targetTile.setUnit(null);

            // 유닛 목록에서 제거
            if (isPlayerUnit(gameState, target)) {
                gameState.getPlayerUnits().remove(target);
            } else {
                gameState.getEnemyUnits().remove(target);
            }
        }

        return true;
    }

    // 턴 종료
    public void endTurn(String gameId) {
        GameState gameState = games.get(gameId);
        if (gameState != null) {
            gameState.endTurn();

            // 적 턴인 경우 AI 행동 실행
            if (!gameState.isPlayerTurn()) {
                executeEnemyTurn(gameState);
            }
        }
    }

    // 적 AI 행동 실행
    private void executeEnemyTurn(GameState gameState) {
        // 각 적 유닛에 대해 행동 수행
        for (Unit enemyUnit : gameState.getEnemyUnits()) {
            // 가장 가까운 플레이어 유닛 찾기
            Unit closestPlayerUnit = findClosestPlayerUnit(gameState, enemyUnit);

            if (closestPlayerUnit != null) {
                // 공격 범위 내에 있으면 공격
                if (isInAttackRange(enemyUnit, closestPlayerUnit)) {
                    attackUnit(gameState.getId(), enemyUnit.getId(), closestPlayerUnit.getId());
                }
                // 공격 범위 밖이면 이동
                else {
                    moveEnemyUnitTowardsTarget(gameState, enemyUnit, closestPlayerUnit);
                }
            }
        }

        // 적 턴 종료 후 플레이어 턴으로
        gameState.endTurn();
    }

    // 가장 가까운 플레이어 유닛 찾기
    private Unit findClosestPlayerUnit(GameState gameState, Unit enemyUnit) {
        Unit closestUnit = null;
        int minDistance = Integer.MAX_VALUE;

        for (Unit playerUnit : gameState.getPlayerUnits()) {
            int distance = calculateManhattanDistance(enemyUnit, playerUnit);
            if (distance < minDistance) {
                minDistance = distance;
                closestUnit = playerUnit;
            }
        }

        return closestUnit;
    }

    // 맨해튼 거리 계산
    private int calculateManhattanDistance(Unit unit1, Unit unit2) {
        return Math.abs(unit1.getX() - unit2.getX()) + Math.abs(unit1.getY() - unit2.getY());
    }

    // 적 유닛을 대상 방향으로 이동
    private void moveEnemyUnitTowardsTarget(GameState gameState, Unit enemyUnit, Unit targetUnit) {
        // 이동 가능한 타일 중 대상에 가장 가까운 타일 찾기
        int bestX = enemyUnit.getX();
        int bestY = enemyUnit.getY();
        int bestDistance = calculateManhattanDistance(enemyUnit, targetUnit);

        // 상하좌우 이동 가능 여부 확인
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newX = enemyUnit.getX() + dir[0];
            int newY = enemyUnit.getY() + dir[1];

            // 맵 범위 내에 있고 이동 가능한 타일인지 확인
            if (newX >= 0 && newX < gameState.getWidth() &&
                newY >= 0 && newY < gameState.getHeight()) {

                Tile tile = gameState.getTile(newX, newY);
                if (tile.isWalkable()) {
                    // 대상까지의 거리 계산
                    Unit tempUnit = new Unit(enemyUnit.getName(), enemyUnit.getType(), newX, newY);
                    int distance = calculateManhattanDistance(tempUnit, targetUnit);

                    // 더 가까운 타일이면 업데이트
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestX = newX;
                        bestY = newY;
                    }
                }
            }
        }

        // 더 가까운 타일을 찾았으면 이동
        if (bestX != enemyUnit.getX() || bestY != enemyUnit.getY()) {
            moveUnit(gameState.getId(), enemyUnit.getId(), bestX, bestY);
        }
    }

    // ID로 유닛 찾기
    private Unit findUnitById(GameState gameState, String unitId) {
        // 플레이어 유닛 확인
        for (Unit unit : gameState.getPlayerUnits()) {
            if (unit.getId().equals(unitId)) {
                return unit;
            }
        }

        // 적 유닛 확인
        for (Unit unit : gameState.getEnemyUnits()) {
            if (unit.getId().equals(unitId)) {
                return unit;
            }
        }

        return null;
    }

    // 이동 범위 내인지 확인
    private boolean isInMoveRange(GameState gameState, Unit unit, int targetX, int targetY) {
        // 이동 범위 계산
        for (Tile tile : gameState.calculateMoveRange(unit)) {
            if (tile.getX() == targetX && tile.getY() == targetY) {
                return true;
            }
        }

        return false;
    }

    // 공격 범위 내인지 확인
    private boolean isInAttackRange(Unit attacker, Unit target) {
        int distance = calculateManhattanDistance(attacker, target);
        return distance <= attacker.getAttackRange();
    }

    // 플레이어 유닛인지 확인
    private boolean isPlayerUnit(GameState gameState, Unit unit) {
        return gameState.getPlayerUnits().contains(unit);
    }
}
