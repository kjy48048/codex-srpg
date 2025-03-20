package com.game.srpg.controller;

import com.game.srpg.model.GameState;
import com.game.srpg.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameApiController {

    @Autowired
    private GameService gameService;
    
    // 새 게임 생성
    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> createNewGame() {
        GameState gameState = gameService.createNewGame();
        
        Map<String, Object> response = new HashMap<>();
        response.put("gameId", gameState.getId());
        response.put("message", "새 게임이 생성되었습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    // 게임 상태 조회
    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGameState(@PathVariable String gameId) {
        GameState gameState = gameService.getGameState(gameId);
        
        if (gameState == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(gameState);
    }
    
    // 유닛 이동
    @PostMapping("/{gameId}/move")
    public ResponseEntity<Map<String, Object>> moveUnit(
            @PathVariable String gameId,
            @RequestParam String unitId,
            @RequestParam int targetX,
            @RequestParam int targetY) {
        
        boolean success = gameService.moveUnit(gameId, unitId, targetX, targetY);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("message", "유닛이 이동되었습니다.");
            response.put("success", true);
        } else {
            response.put("message", "유닛 이동에 실패했습니다.");
            response.put("success", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 유닛 공격
    @PostMapping("/{gameId}/attack")
    public ResponseEntity<Map<String, Object>> attackUnit(
            @PathVariable String gameId,
            @RequestParam String attackerUnitId,
            @RequestParam String targetUnitId) {
        
        boolean success = gameService.attackUnit(gameId, attackerUnitId, targetUnitId);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("message", "공격이 성공했습니다.");
            response.put("success", true);
        } else {
            response.put("message", "공격에 실패했습니다.");
            response.put("success", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 턴 종료
    @PostMapping("/{gameId}/end-turn")
    public ResponseEntity<Map<String, Object>> endTurn(@PathVariable String gameId) {
        gameService.endTurn(gameId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "턴이 종료되었습니다.");
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
}
