package com.eci.ARSW.GameService.GameService.LegacyController;

import com.eci.ARSW.GameService.GameService.model.Player;
import com.eci.ARSW.GameService.GameService.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * PlayerController handles HTTP requests for managing players in the game.
 * It provides endpoints to create, retrieve, and delete players.
 */
@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * Creates a new player.
     *
     * @param player The player object to be created.
     * @return The created Player object.
     */
    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param id The ID of the player to retrieve.
     * @return An Optional containing the Player if found, or empty if not found.
     */
    @GetMapping("/{id}")
    public Optional<Player> getPlayer(@PathVariable String id) {
        return playerService.getPlayerById(id);
    }

    /**
     * Retrieves all players.
     *
     * @return A list of all players.
     */
    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    /**
     * Deletes a player by their ID.
     *
     * @param id The ID of the player to delete.
     */
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable String id) {
        playerService.deletePlayer(id);
    }
}