import java.util.Scanner;
import java.util.List;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Random r = new Random();
		String guess = null;
		List<String> wordList = loadWordsFromFile("wordle-list.txt");

		if (wordList.isEmpty()) {
			System.out.println("No words found in the file.");
			return;

		}
		String target = wordList.get(r.nextInt(wordList.size())).toUpperCase().trim();
		System.out.println("Welcome to Wordle! Guess the 5-letter word.");
		System.out.println("THIS IS THE TARGET " + target);
		int attempts = 0;
		
		boolean guessedCorrectly = false;

		while (attempts < 6) {
			while (!"     ".equals(guess)) {
				System.out.print("Enter your guess: ");
				guess = scanner.nextLine().toUpperCase();
				if (guess.length() != 5) {
					System.out.println("YOUR ith GUESS"+"Please enter a 5-letter word.");
					continue;
				}
				char[] result = WordChecker.checkGuess(target, guess);
				if (new String(result).equals("GGGGG")) {
					System.out.println("Congratulations! You've guessed the word!");
					break;
				}

				System.out.println(result);

			}
			attempts++;
		}
		if (!guessedCorrectly) {
			System.out.println("Game over! The correct word was: " + target);
		}
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
