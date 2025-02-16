package com.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons;
    private char[][] board;
    private char currentPlayer;
    private AIPlayer aiPlayer;
    private String difficulty;

    public TicTacToeGUI() {
        setTitle("Tic-Tac-Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Difficulty selection
        selectDifficulty();

        // Initialize board
        board = new char[3][3];
        buttons = new JButton[3][3];
        currentPlayer = 'X'; // Player always starts first
        initializeBoard();

        // GUI Panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        add(boardPanel, BorderLayout.CENTER);

        // Add buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                final int row = i, col = j;
                buttons[i][j].addActionListener(e -> playerMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        setVisible(true);
    }

    private void selectDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        difficulty = (String) JOptionPane.showInputDialog(
                this, "Select AI Difficulty:", "Difficulty Selection",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (difficulty == null) {
            System.exit(0); // Exit if no selection
        }

        aiPlayer = new AIPlayer('O', difficulty.toLowerCase());
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void playerMove(int row, int col) {
        if (board[row][col] == ' ') {
            board[row][col] = 'X';
            animateButton(buttons[row][col], "X");
            buttons[row][col].setEnabled(false);

            if (checkGameOver('X')) return;

            aiMove();
        } else {
            shakeWindow();
        }
    }

    private void aiMove() {
        int[] bestMove = aiPlayer.getBestMove(board);
        board[bestMove[0]][bestMove[1]] = 'O';
        animateButton(buttons[bestMove[0]][bestMove[1]], "O");
        buttons[bestMove[0]][bestMove[1]].setEnabled(false);

        checkGameOver('O');
    }

    private void animateButton(JButton button, String text) {
        Timer timer = new Timer(50, new ActionListener() {
            float opacity = 0.1f;

            @Override
            public void actionPerformed(ActionEvent e) {
                button.setForeground(new Color(0, 0, 0, opacity));
                opacity += 0.2f;
                if (opacity >= 1.0f) {
                    button.setText(text);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    private void shakeWindow() {
        Point location = getLocation();
        int delay = 50;
        Timer timer = new Timer(delay, new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int xOffset = (count % 2 == 0) ? 5 : -5;
                setLocation(location.x + xOffset, location.y);
                count++;
                if (count >= 6) {
                    setLocation(location);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    private boolean checkGameOver(char player) {
        if (isWinner(player)) {
            highlightWinningLine(player);
            return true;
        } else if (isDraw()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetGame();
            return true;
        }
        return false;
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

    private void highlightWinningLine(char player) {
        Timer timer = new Timer(300, new ActionListener() {
            boolean toggle = true;
            int count = 6;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == player) {
                            buttons[i][j].setBackground(toggle ? Color.GREEN : Color.WHITE);
                        }
                    }
                }
                toggle = !toggle;
                count--;
                if (count == 0) {
                    ((Timer) e.getSource()).stop();
                    JOptionPane.showMessageDialog(TicTacToeGUI.this, "Player " + player + " wins!");
                    resetGame();
                }
            }
        });
        timer.start();
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

    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(null);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
