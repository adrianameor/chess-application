//ADRIANA BINTI MEOR AZMAN	1211111079@student.mmu.edu.my

import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List; // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images

// Define the Tor class
// The Tor class represents a chess piece that can move vertically and horizontally, similar to a rook in traditional chess.
class Tor extends Piece {

    // Constructor for the Tor class, accepting the color of the piece
    public Tor(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the color
    }

    // Method to get the image icon for the Tor piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name 
        String key = getColor() + getClass().getSimpleName(); //  (e.g., "RedTor", "BlueTor")
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the Tor piece
    @Override
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Initialize a list to store valid moves
        List<int[]> moves = new ArrayList<>();

        // Define possible move directions: vertical and horizontal
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // Down, Up, Right, Left

        // Iterate through each possible direction
        for (int[] dir : directions) {

            // Get all valid moves in the current direction and add them to the moves list
            moves.addAll(getMovesInDirection(row, col, dir, board));
        }

        // Return the list of valid moves for the Tor piece
        return moves;
    }

    // Helper method to get valid moves in a specific direction
    private List<int[]> getMovesInDirection(int row, int col, int[] dir, Piece[][] board) {

        // Initialize a list to store valid moves in the current direction
        List<int[]> moves = new ArrayList<>();

        // Calculate the new row and column based on the current position and direction
        int newRow = row + dir[0];
        int newCol = col + dir[1];

        // Continue moving in the specified direction until the edge of the board is reached
        while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 5) {

            // If the target cell is empty, add it as a valid move
            if (board[newRow][newCol] == null) {
                moves.add(new int[]{newRow, newCol});

            } else { // Any piece stops the movement
                if (!board[newRow][newCol].getColor().equals(this.getColor())) {
                    moves.add(new int[]{newRow, newCol}); // Add the capturing move
                }
                break; // Stop iterating in this direction
            }
            // Move to the next cell in the same direction
            newRow += dir[0];
            newCol += dir[1];
        }

        // Return the list of valid moves in the specified direction
        return moves;

    }
}
