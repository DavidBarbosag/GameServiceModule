package com.eci.ARSW.GameService.GameService.model;

import com.eci.ARSW.GameService.GameService.model.*;
import java.util.*;


/**
 * GameManager class that manages the game state, players, mines, and the game board.
 * It provides methods to create players, move them, place mines, flag mines, and update the game state.
 */
public class GameManager {

    private Board board;
    private Map<String, Player> players; // ID -> Player
    private List<Mine> mines;
    private int rows;
    private int cols;
    private int playerIdCounter = 1;
    private int numMinesGlobal;
    private int numMinesPerPlayer;
    private GameState gameState;
    private String status; // IN_PROGRESS, FINISHED, PAUSED, etc.
    private GameElement previousElement;

    public GameManager() {
    }

    public GameManager(int rows, int cols, int numMinesGlobal, int numMinesPerPlayer) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Board(rows, cols);
        this.players = new HashMap<>();
        this.mines = new ArrayList<>();
        this.numMinesGlobal = numMinesGlobal;
        this.numMinesPerPlayer = numMinesPerPlayer;
        initializeBoardFromStart();
        this.status = "IN_PROGRESS";
        this.gameState = new GameState(new String[rows][cols]);
    }

    /**
     * Initializes the game board with mines and tiles.
     * Randomly places mines on the board and fills the rest with tiles.
     */
    private void initializeBoardFromStart() {
        Random rand = new Random();
        int placed = 0;

        while (placed < numMinesGlobal) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);

            if (board.getElementAt(x, y) == null) {
                Mine mine = new Mine(new Position(x, y));
                board.setElementAt(x, y, mine);
                mines.add(mine);
                placed++;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board.getElementAt(i, j) == null) {
                    board.setElementAt(i, j, new Tile(i, j));
                }
            }
        }
        updateTileNumbers();
    }


    /**
     * Loads the game state from a given GameState object.
     * This method populates the board, players, and mines based on the provided game state.
     *
     * @param gameState The GameState object containing the current game state.
     */
    public void loadFromMatrix(GameState gameState) {
        this.gameState = gameState;
        String[][] matrix = gameState.getBoardMatrix();
        this.board = new Board(rows, cols);
        this.players = new HashMap<>();
        this.mines = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cell = matrix[i][j];
                Position pos = new Position(i, j);
                if (cell != null && cell.startsWith("M")) {
                    String stateMine = (cell.substring(1)).toString();
                    if ("E".equals(stateMine)) {
                        Mine mine = new Mine(pos);
                        mine.setState('E');
                        board.setElementAt(i, j, mine);
                        mines.add(mine);
                    } else if ("F".equals(stateMine)) {
                        Mine mine = new Mine(pos);
                        mine.setState('F');
                        board.setElementAt(i, j, mine);
                        mines.add(mine);
                    } else {
                        Mine mine = new Mine(pos);
                        mine.setState('D');
                        board.setElementAt(i, j, mine);
                        mines.add(mine);
                    }
                } else if (cell != null && cell.startsWith("P")) {
                    Player player = new Player(pos, numMinesPerPlayer);
                    player.setId(cell);
                    board.setElementAt(i, j, player);
                    players.put(cell, player);
                    try {
                        int num = Integer.parseInt(cell.substring(1));
                        if (num >= playerIdCounter) playerIdCounter = num + 1;
                    } catch (NumberFormatException ignored) {}
                } else {
                    board.setElementAt(i, j, new Tile(i, j));
                }
            }
        }
        updateTileNumbers();
        this.status = "IN_PROGRESS";
        updateGameStateFromBoard(this.status);
    }

    /**
     * Creates a new player at the specified position with the given number of mines.
     * The player is assigned a unique ID and added to the game board.
     *
     * @param position The position where the player will be created.
     * @param mines    The number of mines the player can place.
     * @return The created Player object.
     */
    public Player createPlayer(Position position, int mines) {

        if (!(board.getElementAt(position.getX(), position.getY()) instanceof Tile)) {
            throw new IllegalArgumentException("Invalid position: already occupied by a Mine or another Player.");
        }

        String playerId = "P" + playerIdCounter;
        Player player = new Player(position, mines);
        player.setSymbol(playerId);
        previousElement = board.getElementAt(position.getX(), position.getY());
        board.setElementAt(position.getX(), position.getY(), player);
        players.put(playerId, player);
        playerIdCounter++;

        return player;
    }

    /**
     * Moves a player in the specified direction if the move is valid.
     * The player can move up (W), down (S), left (A), or right (D).
     *
     * @param playerId  The ID of the player to move.
     * @param direction The direction to move the player.
     * @return true if the move was successful, false otherwise.
     */
    public boolean movePlayer(String playerId, char direction) {
        Player player = players.get(playerId);
        if (player == null || !player.isState()) return false;

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        int newX = x, newY = y;

        switch (direction) {
            case 'W' -> newX--; // up
            case 'S' -> newX++; // down
            case 'A' -> newY--; // left
            case 'D' -> newY++; // right
            default -> { return false; }
        }

        if (!board.isInBounds(newX, newY)) return false;

        GameElement target = board.getElementAt(newX, newY);

        if(target instanceof Player){
            return false;
        }

        if (target instanceof Mine mine && mine.getState() == 'E') {
            player.setState(false);
            mine.setState('D');
            board.setElementAt(x, y, new Tile(x, y));
            updateGameStateFromBoard(this.status);
            return true;
        }

        player.setPosition(new Position(newX, newY));
        board.setElementAt(newX, newY, player);
        board.setElementAt(x, y, previousElement);
        previousElement = target;
        return true;
    }


    /**
     * Places a mine at the specified position for the given player.
     * @param playerId The ID of the player placing the mine.
     * @param x
     * @param y
     */
    public void placeMine(String playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        Mine mine = new Mine(new Position(x, y));
        mines.add(mine);
        board.setElementAt(x, y, mine);

        updateTileNumbers();
        updateGameStateFromBoard(this.status);
    }

    /**
     * Flags a mine at the specified position for the given player, if is not a mine flagged the tile.
     * @param playerId The ID of the player flagging the mine.
     * @param x row index
     * @param y column index
     */
    public void flagElement(String playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        GameElement elem = board.getElementAt(x, y);
        if (elem instanceof Mine mine) {
            if (mine.getState() == 'E') {
                mine.setState('F'); // Flag the mine
            } else if (mine.getState() == 'F') {
                mine.setState('D'); // Deactivate the mine
            }
            updateGameStateFromBoard(this.status);
        } else if (elem instanceof Tile tile) {
            if(!tile.isFlagged()){
                tile.setFlagged(true);
            }
        }
    }

    /**
     * Unflags a mine at the specified position for the given player, if is a tile flagged the tile.
     * @param playerId The ID of the player unflagging the mine.
     * @param x row index
     * @param y column index
     */
    public void unflagElement(String playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        GameElement elem = board.getElementAt(x, y);
        if (elem instanceof Mine mine) {
            if (mine.getState() == 'F') {
                mine.setState('E');
            }
            updateGameStateFromBoard(this.status);
        } else if (elem instanceof Tile tile) {
            if(tile.isFlagged()){
                tile.setFlagged(false);
            }
        }
    }


    /**
     * Update the numbers on the tiles based on the number of adjacent mines.
     */
    private void updateTileNumbers() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameElement elem = board.getElementAt(i, j);
                if (elem instanceof Tile tile) {
                    tile.setAdjacentMines(countAdjacentMines(i, j));
                }
            }
        }
    }


    /**
     * Counts the number of adjacent mines around a given position (x, y).
     * @param x row index
     * @param y column index
     * @return the count of adjacent mines
     */
    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;
                if (board.isInBounds(nx, ny)) {
                    GameElement elem = board.getElementAt(nx, ny);
                    if (elem instanceof Mine ) {
                        count++;
                    }
                }
            }
        }
        return count;
    }


    /**
     * Updates the game state based on the current board and status.
     * @param status
     */
    private void updateGameStateFromBoard(String status) {
        String[][] matrix = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameElement elem = board.getElementAt(i, j);
                if (elem instanceof Tile tile) {
                    matrix[i][j] = tile.getSymbol();
                } else if (elem instanceof Player player) {
                    matrix[i][j] = player.getSymbol();
                } else if (elem instanceof Mine mine) {
                    matrix[i][j] =  mine.getSymbol();
                } else {
                    matrix[i][j] = "?";
                }
            }
        }
        this.gameState.setBoardMatrix(matrix);
        this.gameState.setStatus(status);
        this.gameState.setUpdatedAt(new java.util.Date());
        this.gameState.setPlayers(new ArrayList<>(players.values()));
        this.gameState.setMines(new ArrayList<>(mines));

    }

    /**
     * Returns the game board.
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the map of players in the game.
     * @return a map where keys are player IDs and values are Player objects
     */
    public Map<String, Player> getPlayers() {
        return players;
    }

    /**
     * Returns the list of mines in the game.
     * @return a list of Mine objects
     */
    public List<Mine> getMines() {
        return mines;
    }

    /**
     * Returns the gameState.
     * @return gameState object containing the current state of the game
     */
    public GameState getGameState() {
        updateGameStateFromBoard("IN_PROGRESS");
        return gameState;
    }
}
