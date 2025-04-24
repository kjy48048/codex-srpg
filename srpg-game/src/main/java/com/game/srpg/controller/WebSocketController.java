package com.game.srpg.controller;

import com.game.srpg.model.GameState;
import com.game.srpg.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    private final GameService gameService;

    public WebSocketController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @MessageMapping("/game.update")
    @SendTo("/topic/game")
    public GameState updateGame(String gameId) {
        return gameService.getGameState(gameId);
    }
    
    @MessageMapping("/game.move")
    @SendTo("/topic/game")
    public Map<String, Object> moveUnit(Map<String, Object> payload) {
        String gameId = (String) payload.get("gameId");
        String unitId = (String) payload.get("unitId");
        int targetX = (int) payload.get("targetX");
        int targetY = (int) payload.get("targetY");
        
        boolean success = gameService.moveUnit(gameId, unitId, targetX, targetY);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "MOVE_RESULT");
        response.put("success", success);
        response.put("gameState", gameService.getGameState(gameId));
        
        return response;
    }
    
    @MessageMapping("/game.attack")
    @SendTo("/topic/game")
    public Map<String, Object> attackUnit(Map<String, Object> payload) {
        String gameId = (String) payload.get("gameId");
        String attackerUnitId = (String) payload.get("attackerUnitId");
        String targetUnitId = (String) payload.get("targetUnitId");
        
        boolean success = gameService.attackUnit(gameId, attackerUnitId, targetUnitId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "ATTACK_RESULT");
        response.put("success", success);
        response.put("gameState", gameService.getGameState(gameId));
        
        return response;
    }
    
    @MessageMapping("/game.endTurn")
    @SendTo("/topic/game")
    public Map<String, Object> endTurn(String gameId) {
        gameService.endTurn(gameId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "END_TURN_RESULT");
        response.put("success", true);
        response.put("gameState", gameService.getGameState(gameId));
        
        return response;
    }
}
