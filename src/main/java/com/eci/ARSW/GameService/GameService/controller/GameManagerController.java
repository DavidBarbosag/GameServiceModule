package com.eci.ARSW.GameService.GameService.controller;

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
    public void initializeGame(@RequestParam int rows,
                               @RequestParam int cols,
                               @RequestParam int globalMines,
                               @RequestParam int minesPerPlayer) {
        gameService.initializeGame(rows, cols, globalMines, minesPerPlayer);
    }

    @GetMapping("/state")
    public GameState getGameState() {
        return gameService.getGameState();
    }

    @PostMapping("/player")
    public Player createPlayer(@RequestBody Position position,
                               @RequestParam int mines) {
        return gameService.addPlayer(position, mines);
    }

    @PostMapping("/move")
    public boolean movePlayer(@RequestParam String playerId,
                              @RequestParam char direction) {
        return gameService.movePlayer(playerId, direction);
    }

    @PostMapping("/mine")
    public void placeMine(@RequestParam String playerId,
                          @RequestParam int x,
                          @RequestParam int y) {
        gameService.placeMine(playerId, x, y);
    }
}
