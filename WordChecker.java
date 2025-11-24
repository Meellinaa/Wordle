public class WordChecker {

    public static char[] checkGuess(String target, String guess) {
        if (target.length() != guess.length()) {
            throw new IllegalArgumentException("Target and guess must be the same length");
        }

        char[] result = new char[target.length()];
        boolean[] matchedInTarget = new boolean[target.length()];

        // First pass: check for correct position (green)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                result[i] = 'G';
                matchedInTarget[i] = true;
            }
        }

        // Second pass: check for correct letters in wrong positions (yellow)
        for (int i = 0; i < guess.length(); i++) {
            if (result[i] != 'G') {
                for (int j = 0; j < target.length(); j++) {
                    if (!matchedInTarget[j] && guess.charAt(i) == target.charAt(j)) {
                        result[i] = 'Y';
                        matchedInTarget[j] = true;
                        break;
                    }
                }
            }
        }

        // Third pass: mark the rest as incorrect (black)
        for (int i = 0; i < result.length; i++) {
            if (result[i] != 'G' && result[i] != 'Y') {
                result[i] = 'B';
            }
        }

        return result;
    }
}
