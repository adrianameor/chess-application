

import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List;  // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images

// The Ram class represents a chess piece that can move one step forward.
public class Ram extends Piece {

    // Constructor for the Ram class, accepting the color of the piece
    public Ram(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the color
    }

    // Method to get the image icon for the Ram piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name
        String key = getColor() + getClass().getSimpleName(); // (e.g., "RedRam", "BlueRam")

        // Retrieve the corresponding icon from the ChessBoard's pieceIcons map
        // If the icon is not found, return a default empty icon
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the Ram piece
    @Override
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Initialize a list to store valid moves
        List<int[]> moves = new ArrayList<>();
        // Get the instance of ChessBoard
        int direction = (color.equals("Red")) ? -1 : 1; // Adjust direction based on flipped state

        // Calculate the new position for the Ram piece (one step forward)
        int newRow = row + direction; // Move one step in the correct direction
        int newCol = col; // Ram moves straight

        // Check if the move is within bounds and valid
        if (isValidMove(newRow, newCol, board)) {

            // If valid, add the new position to the list of valid moves
            moves.add(new int[]{newRow, newCol});
        }

        // Return the list of valid moves for the Ram piece
        return moves;
    }

    // Validate the move to ensure it is within the board limits and not blocked by friendly pieces
    private boolean isValidMove(int row, int col, Piece[][] board) {

        // Check if the position is within bounds and if the target cell is either empty or occupied by an opponent's piece
        return row >= 0 && row < 8 && col >= 0 && col < 5
                && (board[row][col] == null || !board[row][col].getColor().equals(this.getColor()));
    }

    // After the move is executed, check if the piece should be upgraded
    // In Ram.java (adjust the checkForUpgrade signature):
    @Override
    public void checkForUpgrade(int row, int col, Piece[][] board, ChessBoard view) { // Added view parameter
        if (row == 0 || row == 7) { //Upgrade condition, // If the Ram piece reaches the first row
            board[row][col] = new RamUpgrade(this.getColor());  // Upgrade the Ram piece to RamUpgrade
            view.showMessage(this.getColor() + " Ram has been upgraded to Ram Upgrade!"); // show the message.
        }
    }

}
