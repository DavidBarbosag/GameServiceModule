package com.eci.ARSW.GameService.GameService.model;

import java.util.Arrays;

public class GameTest {
    public static void main(String[] args) {
        GameManager manager = new GameManager(5, 5, 3, 2);

        // Crear un jugador
        Position pos = new Position(0, 0);
        Player player = manager.createPlayer(pos, 2);

        // Simular un movimiento
        manager.movePlayer(player.getId(), 'S');

        // Obtener el estado del juego
        GameState gameState = manager.getGameState();

        // Imprimir la matriz
        System.out.println("Estado del juego:");
        String[][] matrix = gameState.getBoardMatrix();
        for (String[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        // Mostrar estado textual
        System.out.println("Status: " + gameState.getStatus());
        System.out.println("Updated at: " + gameState.getUpdatedAt());
    }
}

