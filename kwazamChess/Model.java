

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// This class handles saving and loading the game state.
public class Model {

    // Map to store piece creation functions, keyed by piece type name
    private static final Map<String, PieceFactory> pieceFactories = new HashMap<>();

    // Interface for piece creation
    private interface PieceFactory {

        Piece create(String color);
    }

    // Static initializer to populate the piece factory map
    static {
        pieceFactories.put("Biz", Biz::new);  // Using method references
        pieceFactories.put("Ram", Ram::new);
        pieceFactories.put("Sau", Sau::new);
        pieceFactories.put("Xor", Xor::new);
        pieceFactories.put("Tor", Tor::new);
        pieceFactories.put("RamUpgrade", RamUpgrade::new); // Include RamUpgrade
    }

    // Saves the current game state to a file named "gameState.txt"
    public static void saveGame(Piece[][] board, int turnCount, int halfTurnCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("gameState.txt"))) {
            writer.write(turnCount + "\n"); // Save the turn count
            writer.write(halfTurnCount + "\n"); // Save the half-turn count

            // Loop through the board
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 5; col++) {
                    Piece piece = board[row][col];
                    // if a cell has a piece
                    if (piece != null) {
                        // save the peice type, color, row and column
                        writer.write(piece.getClass().getSimpleName() + "," + piece.getColor() + "," + row + "," + col + "\n");
                    }
                }
            }
        }
    }

    // Loads the game state from the "gameState.txt" file.
    public static Object[] loadGame() throws IOException {
        Piece[][] board = new Piece[8][5];
        int turnCount = 0;
        int halfTurnCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("gameState.txt"))) {
            String turnCountStr = reader.readLine(); // read turn counts
            String halfTurnCountStr = reader.readLine();
            if (turnCountStr != null) {
                turnCount = Integer.parseInt(turnCountStr);
            }
            if (halfTurnCountStr != null) {
                halfTurnCount = Integer.parseInt(halfTurnCountStr);
            }
            String line;
            while ((line = reader.readLine()) != null) { // Read each line (representing a piece)
                String[] parts = line.split(","); // Split the line into parts
                String pieceType = parts[0]; // Extract piece information
                String color = parts[1];
                int row = Integer.parseInt(parts[2]);
                int col = Integer.parseInt(parts[3]);

                Piece piece = createPiece(pieceType, color); // Create the piece using a factory
                board[row][col] = piece; // Place the piece on the board
            }
        }
        return new Object[]{board, turnCount, halfTurnCount};
    }

    private static Piece createPiece(String pieceType, String color) {
        PieceFactory factory = pieceFactories.get(pieceType);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown piece type: " + pieceType); // Handle unknown piece type
        }
        return factory.create(color); // Create piece using the factory
    }
}
