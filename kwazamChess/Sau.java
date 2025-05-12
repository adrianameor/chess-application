

import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List; // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images

// The Sau class represents a chess piece that can move one step in any direction
public class Sau extends Piece {

    // Constructor for the Sau class, accepting the color of the piece
    public Sau(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the color
    }

    // Method to get the image icon for the Sau piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name
        String key = getColor() + getClass().getSimpleName(); // e.g., "RedBiz", "BlueBiz"
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the Sau piece
    @Override
    // Initialize a list to store valid moves
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Implement the logic for valid moves of Sau piece
        // Sau can move one step in all eight directions
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Iterate through each possible direction
        for (int[] dir : directions) {

            // Calculate the new row and column based on the current position and direction
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if the new position is within the bounds of the board
            // and if the move is valid (can skip over other pieces)
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 5
                    && (board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.getColor()))) {
                // If valid, add the new position to the list of valid moves
                moves.add(new int[]{newRow, newCol});
            }
        }

        // Return the list of valid moves for the Sau piece
        return moves;
    }
}
