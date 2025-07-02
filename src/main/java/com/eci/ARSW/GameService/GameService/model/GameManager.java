package com.eci.ARSW.GameService.GameService.model;

import com.eci.ARSW.GameService.GameService.model.*;
import java.util.*;

public class GameManager {

    private Board board;
    private Map<String, Player> players; // ID -> Player
    private List<Mine> mines;
    private final int rows;
    private final int cols;
    private int playerIdCounter = 1;
    private int numMinesGlobal;
    private int numMinesPerPlayer;
    private GameState gameState;

    public GameManager(int rows, int cols, int numMinesGlobal, int numMinesPerPlayer) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Board(rows, cols);
        this.players = new HashMap<>();
        this.mines = new ArrayList<>();
        this.numMinesGlobal = numMinesGlobal;
        this.numMinesPerPlayer = numMinesPerPlayer;
        initializeBoard();
    }

    private void initializeBoard() {
        Random rand = new Random();
        int placed = 0;

        while (placed < numMinesGlobal) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);

            if (board.getElementAt(x, y) == null) {
                Mine mine = new Mine(new Position(x, y), 'E');
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

    public Player createPlayer(Position position, int mines) {

        if (!(board.getElementAt(position.getX(), position.getY()) instanceof Tile)) {
            throw new IllegalArgumentException("Invalid position: already occupied by a Mine or another Player.");
        }

        String playerId = "P" + playerIdCounter;
        Player player = new Player(position, mines);
        player.setId(playerId);
        board.setElementAt(position.getX(), position.getY(), player);
        players.put(playerId, player);
        playerIdCounter++;

        return player;
    }

    public boolean movePlayer(String playerId, char direction) {
        Player player = players.get(playerId);
        if (player == null || !player.isState()) return false;

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        int newX = x, newY = y;

        switch (direction) {
            case 'W' -> newY--; // up
            case 'S' -> newY++; // down
            case 'A' -> newX--; // left
            case 'D' -> newX++; // right
            default -> { return false; }
        }

        if (!board.isInBounds(newX, newY)) return false;

        GameElement target = board.getElementAt(newX, newY);
        if (target instanceof Mine mine && mine.getState() != 'E') {
            player.setState(false);
            mine.setState('D');
            board.setElementAt(x, y, new Tile(x, y));
            return true;
        }

        board.setElementAt(x, y, new Tile(x, y));
        player.setPosition(new Position(newX, newY));
        board.setElementAt(newX, newY, player);
        return true;
    }

    public void placeMine(String playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        Mine mine = new Mine(new Position(x, y), 'E');
        mines.add(mine);
        board.setElementAt(x, y, mine);

        updateTileNumbers();
    }

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

    private void updateGameStateFromBoard(String status) {
        String[][] matrix = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameElement elem = board.getElementAt(i, j);
                if (elem instanceof Tile tile) {
                    matrix[i][j] = String.valueOf(tile.getAdjacentMines());
                } else if (elem instanceof Player player) {
                    matrix[i][j] = player.getId(); // o "P" si prefieres ocultar info
                } else if (elem instanceof Mine mine) {
                    matrix[i][j] = mine.getState() == 'D' ? "X" : "M"; // revelada o no
                } else {
                    matrix[i][j] = "?";
                }
            }
        }

        if (gameState == null) {
            gameState = new GameState(matrix, status);
        } else {
            gameState.setBoardMatrix(matrix);
            gameState.setStatus(status);
            gameState.setUpdatedAt(java.time.LocalDateTime.now());
        }
    }


    public Board getBoard() {
        return board;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public List<Mine> getMines() {
        return mines;
    }

    public GameState getGameState() {
        updateGameStateFromBoard("IN_PROGRESS");
        return gameState;
    }
}
