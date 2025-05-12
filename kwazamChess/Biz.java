//NURAFRINA BATRISYIA BINTI NORDZAMAN	1231303327@student.mmu.edu.my

import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List; // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images              

// Define the Biz class, which extends the Piece class
public class Biz extends Piece {

    // Constructor for the Biz class, accepting the color of the piece
    public Biz(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the color
    }

    // Method to get the image icon for the Biz piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name
        String key = getColor() + getClass().getSimpleName(); // e.g., "RedBiz", "BlueBiz"

        // Retrieve the corresponding icon from the ChessBoard's pieceIcons map
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the Biz piece
    @Override
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Initialize a list to store valid moves
        List<int[]> moves = new ArrayList<>();

        // Define all possible L-shaped moves for the Biz piece
        int[][] directions = {
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}, // Moves in vertical direction
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1} // Moves in horizontal direction
        };

        // Iterate through each possible direction
        for (int[] dir : directions) {

            // Calculate the new row and column based on the current position and direction
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if the move is within bounds and valid (can skip over other pieces)
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 5
                    && (board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.getColor()))) {
                // If valid, add the new position to the list of valid moves
                moves.add(new int[]{newRow, newCol});
            }
        }

        // Return the list of valid moves for the Biz piece
        return moves;
    }
}
