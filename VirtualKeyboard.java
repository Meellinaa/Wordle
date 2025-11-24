import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class VirtualKeyboard {
    private final Map<Character, JButton> keyboardButtons = new HashMap<>();
    private final JPanel keyboardPanel = new JPanel();

    // Interface for button click event handling
    public interface KeyboardButtonClickListener {
        void onButtonClick(char letter);
    }

    // Constructor with event listener and adding KeyListener for system keyboard
    public VirtualKeyboard(KeyboardButtonClickListener listener) {
        keyboardPanel.setLayout(new GridLayout(3, 1, 5, 5)); // 3 rows for the keyboard layout

        String[] rows = {
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        };
        
        // Setting up everything 
        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            for (char key : row.toCharArray()) {
                JButton keyButton = new JButton(String.valueOf(key));
                keyButton.setFocusable(false);
                keyButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size
                keyButton.setPreferredSize(new Dimension(40, 40));
                keyButton.setBackground(Color.LIGHT_GRAY);

                // Add action listener to each button
                keyButton.addActionListener(e -> listener.onButtonClick(key));

                keyboardButtons.put(key, keyButton);
                rowPanel.add(keyButton);
            }
            keyboardPanel.add(rowPanel);
        }

        // Add a KeyListener to capture system keyboard input (via focus on the JFrame)
        JFrame frame = new JFrame("Virtual Keyboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.add(keyboardPanel);
        frame.setVisible(true);
        
        // Focus to listen to key events
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (keyboardButtons.containsKey(keyChar)) {
                    // Handle the key press (same logic as button click)
                    listener.onButtonClick(keyChar);
                }
            }
        });
        frame.setFocusable(true);
        frame.requestFocusInWindow(); // Ensure the frame gets focus to capture key events
    }

    public JPanel getKeyboardPanel() {
        return keyboardPanel;
    }
    

    // Method to update the colors based on guesses
    public void updateKeyColors(String guess, char[] result) {
        for (int i = 0; i < guess.length(); i++) {
            char keyChar = guess.charAt(i);
            JButton keyButton = keyboardButtons.get(keyChar);
            if (keyButton != null) {
                switch (result[i]) {
                    case 'G': // Green for correct position
                        keyButton.setBackground(Color.GREEN);
                        break;
                    case 'Y': // Yellow for correct letter wrong position
                        keyButton.setBackground(Color.YELLOW);
                        break;
                    case 'B': // Black (or dark gray) for incorrect
                        keyButton.setBackground(Color.DARK_GRAY);
                        break;
                }
            }
        }
    }


    public void resetKeyColors() {
        for (JButton button : keyboardButtons.values()) {
            button.setBackground(Color.LIGHT_GRAY);
        }
    }
}
