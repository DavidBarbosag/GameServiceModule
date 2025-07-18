package com.eci.ARSW.GameService.GameService.controller;

import com.eci.ARSW.GameService.GameService.dto.CreatePlayerDTO;
import com.eci.ARSW.GameService.GameService.dto.GameInitDTO;
import com.eci.ARSW.GameService.GameService.dto.MineDTO;
import com.eci.ARSW.GameService.GameService.dto.PlayerActionDTO;
import com.eci.ARSW.GameService.GameService.model.*;
import com.eci.ARSW.GameService.GameService.service.GameManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;


/**
 * GameWebSocketController handles WebSocket messages for game operations.
 */
@Controller
public class GameWebSocketController {

    @Autowired
    private GameManagerService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/init/{gameId}")
    public void initializeGame(@DestinationVariable String gameId, GameInitDTO dto) {
        System.out.println("[INIT] Recibida petición de inicialización para juego " + gameId);
        gameService.initializeGame(gameId, dto.getRows(), dto.getCols(), dto.getGlobalMines(), dto.getMinesPerPlayer());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameService.getGameState(gameId));
    }

    @MessageMapping("/createPlayer/{gameId}")
    public void createPlayer(@DestinationVariable String gameId, CreatePlayerDTO dto, Principal principal) {
        Player player = gameService.addPlayer(gameId, new Position(dto.getX(), dto.getY()), dto.getMines());
        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/playerCreated", player);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameService.getGameState(gameId));
    }

    @MessageMapping("/move/{gameId}")
    public void movePlayer(@DestinationVariable String gameId, PlayerActionDTO dto) {
        System.out.println("MOVE recibido en gameId=" + gameId + ": " + dto);
        gameService.movePlayer(gameId, dto.getPlayerId(), dto.getDirection());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameService.getGameState(gameId));
    }

    @MessageMapping("/flag/{gameId}")
    public void flagElement(@DestinationVariable String gameId, PlayerActionDTO dto) {
        gameService.flagElement(gameId, dto.getPlayerId(), dto.getDirection());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameService.getGameState(gameId));
    }
}
