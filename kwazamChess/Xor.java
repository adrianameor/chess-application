//NUR ADIBAH BINTI KHAIRUL ANUAR	1211112286@student.mmu.edu.my

import java.util.ArrayList; // Importing ArrayList for storing valid moves
import java.util.List;  // Importing List interface for handling lists of valid moves
import javax.swing.ImageIcon; // Importing ImageIcon for handling piece images

// The Xor class represents a chess piece that can move diagonally any number of steps.
class Xor extends Piece {

    // Constructor for the Xor class, accepting the color of the piece
    public Xor(String color) {
        super(color); // Call the constructor of the superclass (Piece) to set the colo
    }

    // Method to get the image icon for the Xor piece
    @Override
    public ImageIcon getImageIcon() {

        // Create a key based on the piece's color and class name
        String key = getColor() + getClass().getSimpleName(); // (e.g., "RedXor", "BlueXor")
        return ChessBoard.pieceIcons.getOrDefault(key, new ImageIcon()); // Return default empty icon if not found
    }

    // Method to get all valid moves for the Xor piece
    @Override
    public List<int[]> getValidMoves(int row, int col, Piece[][] board) {

        // Implement the logic for valid moves of Xor piece
        // Xor can move diagonally any number of steps
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Iterate through each possible direction
        for (int[] dir : directions) {

            // Calculate the new row and column based on the current position and direction
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Continue moving in the specified direction until the edge of the board is reached
            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 5) {
                if (board[newRow][newCol] == null) {

                    // Empty cell, add as a valid move
                    moves.add(new int[]{newRow, newCol});

                } else if (!board[newRow][newCol].getColor().equals(this.getColor())) {

                    // Enemy piece, add as a valid move and stop
                    moves.add(new int[]{newRow, newCol}); // Add the capturing move
                    break;

                } else {

                    // If a friendly piece is encountered, stop further movement
                    break;
                }
                // Move further in the current direction
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        // Return the list of valid moves for the Xor piece
        return moves;
    }
}
