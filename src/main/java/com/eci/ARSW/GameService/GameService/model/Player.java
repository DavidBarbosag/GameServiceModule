package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
public class Player implements GameElement {

    @Id
    private String id;

    private Position position;
    private int mines;
    private boolean state; // alive or not
    private char mode; // 'N' = normal, 'T' = tactical (can place bombs)

    public Player() {}

    public Player(Position position, int mines) {
        this.position = position;
        this.mines = mines;
        this.state = true; // Default state is alive
        this.mode = 'N'; // Default mode is normal
    }

    // ID
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Position
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    // Mines
    public int getMines() { return mines; }
    public void setMines(int mines) { this.mines = mines; }

    // State
    public boolean isState() { return state; }
    public void setState(boolean state) { this.state = state; }

    // Mode
    public char getMode() { return mode; }
    public void setMode(char mode) { this.mode = mode; }
}
