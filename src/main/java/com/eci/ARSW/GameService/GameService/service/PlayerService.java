package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.Player;
import com.eci.ARSW.GameService.GameService.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PlayerService provides methods to manage players in the game.
 * It interacts with the PlayerRepository to perform CRUD operations.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Saves a new player to the repository.
     *
     * @param player The player to be saved.
     * @return The saved player.
     */
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param id The ID of the player to retrieve.
     * @return An Optional containing the Player if found, or empty if not found.
     */
    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    /**
     * Retrieves all players from the repository.
     *
     * @return A list of all players.
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Deletes a player by their ID.
     *
     * @param id The ID of the player to delete.
     */
    public void deletePlayer(String id) {
        playerRepository.deleteById(id);
    }
}