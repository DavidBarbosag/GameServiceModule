package com.eci.ARSW.GameService.GameService.dto;

public class PlayerActionDTO {
    private String playerId;
    private char direction;
    private char mode;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public void setMode(char mode) { this.mode = mode; }

    public char getMode() { return mode; }

}

