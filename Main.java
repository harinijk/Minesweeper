// Written by Harini Kuchibhotla, kuchi027

//Import Section

import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class Main {

    public static void main(String[] args) {

        //creating a scanner for user input
        Scanner scanner = new Scanner(System.in);
        //input for mode
        String mode;

        //validating mode input
        while (true) {
            System.out.println("Choose one level: (Easy, Medium, Hard)");
            mode = scanner.nextLine().toLowerCase();

            if (mode.equals("easy") || mode.equals("medium") || mode.equals("hard")) {
                break; // Exit the loop if input is valid
            } else {
                System.out.println("Invalid input. Please choose again.");
            }
        }

        int rows, cols, flags;

        //assigning number of rows, columns, flags according to chosen mode
        if (mode.equals("easy")) {
            rows = 5;
            cols = 5;
            flags = 5;
        } else if (mode.equals("medium")) {
            rows = 9;
            cols = 9;
            flags = 12;
        } else {
            rows = 20;
            cols = 20;
            flags = 40;
        }

        //creating minefield
        Minefield minefield = new Minefield(rows, cols, flags);

        // input for debug mode
        String debugModeString;

        //validating debug mode input
        while (true) {
            System.out.println("Choose debug mode:( ON, OFF )");
            debugModeString = scanner.nextLine().toLowerCase();

            if (debugModeString.equals("on") || debugModeString.equals("off")) {
                break; // Exit the loop if input is valid
            } else {
                System.out.println("Invalid input. Please choose again.");
            }
        }
        boolean debugMode;

        //assigning appropriate value to debug mode based on input
        if (debugModeString.equals("on")) {
            debugMode = true;
        } else {
            debugMode = false;
        }

        //input for starting coordinates
        System.out.println("Enter starting coordinates: [x] [y]");
        int xPos = scanner.nextInt();
        int yPos = scanner.nextInt();

        minefield.createMines(xPos, yPos, flags);
        minefield.evaluateField();
        minefield.revealStartingArea(xPos, yPos);

        //calling debugMode for starting area if chosen debugMode
        if (debugMode) {
            System.out.println();
            minefield.debug();
            System.out.println();
            System.out.println(minefield);
        } else {
            System.out.println();
            System.out.println(minefield);
        }

        while (!minefield.gameOver()) {

            //input for coordinates of cell to be guessed and if choosing to place flag
            // for flag choice, inputting f if one wants to place a flag and -1 if not placing a flag
            int x, y;
            String flag;
            Boolean flagTwo;

            // validating guessed coordinates input
            while (true) {
                System.out.println("Enter a coordinate and if you wish to place a flag (remaining " + minefield.getFlags() + ") : [x] [y] [f or -1]");
                x = scanner.nextInt();
                y = scanner.nextInt();
                flag = scanner.nextLine().trim();

                if ((x >= 0 && x < minefield.getRows()) && (y >= 0 && y < minefield.getCols())) {
                    if (flag.equals("-1") || flag.equals("f")) {
                        flagTwo = flag.equals("f");
                        // Exit loop if input is valid
                        break;
                    } else {
                        System.out.println("Invalid flag input. Please enter 'f' or '-1'.");
                    }
                } else {
                    System.out.println("Invalid coordinates. Please enter valid coordinates.");
                }
            }

            minefield.guess(x, y, flagTwo);

            //calling debugMode for each guess if chosen debugMode
            if (debugMode) {
                System.out.println();
                minefield.debug();
                System.out.println();
                System.out.println(minefield);
            } else {
                System.out.println();
                System.out.println(minefield);
            }
        }
        scanner.close();
    }
}
