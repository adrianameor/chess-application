

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// This class controls the game logic and updates the view (ChessBoard)
public class ChessController {

    // List of observers (like the ChessBoard) to be notified of changes
    private List<GameObserver> observers = new ArrayList<>();
    // 2D array representing the chessboard. Each element holds a Piece
    private Piece[][] board;
    private Piece selectedPiece = null;
    private int selectedRow, selectedCol;
    private int turnCount = 0; // Current turn number
    private int halfTurnCount = 0; // Count of half-turns (one move by one player)
    private String currentPlayerColor = "Red"; // Initialize current player
    private ChessBoard view; // Reference to the ChessBoard (the view)

    public ChessController() {
        initializeBoard();
    }

    // Tell the view to update the board display
    private void notifyObserversBoardChanged() {
        for (GameObserver observer : observers) {
            observer.updateBoard(board);
        }
    }

    // Tell the view to update the turn label.
    private void notifyObserversTurnChanged() {
        for (GameObserver observer : observers) {
            observer.updateTurnLabel(turnCount, halfTurnCount);
        }
    }

    // Getter for the board.  Returns the 2D array of pieces
    public Piece[][] getBoard() {
        return board;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public int getHalfTurnCount() {
        return halfTurnCount;
    }

    // Called when a cell on the board is clicked
    public void handleCellClick(int row, int col) {
        if (view.isFlipped()) {
            row = 7 - row;
            col = 4 - col;
        }
        // If no piece is selected and a piece is clicked
        if (selectedPiece == null && board[row][col] != null) {
            if (isCurrentPlayerTurn(board[row][col])) {
                // Select the clicked piece
                selectedPiece = board[row][col];
                selectedRow = row;
                selectedCol = col;
                view.clearHighlights();
                view.highlightMoves(selectedPiece.getValidMoves(row, col, board));
            } else {
                view.showMessage("It's not your turn!");
            }
        } else if (selectedPiece != null) {
            if (selectedPiece.isValidMove(row, col, selectedRow, selectedCol, board)) {
                Piece tempPiece = board[row][col];
                board[row][col] = selectedPiece;
                board[selectedRow][selectedCol] = null;
                new Thread(() -> playSound("audio/PieceMove.wav")).start(); // Play sound when a piece is moved
                if (tempPiece instanceof Sau) {
                    new Thread(() -> playSound("audio/SauCaptured.wav")).start(); // Play sound when Sau is captured
                    String winnerColor = !selectedPiece.getColor().equals("Red") ? "Blue" : "Red";
                    view.showMessage(winnerColor + " wins! Sau has been captured. Restart the game.");
                    restartGame();
                    return;
                }
                if (selectedPiece instanceof Ram ram) {
                    ram.checkForUpgrade(row, col, board, view); // Pass view as an argument
                }
                selectedPiece = null;
                view.clearHighlights();
                view.updateBoard(board);
                halfTurnCount++;
                if (halfTurnCount % 2 == 0) {
                    turnCount++;
                }
                view.updateTurnLabel(turnCount, halfTurnCount);
                convertPieces();
                view.updateBoard(board);
            } else if (board[row][col] != null && isCurrentPlayerTurn(board[row][col])) {
                selectedPiece = board[row][col];
                selectedRow = row;
                selectedCol = col;
                view.clearHighlights();
                if (selectedPiece instanceof Sau) {
                    List<int[]> validMoves = selectedPiece.getValidMoves(row, col, board).stream()
                            .filter(move -> !isUnderAttack(move[0], move[1], selectedPiece.getColor()))
                            .toList();
                    view.highlightMoves(validMoves);
                } else {
                    view.highlightMoves(selectedPiece.getValidMoves(row, col, board));
                }
            } else {
                view.showMessage("Invalid move!");
            }
        }
    }

    public boolean isUnderAttack(int row, int col, String playerColor) {
        String opponentColor = playerColor.equals("Red") ? "Blue" : "Red";

        // Iterate through the board to find opponent pieces
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    // Check if this opponent's piece can attack the target cell
                    List<int[]> validMoves = piece.getValidMoves(r, c, board);
                    for (int[] move : validMoves) {
                        if (move[0] == row && move[1] == col) {
                            return true; // Target cell is under attack
                        }
                    }
                }
            }
        }
        return false; // Target cell is safe
    }


    // Check if it's the current player's turn based on the piece's color.
    private boolean isCurrentPlayerTurn(Piece piece) {
        if (view.isFlipped()) {
            return piece.getColor().equals("Blue");
        } else {
            return piece.getColor().equals("Red");
        }
    }

    // Converts Tor and Xor pieces every two rounds.
    private void convertPieces() {
        if (halfTurnCount > 0 && halfTurnCount % 4 == 0) {  // Every 2 rounds
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 5; col++) {
                    Piece piece = board[row][col];
                    if (piece instanceof Tor) { // If it's a Tor
                        // change it to an Xor of the same color
                        board[row][col] = new Xor(piece.getColor());
                    } else if (piece instanceof Xor) { // If it's an Xor
                        // change it to a Tor of the same color
                        board[row][col] = new Tor(piece.getColor());
                    }
                }
            }
            // Update the view to show the changed pieces
            view.updateBoard(board);
        }
    }

    public void restartGame() {
        initializeBoard(); // Set up the initial board state
        turnCount = 0;
        halfTurnCount = 0;
        currentPlayerColor = "Red";
        view.updateTurnLabel(turnCount, halfTurnCount);
        selectedPiece = null;
        view.updateBoard(board);
        view.clearHighlights();
        notifyObserversBoardChanged();
        notifyObserversTurnChanged(); // Turn label and current player need to be updated
    }

    // Adds an observer to be notified of game state changes
    public void addObserver(GameObserver observer) {
        observers.add(observer);
        if (observer instanceof ChessBoard) {  // Check if the observer is a ChessBoard instance
            this.view = (ChessBoard) observer; // Set the view field directly
        }
    }

    // Save the current game state to a file
    public void saveGame() {
        try {
            Model.saveGame(board, turnCount, halfTurnCount); // Save the game using the Model
            view.showMessage("Game saved!");
        } catch (IOException e) {
            view.showMessage("Failed to save game!");
        }
    }

    // Load the game state from a file
    public void loadGame() {
        try {
            Object[] gameState = Model.loadGame();
            board = (Piece[][]) gameState[0];
            turnCount = (int) gameState[1];
            halfTurnCount = (int) gameState[2];
            view.updateBoard(board);
            view.updateTurnLabel(turnCount, halfTurnCount);
        } catch (IOException e) {
            // If loading fails, start a new game
            restartGame();
        }
        notifyObserversBoardChanged();
        notifyObserversTurnChanged();
    }

    // Initialize the board with pieces in their starting positions
    private void initializeBoard() {
        board = new Piece[8][5];
        // Clear the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col] = null;
            }
        }

        // Place Biz pieces for testing
        board[0][1] = new Biz("Blue"); // First row, second left column
        board[0][3] = new Biz("Blue"); // First row, second right column
        board[7][1] = new Biz("Red"); // Last row, second left column
        board[7][3] = new Biz("Red"); // Last row, second right column
        board[0][2] = new Sau("Blue");
        board[7][2] = new Sau("Red");
        board[7][4] = new Tor("Red"); // Last row, second right column
        board[7][0] = new Xor("Red"); // Last row, second right column
        board[0][0] = new Tor("Blue"); // Last row, second right column
        board[0][4] = new Xor("Blue"); // Last row, second right column

        for (int col = 0; col < 5; col++) {
            board[1][col] = new Ram("Blue");
            board[6][col] = new Ram("Red");
        }
    }

    private void playSound(String soundFile) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(soundFile)));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
