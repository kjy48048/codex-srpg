package com.game.srpg.controller;

import com.game.srpg.model.GameState;
import com.game.srpg.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GameApiController REST API 테스트
 */
@WebMvcTest(GameApiController.class)
class GameApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void testCreateNewGame() throws Exception {
        GameState state = new GameState(3, 3);
        when(gameService.createNewGame()).thenReturn(state);

        mockMvc.perform(post("/api/game/new").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(state.getId()))
                .andExpect(jsonPath("$.message").value("새 게임이 생성되었습니다."));
        verify(gameService, times(1)).createNewGame();
    }

    @Test
    void testGetGameStateFound() throws Exception {
        GameState state = new GameState(4, 4);
        String id = state.getId();
        when(gameService.getGameState(id)).thenReturn(state);

        mockMvc.perform(get("/api/game/{gameId}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(gameService, times(1)).getGameState(id);
    }

    @Test
    void testGetGameStateNotFound() throws Exception {
        String id = "unknown";
        when(gameService.getGameState(id)).thenReturn(null);

        mockMvc.perform(get("/api/game/{gameId}", id))
                .andExpect(status().isNotFound());
        verify(gameService, times(1)).getGameState(id);
    }

    @Test
    void testMoveUnitSuccess() throws Exception {
        String id = "g1";
        when(gameService.moveUnit(id, "u1", 2, 3)).thenReturn(true);

        mockMvc.perform(post("/api/game/{gameId}/move", id)
                        .param("unitId", "u1")
                        .param("targetX", "2")
                        .param("targetY", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("유닛이 이동되었습니다."));
        verify(gameService, times(1)).moveUnit(id, "u1", 2, 3);
    }

    @Test
    void testAttackUnitSuccess() throws Exception {
        String id = "g1";
        when(gameService.attackUnit(id, "a1", "t1")).thenReturn(true);

        mockMvc.perform(post("/api/game/{gameId}/attack", id)
                        .param("attackerUnitId", "a1")
                        .param("targetUnitId", "t1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("공격이 성공했습니다."));
        verify(gameService, times(1)).attackUnit(id, "a1", "t1");
    }

    @Test
    void testEndTurn() throws Exception {
        String id = "g1";

        mockMvc.perform(post("/api/game/{gameId}/end-turn", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("턴이 종료되었습니다."));
        verify(gameService, times(1)).endTurn(id);
    }
}