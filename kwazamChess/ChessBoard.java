/* NURAFRINA BATRISYIA BINTI NORDZAMAN	1231303327@student.mmu.edu.my
ADRIANA BINTI MEOR AZMAN	1211111079@student.mmu.edu.my
NURULAIN AFIQAH BINTI ABDULLAH	1211112326@student.mmu.edu.my
NUR ADIBAH BINTI KHAIRUL ANUAR	1211112286@student.mmu.edu.my */

import java.awt.*; // Importing AWT classes for GUI components
import java.awt.event.ActionEvent; // Importing ActionEvent for handling button actions
import java.awt.event.ActionListener; // Importing ActionListener interface
import java.util.HashMap; // Importing HashMap for storing piece icons
import java.util.List; // Importing List for valid moves
import java.util.Map; // Importing Map for storing piece icons
import javax.swing.*; // Importing BufferedImage for image manipulation
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;

// Main class for the chessboard GUI
public class ChessBoard extends JFrame implements GameObserver {

    // 2D array of buttons representing the chessboard cells
    private JButton[][] cells = new JButton[8][5];
    private ChessController controller; // Reference to the controller
    private JLabel turnLabel; // Label to display the current player's turn
    private JLabel turnCountLabel; // Label to display the round count
    public static Map<String, ImageIcon> pieceIcons = new HashMap<>(); // // Static map for piece icons
    private JButton restartButton; // Button to restart the game
    private JButton saveButton; // Button to save the game state
    private boolean flip = false; // Add this class field
    private Clip introClip; // Add a class field to hold the intro clip

    // Getter method for the piece icons
    public static Map<String, ImageIcon> getPieceIcons() { // Getter method for the icons
        return pieceIcons;
    }

    // Constructor for the ChessBoard class
    public ChessBoard(ChessController controller) {
        this.controller = controller; // Assigning the controller
        setTitle("Kwazam Chess"); // Setting the title of the window
        setSize(700, 700); // Setting the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        setLayout(new BorderLayout()); // Setting layout manager

        // Play intro sound immediately
        new Thread(() -> playSound("audio/Intro.wav")).start();

        // Create the front screen panel with background image
        JPanel frontScreenPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("img/BackgroundIntro.jpg");
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        frontScreenPanel.setBackground(new Color(50, 50, 50)); // Dark background color

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false); // Make button panel transparent
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(400, 0, 0, 0)); // Move buttons up

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 18)); // Smaller font size
        newGameButton.setBackground(new Color(70, 130, 180)); // Steel blue background
        newGameButton.setForeground(Color.WHITE); // White text color
        newGameButton.setFocusPainted(false);
        newGameButton.setPreferredSize(new Dimension(150, 50)); // Smaller button size
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopIntroSound(); // Stop the intro sound
                remove(frontScreenPanel);
                initializeGameComponents();
                controller.restartGame();
            }
        });
        buttonPanel.add(newGameButton);

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setFont(new Font("Arial", Font.BOLD, 18)); // Smaller font size
        loadGameButton.setBackground(new Color(34, 139, 34)); // Forest green background
        loadGameButton.setForeground(Color.WHITE); // White text color
        loadGameButton.setFocusPainted(false);
        loadGameButton.setPreferredSize(new Dimension(150, 50)); // Smaller button size
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopIntroSound(); // Stop the intro sound
                remove(frontScreenPanel);
                initializeGameComponents();
                controller.loadGame();
            }
        });
        buttonPanel.add(loadGameButton);

        frontScreenPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(frontScreenPanel, BorderLayout.CENTER);
    }

    private void playSound(String soundFile) {
        try {
            File audioFile = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat baseFormat = audioStream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat);
            introClip = (Clip) AudioSystem.getLine(info);
            introClip.open(decodedStream);
            introClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopIntroSound() {
        if (introClip != null && introClip.isRunning()) {
            introClip.stop();
            introClip.close();
        }
    }

    // Method to initialize game components after front screen
    private void initializeGameComponents() {
        // Initializing the turn label
        turnLabel = new JLabel("Round: Red");
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centering the label
        add(turnLabel, BorderLayout.NORTH); // Adding label to the north of the layout

        // Initializing the turn count label
        turnCountLabel = new JLabel("Round Count: 0");
        turnCountLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centering the label
        add(turnCountLabel, BorderLayout.SOUTH); // Adding label to the south of the layout

        // Creating the board panel with a grid layout
        JPanel boardPanel = new JPanel(new GridLayout(8, 5));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                JButton cell = new JButton(); // Creating a button for each cell
                cell.setFont(new Font("Arial", Font.BOLD, 20)); // Setting font for the button
                cell.setBackground(Color.WHITE); // Setting background color
                cell.setFocusPainted(false); // Removing focus painting
                cells[row][col] = cell; // Storing the button in the cells array

                // Anonymous inner class for action listener
                // Adding action listener to handle cell clicks
                int r = row; // Final variable for inner class
                int c = col; // Final variable for inner class
                cell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.handleCellClick(r, c); // Notify controller of the clicked cell
                    }
                });
                boardPanel.add(cell); // Adding the button to the board panel
            }
        }
        add(boardPanel, BorderLayout.CENTER); // Adding the board panel to the center of the layout

        loadPieceIcons();  // Call the icons for the pieces after boardPanel is fully initialized.

        // Initializing the restart button
        restartButton = new JButton("Restart");
        restartButton.setBackground(Color.RED); // Setting background color
        restartButton.setForeground(Color.WHITE); // Setting text color
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restartGame(); // Notify controller to restart the game
            }
        });

        // Initializing the save button
        saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN); // Setting background color
        saveButton.setForeground(Color.WHITE); // Setting text color
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveGame(); // Notify controller to save the game
            }
        });

        // Creating a panel for buttons and adding them
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton); // Adding restart button
        buttonPanel.add(saveButton); // Adding save button
        add(buttonPanel, BorderLayout.SOUTH); // Adding button panel to the south of the layout

        controller.addObserver(this); // Notifying the controller that this view is observing it
        updateBoard(controller.getBoard()); // updateBoard should be called after loadGame
    }

    // Method to load piece icons from files
    private void loadPieceIcons() {
        pieceIcons.put("RedBiz", new ImageIcon(new ImageIcon("img\\RedBiz.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueBiz", new ImageIcon(new ImageIcon("img\\BlueBiz.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("RedRam", new ImageIcon(new ImageIcon("img\\RedRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueRam", new ImageIcon(new ImageIcon("img\\BlueRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("RedSau", new ImageIcon(new ImageIcon("img\\RedSau.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueSau", new ImageIcon(new ImageIcon("img\\BlueSau.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("RedTor", new ImageIcon(new ImageIcon("img\\TorRed.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueTor", new ImageIcon(new ImageIcon("img\\TorBlue.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("RedXor", new ImageIcon(new ImageIcon("img\\XorRed.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueXor", new ImageIcon(new ImageIcon("img\\XorBlue.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("RedRamUpgrade", new ImageIcon(new ImageIcon("img\\RedRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("BlueRamUpgrade", new ImageIcon(new ImageIcon("img\\BlueRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedRedRam", new ImageIcon(new ImageIcon("img\\FlippedRedRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedBlueRam", new ImageIcon(new ImageIcon("img\\FlippedBlueRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedRedSau", new ImageIcon(new ImageIcon("img\\FlippedRedSau.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedBlueSau", new ImageIcon(new ImageIcon("img\\FlippedBlueSau.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedRedRamUpgrade", new ImageIcon(new ImageIcon("img\\FlippedRedRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        pieceIcons.put("FlippedBlueRamUpgrade", new ImageIcon(new ImageIcon("img\\FlippedBlueRam.PNG").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
    }

    // Method to update the board display
    @Override
    public void updateBoard(Piece[][] board) {
        flip = controller.getHalfTurnCount() % 2 == 1; // Set flip based on halfTurnCount

        Piece[][] boardToDisplay = board;
        if (flip) {
            boardToDisplay = getFlippedBoard(board);
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) { // Fix the loop condition
                cells[row][col].setIcon(null);  // Clear icon first
                cells[row][col].setText(""); // Clear any text

                // Set the background color for the cells
                Color cellColor = (row + col) % 2 == 0 ? new Color(238, 238, 210) : new Color(118, 150, 86); // Light/Dark Brown
                cells[row][col].setBackground(cellColor); // Apply the background color

                if (boardToDisplay[row][col] != null) {
                    ImageIcon icon = getImageIcon(boardToDisplay[row][col], flip);
                    //cells[row][col].setIcon(board[row][col].getImageIcon()); // Use polymorphism
                    cells[row][col].setIcon(icon); // Use polymorphism & Set the icon for the piece
                }
            }
        }
    }

    // Method to retrieve the appropriate image icon for a given piece
    private ImageIcon getImageIcon(Piece piece, boolean flipped) {
        String color = piece.getColor(); // Get the color of the piece (e.g., "Red" or "Blue")
        String pieceName = piece.getClass().getSimpleName(); // Get the simple name of the piece class (e.g., "Ram", "Sau")
        // Create a key for the piece icon based on its color and name
        String key = color + pieceName; // Example: "RedRam", "BlueSau"

        // Check if the piece should be flipped
        if (flipped && (pieceName.equals("Ram") || pieceName.equals("Sau") || pieceName.equals("RamUpgrade"))) {
            key = "Flipped" + key; // Modify the key to retrieve the flipped version of the icon, Example: "FlippedRedRam"
        }

        // Return the corresponding ImageIcon from the pieceIcons map using the generated key
        return pieceIcons.get(key);
    }

    // Method to get the flipped board for the current turn
    private Piece[][] getFlippedBoard(Piece[][] board) {
        Piece[][] flippedBoard = new Piece[8][5];  // Create a new board for the flipped state
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) { // Fix the loop condition
                flippedBoard[7 - row][4 - col] = board[row][col]; // Flip the pieces
            }
        }
        return flippedBoard;
    }

    // Highlight valid moves for the selected piece
    public void highlightMoves(List<int[]> moves) {
        clearHighlights(); // Clear previous highlights
        for (int[] move : moves) {
            int row = move[0];
            int col = move[1];
            if (flip) {
                row = 7 - row;
                col = 4 - col;
            }
            cells[row][col].setBackground(Color.YELLOW); // Highlight valid moves
        }
    }

    // Clear all highlighted cells
    public void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) { // Fix the loop condition
                cells[row][col].setBackground((row + col) % 2 == 0 ? new Color(238, 238, 210) : new Color(118, 150, 86)); // Restore alternating colors
            }
        }
    }

    // Update the round label to show the current round and turn count
    @Override
    public void updateTurnLabel(int turnCount, int halfTurnCount) {
        String teamTurn = (halfTurnCount % 2 == 0) ? "Red" : "Blue"; // Determine current player's turn
        turnLabel.setText("Round: " + turnCount + "                " + teamTurn + " turn"); // Update turn label
        turnCountLabel.setText("Round Count: " + turnCount); // Update turn count label
    }

    // Method to show a message dialog
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message); // Display a message dialog
    }

    // Main method to run the application
    public static void main(String[] args) {
        ChessController controller = new ChessController(); // Create a new controller
        SwingUtilities.invokeLater(() -> new ChessBoard(controller).setVisible(true)); // Run the GUI
    }

    public boolean isFlipped() {
        return flip;
    }

}
