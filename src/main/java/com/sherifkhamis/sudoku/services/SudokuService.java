package com.sherifkhamis.sudoku.services;

import java.util.Random;

public class SudokuService {
    private static final int GridSize = 9;

    // Define the Cell class as a static nested class
    static class Cell {
        int value;
        boolean isGiven;
        boolean hasError;

        public Cell() {
            this.value = 0; // 0 represents an empty cell
            this.isGiven = false;
            this.hasError = false;
        }
    }

    private static Cell[][] grid; // Changed from int[][]

    public enum Difficulty { EASY, MEDIUM, HARD }

    public static void generateSudoku(Difficulty difficulty) {
        grid = new Cell[GridSize][GridSize]; // Initialize the main grid array
        for (int i = 0; i < GridSize; i++) {
            for (int j = 0; j < GridSize; j++) {
                grid[i][j] = new Cell(); // Initialize each cell with a new Cell object
            }
        }
        solveSudoku();
        generateEmptyCells(difficulty);
    }

    private static boolean solveSudoku() {
        for (int row = 0; row < GridSize; row++) {
            for (int col = 0; col < GridSize; col++) {
                if (grid[row][col].value == 0) { // Access .value for empty cell check
                    for (int number = 1; number <= GridSize; number++) {
                        if (isValidPlacement(number, row, col)) {
                            grid[row][col].value = number; // Set .value

                            if (solveSudoku()) { // Recurse
                                return true; // Solution found
                            } else {
                                grid[row][col].value = 0; // Backtrack, reset .value
                            }
                        }
                    }
                    return false; // No valid number found for this cell
                }
            }
        }
        return true; // All cells filled
    }

    private static boolean isValidPlacement(int number, int row, int col) {
        // Check row
        for (int i = 0; i < GridSize; i++) {
            if (grid[row][i].value == number) { // Access .value
                return false;
            }
        }

        // Check column
        for (int i = 0; i < GridSize; i++) {
            if (grid[i][col].value == number) { // Access .value
                return false;
            }
        }

        // Check 3x3 subgrid
        int subgridRowStart = row - row % 3;
        int subgridColStart = col - col % 3;

        for (int i = subgridRowStart; i < subgridRowStart + 3; i++) {
            for (int j = subgridColStart; j < subgridColStart + 3; j++) {
                if (grid[i][j].value == number) { // Access .value
                    return false;
                }
            }
        }

        for (int i = 0; i < GridSize; i++) {
            for (int j = 0; j < GridSize; j++) {
                if (grid[i][j].isGiven) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void generateEmptyCells(Difficulty difficulty) {
        int cellsToBeEmptied;
        switch (difficulty) {
            case EASY: {
                cellsToBeEmptied = 30;
            }
            case MEDIUM: {
                cellsToBeEmptied = 40;
            }
            case HARD: {
                cellsToBeEmptied = 50;
            }
            default: {
                cellsToBeEmptied = 40;
            }
        }
        while (cellsToBeEmptied > 0) {
            for (int row = 0; row < GridSize; row++) {
                for (int col = 0; col < GridSize; col++) {
                    Random random = new Random();
                    if (random.nextInt(1,10) % 3 == 0 && grid[row][col].value != 0) {
                        grid[row][col].value = 0;
                        cellsToBeEmptied--;
                    }

                }
            }
        }
        for (int row = 0; row < GridSize; row++) {
            for (int col = 0; col < GridSize; col++) {
                if (grid[row][col].value != 0) {
                    grid[row][col].isGiven = true;
                }
            }
        }

    }
}
