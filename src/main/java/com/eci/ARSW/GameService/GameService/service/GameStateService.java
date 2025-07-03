package com.eci.ARSW.GameService.GameService.service;


import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameStateService {

    @Autowired
    private GameStateRepository gameStateRepository;


    public GameState saveGameState(GameState gameState) {
        return gameStateRepository.save(gameState);
    }

    public Optional<GameState> getGameStateById(String id) {
        return gameStateRepository.findById(id);
    }

    public List<GameState> getAllGameStates() {
        return gameStateRepository.findAll();
    }

    public void deleteGameState(String id) {
        gameStateRepository.deleteById(id);
    }

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

