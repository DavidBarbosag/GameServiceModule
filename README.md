# Game Service

## Author : David Alfonso Barbosa Gómez

This is the Game Service module for the **Buscaminas Xtreme** multiplayer game, developed using a microservice architecture with Spring Boot.

## Description

The Game Service is responsible for managing the core logic of the game. It maintains the game board, player positions, mine placement, and evaluates the current state of the match. This service ensures proper movement, mine interactions, and tile updates in a grid-based environment.

## Responsibilities

- Generate and initialize the game board.
- Place and track global and player-specific mines.
- Move players based on input direction.
- Detect collisions with mines.
- Calculate adjacent mines for each tile.
- Maintain game state and provide game status updates.

## Project Structure

├── src/main/java/com/eci/ARSW/GameService/

│ ├── controller/ # API REST controllers

│ ├── model/ # Domain classes (Player, Tile, Mine, Board, etc.)

│ ├── service/ # Game logic services

│ ├── GameServiceApplication.java

├── resources/

│ └── application.properties

└── README.md

## REST API

| Endpoint        | Method | Description                      |
| --------------- | ------ | -------------------------------- |
| /game/state     | GET    | Get current game state           |
| /game/player    | POST   | Create a new player              |
| /game/move/{id} | POST   | Move a player in given direction |
| /game/mine/{id} | POST   | Place a mine by a player         |

## Class Diagram
![GameServiceCD](https://github.com/user-attachments/assets/59b8b49f-52da-496c-a87e-01bd83d86eaf)


