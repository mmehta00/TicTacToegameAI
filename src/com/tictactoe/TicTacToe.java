package com.tictactoe;

import java.util.Scanner;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private AIPlayer aiPlayer;
    private Scanner scanner;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X'; // Player starts as 'X'
        scanner = new Scanner(System.in);
        initializeBoard();
        selectDifficulty(); // Ask user for AI difficulty
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void selectDifficulty() {
        System.out.println("Select AI Difficulty: (easy / medium / hard)");
        String difficulty;
        while (true) {
            difficulty = scanner.nextLine().trim().toLowerCase();
            if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'easy', 'medium', or 'hard'.");
            }
        }
        aiPlayer = new AIPlayer('O', difficulty);
        System.out.println("AI Difficulty set to: " + difficulty.toUpperCase());
    }

    public void playGame() {
        boolean gameRunning = true;
        while (gameRunning) {
            printBoard();
            if (currentPlayer == 'X') {
                playerMove();
            } else {
                aiMove();
            }

            if (isWinner(currentPlayer)) {
                printBoard();
                System.out.println("Player " + currentPlayer + " wins!");
                gameRunning = false;
            } else if (isDraw()) {
                printBoard();
                System.out.println("It's a draw!");
                gameRunning = false;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch turns
        }
    }

    private void playerMove() {
        int row, col;
        while (true) {
            System.out.print("Enter row (0-2) and column (0-2): ");
            row = scanner.nextInt();
            col = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ') {
                board[row][col] = 'X';
                break;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    private void aiMove() {
        System.out.println("AI is thinking...");
        int[] bestMove = aiPlayer.getBestMove(board);
        board[bestMove[0]][bestMove[1]] = 'O';
        System.out.println("AI placed 'O' at (" + bestMove[0] + ", " + bestMove[1] + ")");
    }

    private boolean isWinner(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playGame();
    }
}
