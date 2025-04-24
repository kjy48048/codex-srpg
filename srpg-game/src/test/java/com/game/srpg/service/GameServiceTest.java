package com.game.srpg.service;

import com.game.srpg.config.CardTemplateLoader;
import com.game.srpg.config.GameDataProperties;
import com.game.srpg.config.GameDataProperties.UnitDef;
import com.game.srpg.model.GameState;
import com.game.srpg.model.Unit;
import com.game.srpg.model.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameService 이동/공격/턴 처리 로직 테스트
 */
class GameServiceTest {
    private GameDataProperties props;
    private CardTemplateLoader templateLoader;
    private GameStateFactory factory;
    private GameService service;
    private String gameId;
    private GameState state;

    @BeforeEach
    void setUp() {
        // 게임 데이터 설정
        props = new GameDataProperties();
        props.setWidth(3);
        props.setHeight(3);
        // 플레이어 유닛 정의
        UnitDef p = new UnitDef();
        p.setName("P"); p.setType(UnitType.WARRIOR);
        p.setX(0); p.setY(0);
        p.setMaxHp(50); p.setAttackPower(10);
        p.setMoveRange(2); p.setAttackRange(1);
        props.setInitialUnits(Arrays.asList(p));
        // 적 유닛 정의
        UnitDef e = new UnitDef();
        e.setName("E"); e.setType(UnitType.WARRIOR);
        e.setX(1); e.setY(0);
        e.setMaxHp(50); e.setAttackPower(10);
        e.setMoveRange(2); e.setAttackRange(1);
        props.setInitialEnemyUnits(Arrays.asList(e));
        // 팩토리 및 서비스 초기화
        templateLoader = new CardTemplateLoader();
        factory = new GameStateFactory(props, templateLoader);
        service = new GameService(factory);
        // 새 게임 생성
        state = service.createNewGame();
        gameId = state.getId();
    }

    @Test
    void testMoveUnit() {
        Unit player = state.getPlayerUnits().get(0);
        int oldX = player.getX();
        int oldY = player.getY();
        int newX = 1, newY = 1;
        boolean moved = service.moveUnit(gameId, player.getId(), newX, newY);
        assertTrue(moved, "Unit should move within range");
        assertNull(state.getTile(oldX, oldY).getUnit(), "Old tile should be empty");
        assertEquals(player, state.getTile(newX, newY).getUnit(), "New tile should contain the unit");
    }

    @Test
    void testAttackUnit() {
        Unit player = state.getPlayerUnits().get(0);
        Unit enemy = state.getEnemyUnits().get(0);
        // 공격 범위 내 (distance = 1)
        assertTrue(Math.abs(player.getX() - enemy.getX()) + Math.abs(player.getY() - enemy.getY()) <= player.getAttackRange());
        int enemyHp = enemy.getCurrentHp();
        boolean attacked = service.attackUnit(gameId, player.getId(), enemy.getId());
        assertTrue(attacked, "Attack should succeed");
        assertEquals(enemyHp - player.getAttackPower(), enemy.getCurrentHp(), "Enemy HP should decrease");
    }

    @Test
    void testEndTurn_EnemyAttacks() {
        Unit player = state.getPlayerUnits().get(0);
        int playerHp = player.getCurrentHp();
        // 플레이어 턴 종료 -> 적 턴 실행
        service.endTurn(gameId);
        // 턴이 플레이어로 다시 전환됨
        assertTrue(state.isPlayerTurn(), "After endTurn, playerTurn should be true");
        // 적이 공격했으므로 플레이어 HP 감소
        assertEquals(playerHp - state.getEnemyUnits().get(0).getAttackPower(), player.getCurrentHp(), "Player HP should decrease due to enemy attack");
    }
}