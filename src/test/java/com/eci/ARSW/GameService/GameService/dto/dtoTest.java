package com.eci.ARSW.GameService.GameService.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class dtoTest {

    @Test
    public void testCreatePlayerDTO() {
        CreatePlayerDTO dto = new CreatePlayerDTO();
        int testX = 5;
        int testY = 10;
        int testMines = 3;

        dto.setX(testX);
        dto.setY(testY);
        dto.setMines(testMines);

        assertEquals(testX, dto.getX(), "X coordinate should match");
        assertEquals(testY, dto.getY(), "Y coordinate should match");
        assertEquals(testMines, dto.getMines(), "Mines count should match");
    }

    @Test
    public void testGameInitDTO() {
        GameInitDTO dto = new GameInitDTO();
        int testRows = 10;
        int testCols = 15;
        int testGlobalMines = 20;
        int testMinesPerPlayer = 5;

        dto.setRows(testRows);
        dto.setCols(testCols);
        dto.setGlobalMines(testGlobalMines);
        dto.setMinesPerPlayer(testMinesPerPlayer);

        assertEquals(testRows, dto.getRows(), "Rows should match");
        assertEquals(testCols, dto.getCols(), "Columns should match");
        assertEquals(testGlobalMines, dto.getGlobalMines(), "Global mines should match");
        assertEquals(testMinesPerPlayer, dto.getMinesPerPlayer(), "Mines per player should match");
    }

    @Test
    public void testMineDTO() {
        MineDTO dto = new MineDTO();
        String testPlayerId = "player123";
        int testX = 3;
        int testY = 7;

        dto.setPlayerId(testPlayerId);
        dto.setX(testX);
        dto.setY(testY);

        assertEquals(testPlayerId, dto.getPlayerId(), "Player ID should match");
        assertEquals(testX, dto.getX(), "X coordinate should match");
        assertEquals(testY, dto.getY(), "Y coordinate should match");
    }

    @Test
    public void testPlayerActionDTO() {
        PlayerActionDTO dto = new PlayerActionDTO();
        String testPlayerId = "player456";
        char testDirection = 'W';
        char testMode = 'T';

        dto.setPlayerId(testPlayerId);
        dto.setDirection(testDirection);
        dto.setMode(testMode);

        assertEquals(testPlayerId, dto.getPlayerId(), "Player ID should match");
        assertEquals(testDirection, dto.getDirection(), "Direction should match");
        assertEquals(testMode, dto.getMode(), "Mode should match");
    }

    @Test
    public void testPlayerActionDTOModeCaseSensitivity() {
        PlayerActionDTO dto = new PlayerActionDTO();
        char upperCaseMode = 'T';
        char lowerCaseMode = 't';

        dto.setMode(upperCaseMode);
        assertEquals(upperCaseMode, dto.getMode(), "Should handle uppercase mode");

        dto.setMode(lowerCaseMode);
        assertEquals(lowerCaseMode, dto.getMode(), "Should handle lowercase mode");
    }

    @Test
    public void testCreatePlayerDTOEdgeCases() {
        CreatePlayerDTO dto = new CreatePlayerDTO();
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        int zeroValue = 0;

        dto.setX(minValue);
        assertEquals(minValue, dto.getX(), "Should handle minimum integer value for X");

        dto.setY(maxValue);
        assertEquals(maxValue, dto.getY(), "Should handle maximum integer value for Y");

        dto.setMines(zeroValue);
        assertEquals(zeroValue, dto.getMines(), "Should handle zero value for mines");
    }

    @Test
    public void testGameInitDTOValidation() {
        GameInitDTO dto = new GameInitDTO();
        int validRows = 5;
        int validCols = 5;
        int validGlobalMines = 10;
        int validMinesPerPlayer = 2;

        dto.setRows(validRows);
        dto.setCols(validCols);
        dto.setGlobalMines(validGlobalMines);
        dto.setMinesPerPlayer(validMinesPerPlayer);

        assertTrue(dto.getRows() > 0, "Rows should be positive");
        assertTrue(dto.getCols() > 0, "Columns should be positive");
        assertTrue(dto.getGlobalMines() >= 0, "Global mines should be non-negative");
        assertTrue(dto.getMinesPerPlayer() >= 0, "Mines per player should be non-negative");
    }
}