package com.eci.ARSW.GameService.GameService.model;

import com.eci.ARSW.GameService.GameService.model.*;
import java.util.*;

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

    private void initializeBoardFromStart() {
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
                        Mine mine = new Mine(pos, 'E');
                        board.setElementAt(i, j, mine);
                        mines.add(mine);
                    } else if ("F".equals(stateMine)) {
                        Mine mine = new Mine(pos, 'F');
                        board.setElementAt(i, j, mine);
                        mines.add(mine);
                    } else {
                        Mine mine = new Mine(pos, 'D');
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

    public Player createPlayer(Position position, int mines) {

        if (!(board.getElementAt(position.getX(), position.getY()) instanceof Tile)) {
            throw new IllegalArgumentException("Invalid position: already occupied by a Mine or another Player.");
        }

        String playerId = "P" + playerIdCounter;
        Player player = new Player(position, mines);
        player.setId(playerId);
        previousElement = board.getElementAt(position.getX(), position.getY());
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

    public void placeMine(String playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null || !player.isState() || player.getMode() != 'T') return;

        Mine mine = new Mine(new Position(x, y), 'E');
        mines.add(mine);
        board.setElementAt(x, y, mine);

        updateTileNumbers();
        updateGameStateFromBoard(this.status);
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
                    if (mine.getState() == 'E') {
                        matrix[i][j] = "ME";
                    } else if (mine.getState() == 'D') {
                        matrix[i][j] = "MD";
                    } else if (mine.getState() == 'F') {
                        matrix[i][j] = "MF";
                    }
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
