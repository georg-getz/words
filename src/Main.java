import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Main {
    private static final int TARGET_LENGTH = 9;

    public static void main(String[] args) throws IOException {
        Map<Integer, Set<String>> wordsByLength = loadWords();
        Map<Integer, Set<String>> validWords = new HashMap<>();
        validWords.put(1, Set.of("a", "i"));

        for (int len = 2; len <= TARGET_LENGTH; len++) {
            Set<String> currentWords = wordsByLength.get(len);
            Set<String> valid = new HashSet<>();
            Set<String> prevValid = validWords.get(len - 1);
            if (!prevValid.isEmpty()) {
                for (String word : currentWords) {
                    if (canReduce(word, prevValid)) {
                        valid.add(word);
                    }
                }
                validWords.put(len, valid);
            }
        }

        Set<String> result = validWords.getOrDefault(TARGET_LENGTH, Collections.emptySet());
        System.out.println("9-letter words reducible to 'a' or 'i':");
        result.stream()
                .sorted()
                .forEach(System.out::println);
        System.out.println("Total: " + result.size());
    }

    /**
     * Returns a map with key representing the number of letters and value representing
     * the corresponding words that contain at least 1 'a' or 'i'
     * Words over 9 letters are ignored
     */
    private static Map<Integer, Set<String>> loadWords() throws IOException {
        URL wordsList =
                new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(wordsList.openConnection().getInputStream()))) {
            Map<Integer, Set<String>> wordsByLength = new HashMap<>();
            reader.lines().skip(2).forEach(line -> {
                line = line.trim().toLowerCase();
                int len = line.length();
                if (len <= TARGET_LENGTH &&
                        (line.contains("a") || line.contains("i"))) {
                    wordsByLength
                            .computeIfAbsent(len, k -> new HashSet<>())
                            .add(line);
                }
            });

            return wordsByLength;
        }
    }

    /**
     * Returns true if removing one character from word yields any string in prevValid set.
     */
    private static boolean canReduce(String word, Set<String> prevValid) {
        int len = word.length();
        for (int i = 0; i < len; i++) {
            String shorter = word.substring(0, i) + word.substring(i + 1);
            if (prevValid.contains(shorter)) {
                return true;
            }
        }
        return false;
    }
}