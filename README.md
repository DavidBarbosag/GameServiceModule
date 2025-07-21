# Game Service

## Author : David Alfonso Barbosa Gómez

This is the Game Service module for the **Buscaminas Xtreme** multiplayer game, developed using a microservice architecture with Spring Boot.

## Description

The Game Service is responsible for managing the core logic of the game. It maintains the game board, player positions, mine placement, and evaluates the current state of the match. This service ensures proper movement, mine interactions, and tile updates in a grid-based environment.

## Responsibilities

- Generate and initialize the game board.
- Place and track global and player-specific mines.
- Flag/Unflag mines or hidden tiles.
- Move players based on input direction.
- Detect collisions with mines.
- Calculate adjacent mines for each tile.
- Maintain game state and provide game status updates.

## Project Structure

├── src/main/java/com/eci/ARSW/GameService/

│ ├── controller/ # WebSocket controllers

│ ├── dto/ # Auxiliar class for websockets controller

│ ├── model/ # Domain classes (Player, Tile, Mine, Board, etc.)

│ ├── LegacyController/ # Rest controllers

│ ├── Repository/ # For future data storage 

│ ├── service/ # Game logic services

│ ├── GameServiceApplication.java

├── resources/

│ └── application.properties

└── README.md

##  REST API Endpoints

| Endpoint                     | Method | Description                                       |
|-----------------------------|--------|---------------------------------------------------|
| `/gameManager/init`         | POST   | Initialize a new game with board and mine setup  |
| `/gameManager/state`        | GET    | Get current game state by game ID                |
| `/gameManager/player`       | POST   | Add a new player to a game                       |
| `/gameManager/move`         | POST   | Move a player in a given direction               |
| `/gameManager/mine`         | POST   | Place a mine adjacent to a player's position     |
| `/gameManager/flag`         | POST   | Flag or unflag a tile in a given direction       |
| `/api/game`                 | POST   | Save a game state (persistent)                   |
| `/api/game`                 | GET    | Get all saved game states                        |
| `/api/game/{id}`            | GET    | Get a specific game state by ID                  |
| `/api/game/{id}`            | PUT    | Update a game state by ID                        |
| `/api/game/{id}`            | DELETE | Delete a game state by ID                        |
| `/players`                  | POST   | Save a player object (persistent)                |
| `/players`                  | GET    | Get all saved players                            |
| `/players/{id}`             | GET    | Get a player by ID                               |
| `/players/{id}`             | DELETE | Delete a player by ID                            |

## WebSocket Endpoints

| Endpoint                            | Payload Type        | Description                                          |
|-------------------------------------|---------------------|------------------------------------------------------|
| `/app/init/{gameId}`               | `GameInitDTO`       | Initializes the game with specified parameters       |
| `/app/createPlayer/{gameId}`       | `CreatePlayerDTO`   | Creates a new player in the specified game           |
| `/app/move/{gameId}`               | `PlayerActionDTO`   | Moves the player in the given direction              |
| `/app/flag/{gameId}`               | `PlayerActionDTO`   | Flags or unflags a tile in a given direction         |
| `/app/placeMine/{gameId}`          | `PlayerActionDTO`   | Places a mine adjacent to the player's position      |
| `/app/changeMode/{gameId}`         | `PlayerActionDTO`   | Changes the player's current interaction mode        |

### Topics for Subscriptions

| Topic                              | Description                                      |
|-----------------------------------|--------------------------------------------------|
| `/topic/game/{gameId}`            | Broadcasts the updated game state               |
| `/topic/game/{gameId}/playerCreated` | Notifies clients when a new player is created |


# GameState Matrix Explanation

The `GameState` class contains a `boardMatrix` that represents the current state of the game board. This matrix is sent to the frontend to render the game interface.

## Matrix Structure

- **Type**: 2D String array (`String[][]`)
- **Rows**: Represent horizontal lines on the game board
- **Columns**: Represent vertical positions within each row
- **Cells**: Contain symbols representing game elements

## Symbol Meanings

### Player Symbols (`P#`)

Format: "P" + playerNumber (e.g., "P1", "P2")
- Represents a player's position
- Example: "P1" = Player 1 is at this position
- The number increments with each new player

### Mine Symbols (`Mx`)
Format: "M" + stateCharacter

**States**:
- `"ME"`: Enabled mine (active and dangerous)
- `"MF"`: Flagged mine (marked by a player)
- `"MD"`: Deactivated mine (exploded/defused)

**Examples**:
- `"ME"`: Active mine that can kill players
- `"MF"`: Mine flagged by a player
- `"MD"`: Deactivated mine

### Tile Symbols (`T#xY`)

Format: "T" + adjacentMines + flagStatus + revealStatus

**Components**:
- Number (0-8): Adjacent mines count
- Flag status: 
  - `"F"`: Flagged 
  - `"N"`: Not flagged
- Reveal status:
  - `"R"`: Revealed
  - `"H"`: Hidden

**Examples**:
- `"T0NR"`: Tile with 0 adjacent mines, not flagged, revealed
- `"T2FR"`: Tile with 2 adjacent mines, flagged, revealed
- `"T1NH"`: Tile with 1 adjacent mine, not flagged, hidden

## Special Cases
- `"?"`: Unknown/invalid element (shouldn't normally appear)
- Game status values:
  - `"IN_PROGRESS"`
  - `"FINISHED"`
  - `"PAUSED"`

## Matrix Update Process
The matrix is updated via `updateGameStateFromBoard()` which:

1. Creates new matrix with current board dimensions
2. Iterates through each board position
3. Identifies element type (Player/Mine/Tile)
4. Assigns appropriate symbol to matrix cell
5. Updates game status and timestamp

This provides all necessary data for the frontend to render:
- Player positions
- Mine states and flags
- Tile information (adjacent mines, flags, visibility)

## Key Functions

### Board Setup

```java
private void initializeBoardFromStart()
```

1. Randomly places global mines on the board.
2. Places double the mines as regular tiles (for balance).
3. Fills remaining spaces with tiles.
4. Calls updateTileNumbers() to calculate adjacent mines.

### Player Management

```java
public Player createPlayer(Position position, int mines)
```

1. Generates unique player ID ("P1", "P2", etc.).
2. Finds valid starting position using findFreePosition().
3. Stores previous element at that position (for movement).
4. Adds player to board and players collection.

```java
public synchronized boolean movePlayer(String playerId, char direction)
```

1. Handles player movement in 4 directions (WASD).
2. Checks for collisions with other players or mines.
3. Updates positions and maintains previous element.
4. Triggers game over check if player hits mine.

### Mine Operations

```java
public synchronized void placeMine(String playerId, char dir)
```

1. Places mine in specified direction (u/d/l/r).
2. Only works in tactical mode ('T').
3. Adds mine to mines collection.

```java
public synchronized boolean flagElement(String playerId, char dir)
```

1. Toggles flag state on mines/tiles in specified direction
2. Updates player score when flagging mines
3. Prevents flagging when game is finished
   


## Class Diagram
![GameServiceCD](https://github.com/user-attachments/assets/59b8b49f-52da-496c-a87e-01bd83d86eaf)


