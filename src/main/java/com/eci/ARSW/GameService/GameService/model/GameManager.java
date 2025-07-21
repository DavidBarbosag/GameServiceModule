package com.eci.ARSW.GameService.GameService.model;

import com.eci.ARSW.GameService.GameService.model.*;
import java.util.*;


/**
 * GameManager class that manages the game state, players, mines, and the game board.
 * It provides methods to create players, move them, place mines, flag mines, and update the game state.
 */
public class GameManager {

    private String id;
    private Board board;
    private Map<String, Player> players; // Symbol Of the Player -> Player
    private List<Mine> mines;
    private int rows;
    private int cols;
    private int playerIdCounter = 1;
    private int numMinesGlobal;
    private int numMinesPerPlayer;
    private GameState gameState;
    private String status = "IN_PROGRESS"; // IN_PROGRESS, FINISHED, PAUSED, etc.
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

        placed = 0;

        while (placed < 2 * numMinesGlobal) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);

            if (board.getElementAt(x, y) == null) {
                Tile tile = new Tile(x, y);
                tile.setRevealed(false);
                board.setElementAt(x, y, tile);
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
        this.rows = gameState.getBoardMatrix().length;
        this.cols = gameState.getBoardMatrix()[0].length;
        this.board = new Board(rows, cols);
        this.players = new HashMap<>();
        this.mines = new ArrayList<>();
        this.status = gameState.getStatus();
        this.gameState = gameState;

        String[][] matrix = gameState.getBoardMatrix();

        for (Mine mine : gameState.getMines()) {
            Position pos = mine.getPosition();
            Mine newMine = new Mine(pos);
            newMine.setState(mine.getState());
            board.setElementAt(pos.getX(), pos.getY(), newMine);
            mines.add(newMine);
        }

        for (Player savedPlayer : gameState.getPlayers()) {
            Player newPlayer = new Player(savedPlayer.getPosition(), savedPlayer.getMines());
            newPlayer.setSymbol(savedPlayer.getSymbol());
            newPlayer.setState(savedPlayer.isState());
            newPlayer.setMode(savedPlayer.getMode());

            board.setElementAt(newPlayer.getPosition().getX(), newPlayer.getPosition().getY(), newPlayer);
            players.put(newPlayer.getSymbol(), newPlayer);

            try {
                int num = Integer.parseInt(newPlayer.getSymbol().substring(1));
                if (num >= playerIdCounter) playerIdCounter = num + 1;
            } catch (NumberFormatException ignored) {}
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameElement current = board.getElementAt(i, j);
                String cell = matrix[i][j];
                if (current == null && cell != null && cell.startsWith("T")) {
                    Tile tile = new Tile(i, j);
                    tile.setFlagged(cell.endsWith("F"));
                    tile.setSymbol(cell);
                    board.setElementAt(i, j, tile);
                } else if (current == null) {
                    board.setElementAt(i, j, new Tile(i, j));
                }
            }
        }

        updateTileNumbers();
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

        Position freePosition = findFreePosition();

        if (!(board.getElementAt(freePosition.getX(), freePosition.getY()) instanceof Tile)) {
            throw new IllegalArgumentException("Invalid position: already occupied by a Mine or another Player.");
        }

        String playerId = "P" + playerIdCounter;
        Player player = new Player(freePosition, mines);
        player.setSymbol(playerId);
        player.setId(playerId);
        previousElement = board.getElementAt(freePosition.getX(), freePosition.getY());
        board.setElementAt(freePosition.getX(), freePosition.getY(), player);
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
    public synchronized boolean movePlayer(String playerId, char direction) {

        if ("FINISHED".equals(this.status)) return false;

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
            itsGameOver(playerId);
            return true;
        }

        player.setPosition(new Position(newX, newY));
        board.setElementAt(newX, newY, player);
        board.setElementAt(x, y, previousElement);
        previousElement = target;
        updateGameStateFromBoard(this.status);
        updateTileNumbers();
        itsGameOver(playerId);
        return true;
    }


    /**
     * Places a mine at the specified position for the given player.
     * @param playerId The ID of the player placing the mine.
     * @param dir The direction to place the mine ('u', 'd', 'l', 'r').
     */
    public synchronized void placeMine(String playerId,  char dir) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        int newX = x, newY = y;

        switch (dir) {
            case 'u' -> newX--; // up
            case 'd' -> newX++; // down
            case 'l' -> newY--; // left
            case 'r' -> newY++; // right
            default -> { return; }
        }

        GameElement elem = board.getElementAt(newX, newY);
        if(elem instanceof Tile) {
            Mine mine = new Mine(new Position(newX, newY));
            mines.add(mine);
            board.setElementAt(newX, newY, mine);
        }

        updateTileNumbers();
        updateGameStateFromBoard(this.status);
    }

    /**
     * Changes the mode of the player to either 'N' (normal) or 'T' (tactical).
     * @param playerId The ID of the player whose mode is to be changed.
     * @param mode The new mode for the player ('N' or 'T').
     */
    public void changePlayerMode(String playerId, char mode) {
        Player player = players.get(playerId);
        if (player == null || !player.isState()) return;

        if (mode == 'N' || mode == 'T') {
            player.setMode(mode);
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }

    /**
     * Flags a mine at the specified position for the given player, if is not a mine flagged the tile.
     * @param playerId The ID of the player flagging the mine.
     * @param dir The direction to flag the mine ('u', 'd', 'l', 'r').
     * @return true if the flagging was successful, false otherwise.
     */
    public synchronized boolean flagElement(String playerId, char dir) {

        if ("FINISHED".equals(this.status)) return false;

        Player player = players.get(playerId);
        if (player == null || !player.isState()) return false;

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        int newX = x, newY = y;

        switch (dir) {
            case 'u' -> newX--; // up
            case 'd' -> newX++; // down
            case 'l' -> newY--; // left
            case 'r' -> newY++; // right
            default -> { return false; }
        }


        GameElement elem = board.getElementAt(newX, newY);
        if (elem instanceof Mine mine) {
            if (mine.getState() == 'E') {
                mine.setState('F');
                mine.setAsociatedPlayerId(playerId);
                players.get(playerId).setScore(players.get(playerId).getScore() + 1);
            } else if (mine.getState() == 'F' && mine.getAsociatedPlayerId().equals(playerId)) {
                mine.setState('E');
                players.get(playerId).setScore(players.get(playerId).getScore() - 1);
            }
            updateGameStateFromBoard(this.status);
        } else if (elem instanceof Tile tile) {
            if(!tile.isFlagged()){
                tile.setFlagged(true);
            } else {
                tile.setFlagged(false);
            }
        }
        itsGameOver(playerId);
        return true;
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
     * Finds a free position on the board where a new player can be placed.
     * @return a Position object representing a free position on the board
     */
    private Position findFreePosition() {
        Random rand = new Random();
        Position pos;
        do {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);
            pos = new Position(x, y);
        } while (!(board.getElementAt(pos.getX(), pos.getY()) instanceof Tile));
        return pos;
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
     * Checks if the game is over by verifying if all mines are deactivated.
     * If all mines are deactivated, it updates the game status to "FINISHED".
     * @return true if the game is over, false otherwise
     */
    private synchronized void itsGameOver(String playerId) {
        if ("FINISHED".equals(this.status)) return;

        boolean allMinesFlagged = true;
        for(Mine m : mines) {
            if (m.getState() == 'E') {
                allMinesFlagged = false;
                break;
            }
        }

        boolean allPlayersDead = true;
        for(Player p : players.values()) {
            if (p.isState()) {
                allPlayersDead = false;
                break;
            }
        }

        if (allMinesFlagged || allPlayersDead) {
            this.status = "FINISHED";

            Player winner = null;
            int maxScore = -1;
            for (Player p : players.values()) {
                if (p.isState() && p.getScore() > maxScore) {
                    maxScore = p.getScore();
                    winner = p;
                }
            }

            if (winner == null) {
                winner = players.get(playerId);
            }

            this.gameState.setWinnerId(winner != null ? winner.getId() : null);
            updateGameStateFromBoard(this.status);
        }
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
        updateGameStateFromBoard(this.status);
        return gameState;
    }

    /**
     * Returns the id of the GameManager.
     * @return the id of the GameManager
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the GameManager.
     * @param id the new id of the GameManager
     */
    public void setId(String id) {
        this.id = id;
    }
}
