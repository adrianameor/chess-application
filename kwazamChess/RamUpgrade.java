
import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List; // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images

// The RamUpgrade class represents an upgraded version of the Ram chess piece.
public class RamUpgrade extends Piece {

    // Constructor for the RamUpgrade class, accepting the color of the piece
    public RamUpgrade(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the color
    }

    // Method to get the image icon for the RamUpgrade piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name (e.g., "RedRamUpgrade", "BlueRamUpgrade")
        String key = getColor() + getClass().getSimpleName();

        // Retrieve the corresponding icon from the ChessBoard's pieceIcons map
        // If the icon is not found, return a default empty icon
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the RamUpgrade piece
    @Override
    // Initialize a list to store valid moves
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Implement the logic for valid moves of Ram piece
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}}; // Ram can move one step forward or backward

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
        // Return the list of valid moves for the RamUpgrade piece
        return moves;
    }
}
