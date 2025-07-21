package com.eci.ARSW.GameService.GameService.model;
import com.eci.ARSW.GameService.GameService.model.GameElement;
import com.eci.ARSW.GameService.GameService.model.Mine;
import com.eci.ARSW.GameService.GameService.model.Player;


/**
 * Represents the game board for the Minesweeper game,
 * containing a matrix of gameElements such as tiles, mines and players.
 */
public class Board {

    private int rows;
    private int cols;
    private GameElement[][] matrix;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new GameElement[rows][cols];
    }

    /**
     * Returns the GameElement at the specified position in the board.
     * @param row of the element in the board
     * @param col of the element in the board
     * @return the GameElement at the specified position, or null if out of bounds
     */
    public GameElement getElementAt(int row, int col) {
        if (isInBounds(row, col)) {
            return matrix[row][col];
        }
        return null;
    }

    /**
     * Sets the GameElement at the specified position in the board.
     * @param row of the element in the board
     * @param col of the element in the board
     * @param element to be set at the specified position
     */
    public void setElementAt(int row, int col, GameElement element) {
        if (isInBounds(row, col)) {
            matrix[row][col] = element;
        }
    }

    /**
     * Removes the GameElement at the specified position in the board.
     * @param row of the element in the board
     * @param col of the element in the board
     */
    public void removeElementAt(int row, int col) {
        if (isInBounds(row, col)) {
            matrix[row][col] = null;
        }
    }

    /**
     * Checks if the specified position in the board is empty.
     * @param row of the element in the board
     * @param col of the element in the board
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(int row, int col) {
        return isInBounds(row, col) && matrix[row][col] == null;
    }

    /**
     * Checks if the specified position in the board is within bounds.
     * @param row of the element in the board
     * @param col of the element in the board
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Returns the matrix representing the board.
     * @return the matrix of GameElements
     */
    public GameElement[][] getMatrix() {
        return matrix;
    }

    /**
     * Returns the number of rows in the board.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the board.
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }
}