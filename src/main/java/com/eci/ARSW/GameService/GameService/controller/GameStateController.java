package com.eci.ARSW.GameService.GameService.controller;

import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.service.GameStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GameStateController handles HTTP requests for managing game states.
 * It provides endpoints to create, retrieve, update, and delete game states.
 */
@RestController
@RequestMapping("/api/game")
public class GameStateController {

    @Autowired
    private GameStateService gameStateService;

    /**
     * Creates a new game state.
     *
     * @param gameState The game state to be created.
     * @return The created game state.
     */
    @PostMapping
    public ResponseEntity<GameState> createGameState(@RequestBody GameState gameState) {
        return ResponseEntity.ok(gameStateService.saveGameState(gameState));
    }

    /**
     * Retrieves a game state by its ID.
     *
     * @param id The ID of the game state to retrieve.
     * @return The game state if found, or a 404 Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameState> getGameStateById(@PathVariable String id) {
        return gameStateService.getGameStateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all game states.
     *
     * @return A list of all game states.
     */
    @GetMapping
    public List<GameState> getAllGameStates() {
        return gameStateService.getAllGameStates();
    }


    /**
     * Updates an existing game state.
     *
     * @param id        The ID of the game state to update.
     * @param gameState The updated game state.
     * @return The updated game state.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameState> updateGameState(@PathVariable String id, @RequestBody GameState gameState) {
        return ResponseEntity.ok(gameStateService.updateGameState(id, gameState));
    }

    /**
     * Deletes a game state by its ID.
     *
     * @param id The ID of the game state to delete.
     * @return A 204 No Content response if the deletion was successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameState(@PathVariable String id) {
        gameStateService.deleteGameState(id);
        return ResponseEntity.noContent().build();
    }
}
