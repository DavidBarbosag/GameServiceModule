package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "game_states")
public class GameState {

    @Id
    private String id;

    private String[][] boardMatrix;

    private String status; // IN_PROGRESS, FINISHED, PAUSED, etc.

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public GameState() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public GameState(String[][] boardMatrix, String status) {
        this.boardMatrix = boardMatrix;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String[][] getBoardMatrix() {
        return boardMatrix;
    }

    public void setBoardMatrix(String[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

