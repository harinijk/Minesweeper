// Written by Harini Kuchibhotla, kuchi027

import java.util.Random;

public class Minefield {
    /**
     * Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /*
     * Class Variable Section
     *
     */

    private Cell[][] mineField;
    private Cell[][] mineFieldCopy;
    int numRows;
    int numCols;
    private int numFlags;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     * <p>
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     *
     * @param rows    Number of rows.
     * @param columns Number of columns.
     * @param flags   Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {

        // initialising instance variables
        numRows = rows;
        numCols = columns;
        mineField = new Cell[rows][columns];
        mineFieldCopy = new Cell[rows][columns];

        // creating cells in the mineField
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                mineField[i][j] = new Cell(false, "0");
                mineFieldCopy[i][j] = new Cell(false, "0");
            }
        }
        numFlags = flags;
    }


    // getter for the number of flags
    public int getFlags() {
        return numFlags;
    }

    // getter for the number of rows
    public int getRows() {
        return numRows;
    }

    // getter for the number of columns
    public int getCols() {
        return numCols;
    }

    /**
     * evaluateField
     *
     * @function: Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     */
    public void evaluateField() {

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {

                // check if mine is found
                if (mineField[i][j].getStatus().equals("M")) {

                    // checking adjacent tiles
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {

                            // checking if coordinates in bounds of minefield
                            if (k >= 0 && k < numRows && l >= 0 && l < numCols) {
                                if (mineField[k][l].getStatus().equals("-")) {
                                    mineField[k][l].setStatus("0");
                                }
                                if (!mineField[k][l].getStatus().equals("M") && !mineField[k][l].getStatus().equals("-")) {
                                    int number = Integer.parseInt(mineField[k][l].getStatus());
                                    number++;
                                    mineField[k][l].setStatus(Integer.toString(number));
                                }
                            }
                        }
                    }
                }

            }
        }

        // copying elements to mineFieldCopy to use in debug
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                mineFieldCopy[i][j].setStatus(mineField[i][j].getStatus());
            }
        }
    }

    /**
     * createMines
     * <p>
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {

        int count = 0;
        Random rand = new Random();

        // loop until required number of mines
        while (count != mines) {

            int row = rand.nextInt(0, numRows);
            int col = rand.nextInt(0, numCols);
            //check for coordinate not equal to starting cell
            if (row != x && col != y) {
                if (!(mineField[row][col].getStatus().equals("M"))) {

                    // setting it as minefield
                    mineField[row][col].setStatus("M");
                    count++;
                }
            }
        }
    }

    /**
     * guess
     * <p>
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {

        // check if coordinates in bounds
        if (x >= 0 && x < numRows && y >= 0 && y < numCols) {

            //check if the minefield cell is already revealed
            if (mineField[x][y].getRevealed()) {
                System.out.print("Coordinates already chosen before, choose again!");
                return false;
            }

            // check if putting a flag and if there are remaining flags
            if (flag) {
                if (numFlags > 0) {
                    mineField[x][y].setStatus("F");
                    mineField[x][y].setRevealed(true);
                    numFlags--;
                    return false;
                }
                System.out.println("not enough flags");
                return false;
            }

            // case when cell status is 0
            if (mineField[x][y].getStatus().equals("0")) {
                mineField[x][y].setRevealed(true);
                revealZeroes(x, y);
                return false;
            }

            // case when cell status is M
            else if (mineField[x][y].getStatus().equals("M")) {
                mineField[x][y].setRevealed(true);
                return true;
            }

            // remaining cases
            else {
                mineField[x][y].setRevealed(true);
                return false;
            }
        }
        System.out.print("invalid choice, coordinates out of bounds");
        return false;
    }

    /**
     * gameOver
     * <p>
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {

        int count = 0;
        int countTwo = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {

                // case when a mine has been revealed
                if (mineField[i][j].getStatus().equals("M") && mineField[i][j].getRevealed()) {
                    count++;
                }

                // case when the cell has been revealed
                if (mineField[i][j].getRevealed()) {
                    countTwo++;
                }
            }
        }

        // player loses if they revealed one mine including one revealed in revealStartingArea
        if (count == 2) {
            System.out.println("Player loses");
            return true;
        }

        // player wins if all cells are revealed
        else if (countTwo == numRows * numCols) {
            System.out.println("Player wins");
            return true;
        }
        return false;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<Integer> temp = new Stack1Gen<>();

        // pushing both row and column coordinates onto stack
        temp.push(x);
        temp.push(y);

        while (!temp.isEmpty()) {
            Integer numY = temp.pop();
            Integer numX = temp.pop();
            mineField[numX][numY].setRevealed(true);

            //looping through adjacent cells and checking if coordinates in bounds
            for (int k = numX - 1; k <= numX + 1; k++) {
                for (int l = numY - 1; l <= numY + 1; l++) {
                    if ((k == numX || l == numY) && k >= 0 && k < numRows && l >= 0 && l < numCols) {

                        //add unrevealed (unvisited) cells with status 0 to the stack
                        if (!mineField[k][l].getRevealed() && mineField[k][l].getStatus().equals("0")) {
                            temp.push(k);
                            temp.push(l);
                        }
                    }
                }
            }
        }
    }

    /**
     * revealStartingArea
     * <p>
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {

        Q1Gen<Integer> reveal = new Q1Gen<Integer>();

        // adding both row and column coordinates to the queue
        reveal.add(x);
        reveal.add(y);

        while (!(reveal.length() == 0)) {
            int numX = reveal.remove();
            int numY = reveal.remove();
            mineField[numX][numY].setRevealed(true);

            // ending if a mine is found
            if (mineField[numX][numY].getStatus().equals("M")) {
                break;
            }

            //looping through adjacent cells and checking if coordinates in bounds
            for (int k = numX - 1; k <= numX + 1; k++) {
                for (int l = numY - 1; l <= numY + 1; l++) {
                    if ((k == numX || l == numY) && k >= 0 && k < numRows && l >= 0 && l < numCols) {

                        // add unrevealed (unvisited) cells to queue
                        if (!mineField[k][l].getRevealed()) {
                            reveal.add(k);
                            reveal.add(l);
                        }
                    }
                }

            }
        }
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided!
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() {
        System.out.print("  ");

        //printing the column numbers
        for (int p = 0; p < numCols; p++) {
            //printing single digit column numbers
            if (p < 10) {
                System.out.print("  " + p);
            }
            // printing double digit column numbers
            else {
                System.out.print(" " + p);
            }
        }
        System.out.println();

        for (int i = 0; i < numRows; i++) {

            // printing the row numbers
            if (i < 10) {
                //printing single digit row numbers
                System.out.print(i + "  ");
            } else {
                //printing double digit row numbers
                System.out.print(i + " ");
            }
            for (int j = 0; j < numCols; j++) {

                // System.out.print(mineFieldCopy[i][j].getStatus());
                // printing all cells with chosen color according to cell status
                switch (mineFieldCopy[i][j].getStatus()) {
                    case "M":
                        System.out.print(ANSI_RED + " M " + ANSI_GREY_BACKGROUND);
                        break;
                    case "0":
                        System.out.print(ANSI_YELLOW + " 0 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "1":
                        System.out.print(ANSI_BLUE + " 1 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "2":
                        System.out.print(ANSI_GREEN + " 2 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "3":
                        System.out.print(ANSI_PURPLE + " 3 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "4":
                        System.out.print(ANSI_CYAN + " 4 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "5":
                        System.out.print(ANSI_YELLOW_BRIGHT + " 5 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "6":
                        System.out.print(ANSI_RED_BRIGHT + " 6 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "7":
                        System.out.print(ANSI_BLUE + " 7 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "8":
                        System.out.print(ANSI_GREEN + " 8 " + ANSI_GREY_BACKGROUND);
                        break;
                    case "9":
                        System.out.print(ANSI_CYAN + " 9 " + ANSI_GREY_BACKGROUND);
                        break;
                    default:
                        System.out.print(" - ");
                        break;
                }
            }
            System.out.println();
        }
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {

        StringBuilder s = new StringBuilder(" ");
        s.append(" ");

        //appending the column numbers
        for (int p = 0; p < numCols; p++) {
            //appending single digit column numbers
            if (p < 10) {
                s.append("  " + p);
            }
            //appending double digit column numbers
            else {
                s.append(" " + p);
            }
        }

        s.append("\n");

        for (int i = 0; i < numRows; i++) {

            // appending the row numbers
            if (i < 10) {
                //appending single digit row numbers
                s.append(i + "  ");
            } else {
                //appending double digit row numbers
                s.append(i + " ");
            }
            for (int j = 0; j < numCols; j++) {

                //appending only revealed cells
                if (mineField[i][j].getRevealed()) {

                    // appending all revealed cells with chosen color according to cell status
                    switch (mineField[i][j].getStatus()) {
                        case "M":
                            s.append(ANSI_RED + " M " + ANSI_GREY_BACKGROUND);
                            break;
                        case "F":
                            s.append(ANSI_RED + " F " + ANSI_GREY_BACKGROUND);
                            break;
                        case "0":
                            s.append(ANSI_YELLOW + " 0 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "1":
                            s.append(ANSI_BLUE + " 1 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "2":
                            s.append(ANSI_GREEN + " 2 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "3":
                            s.append(ANSI_PURPLE + " 3 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "4":
                            s.append(ANSI_CYAN + " 4 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "5":
                            s.append(ANSI_YELLOW_BRIGHT + " 5 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "6":
                            s.append(ANSI_RED_BRIGHT + " 6 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "7":
                            System.out.print(ANSI_BLUE + " 7 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "8":
                            System.out.print(ANSI_GREEN + " 8 " + ANSI_GREY_BACKGROUND);
                            break;
                        case "9":
                            System.out.print(ANSI_CYAN + " 9 " + ANSI_GREY_BACKGROUND);
                            break;

                        default:
                            s.append(" - ");
                            break;
                    }

                } else {
                    s.append(" - ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
