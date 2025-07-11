package com.eci.ARSW.GameService.GameService.service;


import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * GameStateService provides methods to manage game states.
 * It interacts with the GameStateRepository to perform CRUD operations.
 */
@Service
public class GameStateService {

    @Autowired
    private GameStateRepository gameStateRepository;


    /**
     * Saves a new game state to the repository.
     *
     * @param gameState The game state to be saved.
     * @return The saved game state.
     */
    public GameState saveGameState(GameState gameState) {
        return gameStateRepository.save(gameState);
    }

    /**
     * Retrieves a game state by its ID.
     *
     * @param id The ID of the game state to retrieve.
     * @return An Optional containing the game state if found, or empty if not found.
     */
    public Optional<GameState> getGameStateById(String id) {
        return gameStateRepository.findById(id);
    }

    /**
     * Retrieves all game states from the repository.
     *
     * @return A list of all game states.
     */
    public List<GameState> getAllGameStates() {
        return gameStateRepository.findAll();
    }

    /**
     * Deletes a game state by its ID.
     *
     * @param id The ID of the game state to delete.
     */
    public void deleteGameState(String id) {
        gameStateRepository.deleteById(id);
    }

    /**
     * Updates an existing game state with the provided updatedGameState.
     *
     * @param id                The ID of the game state to update.
     * @param updatedGameState The updated game state object.
     * @return The updated game state.
     */
    public GameState updateGameState(String id, GameState updatedGameState) {
        return gameStateRepository.findById(id)
                .map(existing -> {
                    existing.setBoardMatrix(updatedGameState.getBoardMatrix());
                    existing.setStatus(updatedGameState.getStatus());
                    existing.setUpdatedAt(updatedGameState.getUpdatedAt());
                    return gameStateRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("GameState not found"));
    }
}

