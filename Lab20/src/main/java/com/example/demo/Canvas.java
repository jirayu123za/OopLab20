package com.example.demo;

import lombok.Getter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Canvas {
    @Getter
    private final int BOARD_SIZE = 3;
    @Getter
    private final String PLAYER_X = "X";
    @Getter
    private final String PLAYER_O = "O";
    @Getter
    private final String EMPTY_CELL = " ";
    @Getter
    private final String[][] board;
    @Getter
    private String currentPlayer;
    private SimpMessagingTemplate template;

    public Canvas() {
        board = new String[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
        currentPlayer = PLAYER_X;
    }

    public boolean makeMove(int row, int col, String player) {
        if (!player.equals(currentPlayer)) {
            // It's not this player's turn
            return false;
        }

        if (board[row][col].equals(EMPTY_CELL)) {
            board[row][col] = player;
            currentPlayer = (currentPlayer.equals(PLAYER_X)) ? PLAYER_O : PLAYER_X;
            return true;
        } else {
            // The cell is already occupied
            return false;
        }
    }

    public boolean checkWin(String player) {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            boolean win = true;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[i][j].equals(player)) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < BOARD_SIZE; i++) {
            boolean win = true;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[j][i].equals(player)) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }

        // Check diagonals
        boolean win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!board[i][i].equals(player)) {
                win = false;
                break;
            }
        }
        if (win) {
            return true;
        }

        win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!board[i][BOARD_SIZE - i - 1].equals(player)) {
                win = false;
                break;
            }
        }
        if (win) {
            return true;
        }

        return false;
    }

    public boolean checkDraw() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(EMPTY_CELL)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean paint(int row, int col, String player) {
        // Make the move on the game board
        boolean moveSuccessful = makeMove(row, col, player);

        if (moveSuccessful) {
            // Check if the game is over
            if (checkWin(player)) {
                template.convertAndSend("/topic/gameOver", player + " wins!");
                resetBoard();
            } else if (checkDraw()) {
                template.convertAndSend("/topic/gameOver", "Draw!");
                resetBoard();
            } else {
                // The move was successful and the game is still ongoing
                template.convertAndSend("/topic/canvas", this);
            }
        } else {
            // The move was not valid
            template.convertAndSend("/topic/error", "Invalid move.");
        }
        return moveSuccessful;
    }
}