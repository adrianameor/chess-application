

// The GameObserver interface defines the methods that any observer of the game state must implement.
// It allows observers to receive updates about the game board, turn changes, and messages.
interface GameObserver {

    // Method to update the game board with the current state of pieces
    void updateBoard(Piece[][] board);

    // Method to update the turn label, indicating the current turn count and half turn count
    void updateTurnLabel(int turnCount, int halfTurnCount);

    // Method to display a message to the user, such as notifications or alerts
    void showMessage(String message);
}
