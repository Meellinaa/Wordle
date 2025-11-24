import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class WordleGUI {
	private JFrame frame;
	private JTextField guessField;
	private JTextArea resultArea;
	private VirtualKeyboard virtualKeyboard;
	private String target;
	private int attempts;
	private List<String> wordList;
	private JPanel guessDisplayPanel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			WordleGUI gui = new WordleGUI();
			gui.createAndShowGUI();
		});
	}

	public WordleGUI() {
		wordList = loadWordsFromFile("wordle-list.txt");
		if (wordList.isEmpty()) {
			System.out.println("No words found in the file.");
			return;
		}
		target = getRandomWord();
		attempts = 0;
	}

	public void createAndShowGUI() {

		frame = new JFrame("Wordle Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());

		guessField = new JTextField(5);
		guessField.setFont(new Font("Arial", Font.PLAIN, 20));
		guessField.setHorizontalAlignment(JTextField.CENTER);
		guessField.setEditable(false);
		frame.add(guessField, BorderLayout.NORTH);

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		resultArea.setText("Welcome to Wordle! Guess the 5-letter word.");
		frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

		virtualKeyboard = new VirtualKeyboard(letter -> onKeyboardButtonClick(letter));
		frame.add(virtualKeyboard.getKeyboardPanel(), BorderLayout.SOUTH);
		// Create a panel to hold both buttons with spacing
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.DARK_GRAY);

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		resultArea.setBackground(Color.BLACK);
		resultArea.setForeground(Color.GREEN);
		resultArea.setText("Welcome to Wordle! Guess the 5-letter word.");
		resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.add(new JScrollPane(resultArea), BorderLayout.NORTH);

		guessDisplayPanel = new JPanel();
		guessDisplayPanel.setLayout(new GridLayout(6, 1, 5, 5)); // space between guesses
		guessDisplayPanel.setBackground(Color.DARK_GRAY);
		guessDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.add(guessDisplayPanel, BorderLayout.CENTER);

		frame.add(centerPanel, BorderLayout.CENTER);
		

		guessField.setEditable(true);

		guessField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = Character.toUpperCase(e.getKeyChar());
				String currentText = guessField.getText();

				// Allow only letters, max 5 chars
				if (Character.isLetter(c) && currentText.length() < 5) {
					guessField.setText(currentText + c);
				}

				// If Enter is pressed, trigger submission
				if (c == '\n') {
					onSubmitButtonClick();
				}

				// Prevent non-letter or extra input
				e.consume();
			}

		});
		guessField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String currentText = guessField.getText();
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !currentText.isEmpty()) {
					guessField.setText(currentText.substring(0, currentText.length() - 1));
				} else if (Character.isLetter(e.getKeyChar()) && currentText.length() < 5) {
					guessField.setText(currentText + Character.toUpperCase(e.getKeyChar()));
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					onSubmitButtonClick();
				}
				e.consume();
			}
		});
//	        JPanel guessDisplayPanel;
//	        guessDisplayPanel = new JPanel();
//	        guessDisplayPanel.setLayout(new BoxLayout(guessDisplayPanel, BoxLayout.Y_AXIS));
//	        frame.add(guessDisplayPanel, BorderLayout.CENTER);

//	        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
//	                new JScrollPane(resultArea), guessDisplayPanel);
//	        splitPane.setResizeWeight(0.3);  // 30% for result area
//	        frame.add(splitPane, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	private void onKeyboardButtonClick(char letter) {
		String currentText = guessField.getText();
		if (currentText.length() < 5) {
			guessField.setText(currentText + letter);
		}
	}

	private void onSubmitButtonClick() {
		String guess = guessField.getText().toUpperCase();
		if (guess.length() != 5) {
			resultArea.setText("Please enter a 5-letter word.");
			return;
		}

		char[] result = WordChecker.checkGuess(target, guess);
		resultArea.append("\n" + guess + " -> " + new String(result));
		JLabel guessLabel = new JLabel(guess + " -> " + new String(result));
		guessLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
		guessDisplayPanel.add(guessLabel);
		guessDisplayPanel.revalidate();

		virtualKeyboard.updateKeyColors(guess, result);
		updateGuessFieldColor(result);

		if (new String(result).equals("GGGGG")) {
			resultArea.append("\nCongratulations! You've guessed the word!");
			return;
		}

		attempts++;
		if (attempts >= 6) {
			resultArea.append("\nSorry! You've used all attempts. The word was: " + target);
		}

		guessField.setText("");
	}

	private void updateGuessFieldColor(char[] result) {
		// This could be enhanced to show per-letter colors in a custom grid
		if (new String(result).equals("GGGGG")) {
			guessField.setBackground(Color.GREEN);
		} else {
			guessField.setBackground(Color.WHITE); // Reset to neutral
		}
	}

	private void resetGame() {
		guessField.setText("");
		resultArea.setText("Welcome to Wordle! Guess the 5-letter word.");
		virtualKeyboard.resetKeyColors();
		attempts = 0;
		target = getRandomWord();

		// Clear the guess display panel
		guessDisplayPanel.removeAll();
		guessDisplayPanel.revalidate();
		guessDisplayPanel.repaint();
	}

	private String getRandomWord() {
		return wordList.get(new Random().nextInt(wordList.size())).toUpperCase().trim();
	}

	private static List<String> loadWordsFromFile(String fileName) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			content = content.replace("[", "").replace("]", "").replace("\"", "");
			String[] wordsArray = content.split(",");
			return List.of(wordsArray);
		} catch (IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}
}
