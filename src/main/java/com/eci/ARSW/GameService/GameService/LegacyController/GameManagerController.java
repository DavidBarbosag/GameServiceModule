
package com.eci.ARSW.GameService.GameService.LegacyController;

import com.eci.ARSW.GameService.GameService.model.*;
import com.eci.ARSW.GameService.GameService.service.GameManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GameManagerController handles HTTP requests for managing the game.
 * It provides endpoints to initialize the game, get game state, add players,
 * move players, and place mines.
 */
@RestController
@RequestMapping("/gameManager")
public class GameManagerController {

    @Autowired
    private GameManagerService gameService;

    /**
     * Initializes the game with the specified parameters.
     *
     * @param rows          Number of rows in the game grid.
     * @param cols          Number of columns in the game grid.
     * @param globalMines   Total number of mines in the game.
     * @param minesPerPlayer Number of mines each player can place.
     */
    @PostMapping("/init")
    public void initializeGame(@RequestParam int rows,
                               @RequestParam int cols,
                               @RequestParam int globalMines,
                               @RequestParam int minesPerPlayer) {
        gameService.initializeGame(rows, cols, globalMines, minesPerPlayer);
    }


    /**
     * Retrieves the current state of the game.
     *
     * @return The current game state.
     */
    @GetMapping("/state")
    public GameState getGameState() {
        return gameService.getGameState();
    }

    /**
     * Adds a player to the game at the specified position with a given number of mines.
     *
     * @param position The position where the player will be added.
     * @param mines    The number of mines the player can place.
     * @return The created Player object.
     */
    @PostMapping("/player")
    public Player createPlayer(@RequestBody Position position,
                               @RequestParam int mines) {
        return gameService.addPlayer(position, mines);
    }


    /**
     * Moves a player in the specified direction.
     *
     * @param playerId  The ID of the player to move.
     * @param direction The direction to move the player (e.g., 'W', 'A', 'S', 'D').
     * @return True if the move was successful, false otherwise.
     */
    @PostMapping("/move")
    public boolean movePlayer(@RequestParam String playerId,
                              @RequestParam char direction) {
        return gameService.movePlayer(playerId, direction);
    }

    /**
     * Places a mine at the specified coordinates for the given player.
     *
     * @param playerId The ID of the player placing the mine.
     * @param x        The x-coordinate where the mine is placed.
     * @param y        The y-coordinate where the mine is placed.
     */
    @PostMapping("/mine")
    public void placeMine(@RequestParam String playerId,
                          @RequestParam int x,
                          @RequestParam int y) {
        gameService.placeMine(playerId, x, y);
    }


    /**
     * Flags an element in the game.
     *
     * @param playerId  The ID of the player flagging the element.
     * @param direction The direction of the element to be flagged (e.g., 'u', 'd', 'l', 'r').
     */
    @PostMapping("/flag")
    public void flagElement(@RequestParam String playerId,
                            @RequestParam char direction) {
        gameService.flagElement(playerId, direction);
    }


}
