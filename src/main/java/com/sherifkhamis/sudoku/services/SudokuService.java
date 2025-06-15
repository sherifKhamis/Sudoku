// This file contains the SudokuService class, which is responsible for generating and solving Sudoku puzzles.
// It provides functionality to create puzzles of varying difficulty levels.

// Defines the package where this class belongs.
// Packages in Java help organize code and prevent naming conflicts.
package com.sherifkhamis.sudoku.services;

// Imports the Random class from the java.util package.
// The Random class is used to generate random numbers, which is useful for creating Sudoku puzzles
// by randomly removing numbers from a fully solved grid.
import java.util.Random;

// Defines the SudokuService class.
// This is a public class, meaning it can be accessed from anywhere in the application.
// This class contains all the logic for generating and solving Sudoku puzzles.
public class SudokuService {
    // Defines a private, static, and final constant named GridSize with a value of 9.
    // 'private': This constant is only accessible within this class.
    // 'static': This constant belongs to the class itself, not to any instance of the class.
    // 'final': The value of this constant cannot be changed after initialization.
    // This constant represents the size of the Sudoku grid (9x9).
    private static final int GridSize = 9;

    // Defines a static nested class named Cell.
    // 'static': This inner class can exist without an instance of the outer class (SudokuService).
    // This class represents a single cell in the Sudoku grid.
    static class Cell {
        // An integer variable that stores the numeric value of the cell.
        // Valid values are 1 to 9, and 0 represents an empty cell.
        int value;

        // A boolean variable that indicates whether the cell's value is provided by the puzzle.
        // 'true': The value is given and cannot be changed by the player.
        // 'false': The value is not given and can be set by the player.
        boolean isGiven;

        // A boolean variable that indicates whether the cell contains an error.
        // 'true': The cell contains a value that violates Sudoku rules.
        // 'false': The cell does not contain a known error.
        // Note: This variable is not actively used in the current service logic
        // but could be useful for a user interface to highlight erroneous inputs.
        boolean hasError;

        // Constructor for the Cell class.
        // A constructor is a special method that is automatically called when a new object of this class is created.
        public Cell() {
            // Initializes the value of the cell to 0, meaning the cell is empty by default.
            this.value = 0; // 0 represents an empty cell

            // Initializes isGiven to false, meaning a new cell is not given by default.
            this.isGiven = false;

            // Initializes hasError to false, meaning a new cell has no error by default.
            this.hasError = false;
        }
    }

    // Declares a two-dimensional array of Cell objects named 'grid'.
    // 'private static': This array is only accessible within the SudokuService class
    // and belongs to the class itself, not to any instance.
    // This array will represent the 9x9 Sudoku grid, where each element is a Cell object.
    private static Cell[][] grid; // Changed from int[][]

    // Defines a public enumeration type (Enum) named Difficulty.
    // Enums are used to represent a fixed set of named constants.
    // Here, Difficulty represents the various difficulty levels of the Sudoku game.
    public enum Difficulty { EASY, MEDIUM, HARD }

    // Defines a public static method named generateSudoku.
    // 'public': This method can be called from anywhere in the application.
    // 'static': This method belongs to the SudokuService class and can be called without creating an instance.
    // 'void': This method does not return any value.
    // Parameter: 'difficulty' of type Difficulty, which specifies the desired difficulty level.
    // Purpose: Generates a new, playable Sudoku puzzle.
    public static void generateSudoku(Difficulty difficulty) {
        // Initializes the 'grid' as a new two-dimensional array of Cell objects with size GridSize x GridSize (9x9).
        grid = new Cell[GridSize][GridSize]; // Initialize the main grid array

        // Double for-loop to iterate through each cell in the grid and initialize it.
        for (int i = 0; i < GridSize; i++) { // Outer loop for rows (i from 0 to 8)
            for (int j = 0; j < GridSize; j++) { // Inner loop for columns (j from 0 to 8)
                // Creates a new Cell object for each position (i, j) in the grid.
                // The Cell constructor (public Cell()) is called here and sets value=0, isGiven=false, hasError=false.
                grid[i][j] = new Cell(); // Initialize each cell with a new Cell object
            }
        }

        // Calls the solveSudoku() method. This method fills the (initially empty) grid
        // with a complete and valid Sudoku solution.
        solveSudoku();

        // Calls the generateEmptyCells(difficulty) method. This method takes the fully solved grid
        // and removes a certain number of numbers to create the actual puzzle.
        // The number of removed numbers depends on the provided 'difficulty' parameter.
        generateEmptyCells(difficulty);
    }

    // Defines a private static method named solveSudoku.
    // 'private': This method can only be called from within the SudokuService class.
    // 'static': This method belongs to the class.
    // 'boolean': This method returns a boolean value: 'true' if a solution for the Sudoku was found, otherwise 'false'.
    // Purpose: Solves the Sudoku puzzle in the 'grid' using a backtracking algorithm.
    // Backtracking is a recursive technique that tries to build a solution incrementally.
    // If a step leads to a dead end (no valid number can be placed), it undoes the last step (backtracks)
    // and tries a different possibility.
    private static boolean solveSudoku() {
        // Iterates through each row of the grid.
        for (int row = 0; row < GridSize; row++) {
            // Iterates through each column of the grid.
            for (int col = 0; col < GridSize; col++) {
                // Checks if the current cell (at position row, col) is empty (i.e., its value is 0).
                if (grid[row][col].value == 0) { // Access .value for empty cell check
                    // If the cell is empty, tries each possible number from 1 to GridSize (i.e., 1 to 9).
                    for (int number = 1; number <= GridSize; number++) {
                        // Checks if placing the current 'number' in the cell (row, col) is valid
                        // according to Sudoku rules (unique in row, column, and 3x3 block).
                        if (isValidPlacement(number, row, col)) {
                            // If the placement is valid, sets the 'number' in the cell.
                            grid[row][col].value = number; // Set .value

                            // Recursive call to solveSudoku() to try to solve the rest of the grid
                            // based on the current number placement.
                            if (solveSudoku()) { // Recurse
                                // If the recursive call returns 'true', it means a complete solution was found.
                                return true; // Solution found
                            } else {
                                // If the recursive call returns 'false', it means placing
                                // the current 'number' at this position did not lead to a solution (dead end).
                                // Therefore, the decision must be undone (backtracking).
                                // The cell is reset to 0 (empty).
                                grid[row][col].value = 0; // Backtrack, reset .value
                            }
                        }
                    }
                    // If the inner loop (for 'number') has been completely executed and no number
                    // could be successfully placed in the current empty cell (row, col) that leads to a solution,
                    // then there is no solution from this grid state.
                    // Return 'false' to tell the previous recursion step that this path didn't work.
                    return false; // No valid number found for this cell
                }
            }
        }
        // If the outer loops (for 'row' and 'col') have been completely executed without finding an empty cell (value == 0),
        // it means all cells are filled. Since each number was validly placed, the Sudoku is solved.
        return true; // All cells filled
    }

    // Defines a private static method named isValidPlacement.
    // 'private static boolean': Only visible internally, belongs to the class, returns true/false.
    // Parameters:
    //   'number': The number (1-9) to be placed.
    //   'row': The row number (0-8) where the number is to be placed.
    //   'col': The column number (0-8) where the number is to be placed.
    // Purpose: Checks if placing the 'number' at the position ('row', 'col') complies with Sudoku rules.
    private static boolean isValidPlacement(int number, int row, int col) {
        // 1. Row check:
        // Iterates through all cells in the specified 'row'.
        for (int i = 0; i < GridSize; i++) {
            // If the 'number' is already present in another cell of this row ('grid[row][i]'),
            // the placement is invalid.
            if (grid[row][i].value == number) { // Access .value
                return false; // Number already present in the row.
            }
        }

        // 2. Column check:
        // Iterates through all cells in the specified 'col'.
        for (int i = 0; i < GridSize; i++) {
            // If the 'number' is already present in another cell of this column ('grid[i][col]'),
            // the placement is invalid.
            if (grid[i][col].value == number) { // Access .value
                return false; // Number already present in the column.
            }
        }

        // 3. 3x3 subgrid check:
        // Calculates the starting row of the 3x3 block to which the cell (row, col) belongs.
        // Example: If row = 0, 1, or 2, then row % 3 = 0, 1, or 2. row - row % 3 gives 0.
        //          If row = 3, 4, or 5, then row % 3 = 0, 1, or 2. row - row % 3 gives 3.
        //          If row = 6, 7, or 8, then row % 3 = 0, 1, or 2. row - row % 3 gives 6.
        int subgridRowStart = row - row % 3;
        // Calculates the starting column of the 3x3 block, similar to the starting row.
        int subgridColStart = col - col % 3;

        // Iterates through the 3 rows of the relevant 3x3 block.
        for (int i = subgridRowStart; i < subgridRowStart + 3; i++) {
            // Iterates through the 3 columns of the relevant 3x3 block.
            for (int j = subgridColStart; j < subgridColStart + 3; j++) {
                // If the 'number' is already present in another cell of this 3x3 block ('grid[i][j]'),
                // the placement is invalid.
                if (grid[i][j].value == number) { // Access .value
                    return false; // Number already present in the 3x3 block.
                }
            }
        }

        // If the 'number' has passed the checks for row, column, and 3x3 block
        // (i.e., it was not found anywhere), the placement is valid.
        return true; // Placement is valid.
    }

    // Defines a private static method named generateEmptyCells.
    // 'private static void': Only visible internally, belongs to the class, returns nothing.
    // Parameter: 'difficulty' of type Difficulty, which determines the difficulty level of the puzzle.
    // Purpose: Takes a fully solved Sudoku grid (created by solveSudoku)
    //          and removes a certain number of numbers to create a playable puzzle.
    //          The number of removed numbers depends on the 'difficulty' level.
    private static void generateEmptyCells(Difficulty difficulty) {
        // Variable to store the number of cells to be emptied.
        int cellsToBeEmptied;

        // A 'switch' statement is used to set the number of cells to be emptied
        // based on the value of 'difficulty'.
        switch (difficulty) {
            case EASY: { // If the difficulty level is 'EASY'.
                cellsToBeEmptied = 30; // 30 cells will be emptied.
                break; // The 'break' statement is important to exit the switch statement after this case.
                       // Without 'break', execution would continue to the next case (fall-through).
            }
            case HARD: { // If the difficulty level is 'HARD'.
                cellsToBeEmptied = 50; // 50 cells will be emptied.
                break;
            }
            case MEDIUM: // If the difficulty level is 'MEDIUM'.
                        // This case is also treated as the default case, if no other case applies.
            default:     // The 'default' case is executed if none of the previous 'case' values match 'difficulty'.
                         // For an Enum as a switch variable, this ideally should not occur if all Enum values are covered.
                cellsToBeEmptied = 40; // 40 cells will be emptied (for MEDIUM or as default).
                break;
        }

        // Creates an object of the Random class. This object is used to generate random numbers,
        // which are needed to select random cells to empty.
        // It is created once here (outside the loop) to be more efficient,
        // instead of creating a new Random object in each iteration.
        Random random = new Random();
        // Counter variable to track how many cells have already been successfully emptied.
        int successfullyEmptiedCount = 0;

        // Loop that runs until the desired number ('cellsToBeEmptied') of cells has been emptied.
        // This method for emptying cells is "efficient" in the sense that it directly attempts
        // to empty a cell if it is not already empty. However, it does not guarantee that
        // exactly 'cellsToBeEmptied' cells will be emptied if 'cellsToBeEmptied' is very high
        // and it becomes difficult to find cells that are not already empty.
        // An alternative strategy could be to create a list of all filled cells and then randomly
        // remove 'cellsToBeEmptied' cells from this list to ensure the exact number is reached,
        // as long as enough cells are available.
        while (successfullyEmptiedCount < cellsToBeEmptied) {
            // Selects a random row number between 0 (inclusive) and GridSize (exclusive, so 0-8).
            int rRow = random.nextInt(GridSize);
            // Selects a random column number between 0 (inclusive) and GridSize (exclusive, so 0-8).
            int rCol = random.nextInt(GridSize);

            // Checks if the randomly selected cell (at position rRow, rCol) has a value not equal to 0
            // (i.e., it is not already empty).
            if (grid[rRow][rCol].value != 0) {
                // If the cell is not empty, its value is set to 0 (emptied).
                grid[rRow][rCol].value = 0;
                // Increments the counter of successfully emptied cells.
                successfullyEmptiedCount++;
            }
            // If the cell was already empty, nothing happens in this iteration, and the loop
            // tries again to empty a cell in the next iteration.
        }

        // After the required number of cells has been emptied,
        // the 'isGiven' status must be correctly set for all cells in the grid.
        // Cells that still have a value after emptying are the given numbers of the puzzle.
        // Cells that are now empty (value 0) are not given and must be filled by the player.
        for (int row = 0; row < GridSize; row++) {
            for (int col = 0; col < GridSize; col++) {
                // Sets 'isGiven' to 'true' if the cell has a value not equal to 0 (i.e., not empty).
                // Otherwise (if value == 0), 'isGiven' is set to 'false'.
                // This is a compact way of writing an if-else condition:
                // if (grid[row][col].value != 0) { grid[row][col].isGiven = true; } else { grid[row][col].isGiven = false; }
                grid[row][col].isGiven = (grid[row][col].value != 0);
            }
        }
    }
}
