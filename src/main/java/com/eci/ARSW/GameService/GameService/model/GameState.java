package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "game_states")
public class GameState {

    @Id
    private String id;

    private String[][] boardMatrix;

    private String status; // IN_PROGRESS, FINISHED, PAUSED, etc.

    private Date createdAt;
    private Date updatedAt;

    private List<Player> players;
    private List<Mine> mines;

    public GameState() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public GameState(String[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
        this.status = "IN_PROGRESS";
        this.createdAt = new Date();
        this.updatedAt = new Date();
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Mine> getMines() {
        return mines;
    }

    public void setMines(List<Mine> mines) {
        this.mines = mines;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

