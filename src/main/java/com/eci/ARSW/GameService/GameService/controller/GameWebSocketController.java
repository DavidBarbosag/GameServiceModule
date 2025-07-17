package com.eci.ARSW.GameService.GameService.controller;

import com.eci.ARSW.GameService.GameService.dto.CreatePlayerDTO;
import com.eci.ARSW.GameService.GameService.dto.GameInitDTO;
import com.eci.ARSW.GameService.GameService.dto.MineDTO;
import com.eci.ARSW.GameService.GameService.dto.PlayerActionDTO;
import com.eci.ARSW.GameService.GameService.model.*;
import com.eci.ARSW.GameService.GameService.service.GameManagerService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @MessageMapping("/init")
    public void initializeGame(GameInitDTO dto) {
        gameService.initializeGame(dto.getRows(), dto.getCols(), dto.getGlobalMines(), dto.getMinesPerPlayer());
        messagingTemplate.convertAndSend("/topic/state", gameService.getGameState());
    }

    @MessageMapping("/createPlayer")
    public void createPlayer(CreatePlayerDTO dto, Principal principal) {
        Player player = gameService.addPlayer(new Position(dto.getX(), dto.getY()), dto.getMines());
        messagingTemplate.convertAndSend("/topic/playerCreated", player);
        messagingTemplate.convertAndSend("/topic/state", gameService.getGameState());
    }

    @MessageMapping("/move")
    public void movePlayer(PlayerActionDTO dto) {
        System.out.println("MOVE recibido: " + dto);
        gameService.movePlayer(dto.getPlayerId(), dto.getDirection());
        messagingTemplate.convertAndSend("/topic/state", gameService.getGameState());
    }

    @MessageMapping("/flag")
    public void flagElement(PlayerActionDTO dto) {
        gameService.flagElement(dto.getPlayerId(), dto.getDirection());
        messagingTemplate.convertAndSend("/topic/state", gameService.getGameState());
    }
}
