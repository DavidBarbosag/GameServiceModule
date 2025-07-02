package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mines")
public class Mine implements GameElement {
    private Position position;
    private char state; // E = Enabled, U = Used, D = Deactivated

    public Mine() {}

    public Mine(Position position, char state) {
        this.position = position;
        this.state = state;
    }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public char getState() { return state; }
    public void setState(char state) { this.state = state; }
}
