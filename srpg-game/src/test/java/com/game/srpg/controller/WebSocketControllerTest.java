package com.game.srpg.controller;

import com.game.srpg.model.GameState;
import com.game.srpg.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * WebSocketController 단위 테스트
 */
class WebSocketControllerTest {
    private GameService gameService;
    private WebSocketController controller;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        controller = new WebSocketController(gameService);
    }

    @Test
    void testUpdateGame() {
        String gameId = "game1";
        GameState state = new GameState(5, 5);
        when(gameService.getGameState(gameId)).thenReturn(state);

        GameState result = controller.updateGame(gameId);
        assertSame(state, result, "updateGame should return the GameState from service");
        verify(gameService, times(1)).getGameState(gameId);
    }

    @Test
    void testMoveUnit() {
        String gameId = "g1";
        String unitId = "u1";
        int targetX = 2, targetY = 3;
        GameState state = new GameState(5, 5);
        Map<String, Object> payload = new HashMap<>();
        payload.put("gameId", gameId);
        payload.put("unitId", unitId);
        payload.put("targetX", targetX);
        payload.put("targetY", targetY);

        when(gameService.moveUnit(gameId, unitId, targetX, targetY)).thenReturn(true);
        when(gameService.getGameState(gameId)).thenReturn(state);

        Map<String, Object> response = controller.moveUnit(payload);
        assertEquals("MOVE_RESULT", response.get("type"));
        assertEquals(true, response.get("success"));
        assertSame(state, response.get("gameState"));
        verify(gameService, times(1)).moveUnit(gameId, unitId, targetX, targetY);
        verify(gameService, times(1)).getGameState(gameId);
    }

    @Test
    void testAttackUnit() {
        String gameId = "g1";
        String attackerId = "a1";
        String targetId = "t1";
        GameState state = new GameState(5, 5);
        Map<String, Object> payload = new HashMap<>();
        payload.put("gameId", gameId);
        payload.put("attackerUnitId", attackerId);
        payload.put("targetUnitId", targetId);

        when(gameService.attackUnit(gameId, attackerId, targetId)).thenReturn(false);
        when(gameService.getGameState(gameId)).thenReturn(state);

        Map<String, Object> response = controller.attackUnit(payload);
        assertEquals("ATTACK_RESULT", response.get("type"));
        assertEquals(false, response.get("success"));
        assertSame(state, response.get("gameState"));
        verify(gameService, times(1)).attackUnit(gameId, attackerId, targetId);
        verify(gameService, times(1)).getGameState(gameId);
    }

    @Test
    void testEndTurn() {
        String gameId = "g1";
        GameState state = new GameState(5, 5);
        when(gameService.getGameState(gameId)).thenReturn(state);

        Map<String, Object> response = controller.endTurn(gameId);
        assertEquals("END_TURN_RESULT", response.get("type"));
        assertEquals(true, response.get("success"));
        assertSame(state, response.get("gameState"));
        verify(gameService, times(1)).endTurn(gameId);
        verify(gameService, times(1)).getGameState(gameId);
    }
}