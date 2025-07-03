package com.eci.ARSW.GameService.GameService.controller;

import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameStateController {

    @Autowired
    private GameStateService gameStateService;

    @PostMapping
    public ResponseEntity<GameState> createGameState(@RequestBody GameState gameState) {
        return ResponseEntity.ok(gameStateService.saveGameState(gameState));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameState> getGameStateById(@PathVariable String id) {
        return gameStateService.getGameStateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<GameState> getAllGameStates() {
        return gameStateService.getAllGameStates();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameState> updateGameState(@PathVariable String id, @RequestBody GameState gameState) {
        return ResponseEntity.ok(gameStateService.updateGameState(id, gameState));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameState(@PathVariable String id) {
        gameStateService.deleteGameState(id);
        return ResponseEntity.noContent().build();
    }
}
