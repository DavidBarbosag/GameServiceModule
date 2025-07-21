package com.eci.ARSW.GameService.GameService.LegacyController;

import com.eci.ARSW.GameService.GameService.model.*;
import com.eci.ARSW.GameService.GameService.service.GameManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gameManager")
public class GameManagerController {

    @Autowired
    private GameManagerService gameService;

    @PostMapping("/init")
    public void initializeGame(@RequestParam String gameId,
                               @RequestParam int rows,
                               @RequestParam int cols,
                               @RequestParam int globalMines,
                               @RequestParam int minesPerPlayer) {
        gameService.initializeGame(gameId, rows, cols, globalMines, minesPerPlayer);
    }

    @GetMapping("/state")
    public GameState getGameState(@RequestParam String gameId) {
        return gameService.getGameState(gameId);
    }

    @PostMapping("/player")
    public Player createPlayer(@RequestParam String gameId,
                               @RequestBody Position position,
                               @RequestParam int mines) {
        return gameService.addPlayer(gameId, position, mines);
    }

    @PostMapping("/move")
    public boolean movePlayer(@RequestParam String gameId,
                              @RequestParam String playerId,
                              @RequestParam char direction) {
        return gameService.movePlayer(gameId, playerId, direction);
    }

    @PostMapping("/mine")
    public void placeMine(@RequestParam String gameId,
                          @RequestParam String playerId,
                          @RequestParam char dir) {
        gameService.placeMine(gameId, playerId, dir);
    }

    @PostMapping("/flag")
    public void flagElement(@RequestParam String gameId,
                            @RequestParam String playerId,
                            @RequestParam char direction) {
        gameService.flagElement(gameId, playerId, direction);
    }
}
