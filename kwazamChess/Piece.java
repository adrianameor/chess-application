

// Piece.java
import java.io.Serializable; // Importing Serializable for saving/loading game state
import java.util.List; // Importing List interface for handling lists of valid moves
import javax.swing.Icon; // Importing Icon for handling piece images

// The Piece class is an abstract base class for all chess pieces.
// It implements Serializable to allow saving and loading the game state.
public abstract class Piece implements Serializable {

    protected String color; // The color of the piece, either "Red" or "Blue"

    // Constructor for the Piece class, accepting the color of the piece
    public Piece(String color) {
        this.color = color; // Initialize the color of the piece
    }

    // Method to get the color of the piece
    public String getColor() {
        return color; // Return the color of the piece
    }

    // Method to check if the piece should be upgraded (default implementation does nothing)
    public void checkForUpgrade(int row, int col, Piece[][] board, ChessBoard view) {
        // Default: do nothing
    }

    // Abstract to ensure each piece provides its icon
    public abstract Icon getImageIcon(); // Updated method to return Icon

    // Abstract method to get the valid moves for a piece
    public abstract List<int[]> getValidMoves(int row, int col, Piece[][] board);

    // Check if the move is valid based on the piece's valid moves
    public boolean isValidMove(int newRow, int newCol, int currentRow, int currentCol, Piece[][] board) {

        // Get the list of valid moves for the piece from its current position
        List<int[]> validMoves = getValidMoves(currentRow, currentCol, board);

        // Check if the proposed move is in the list of valid moves
        for (int[] move : validMoves) {
            if (move[0] == newRow && move[1] == newCol) {
                return true; // The move is valid
            }
        }
        return false; // The move is not valid
    }
}
