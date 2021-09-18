import structure5.*;
import java.util.Scanner;

/**
 * A class that contains some static helper methods for working
 * with terms.
 */
class Term {

    /**
     * Converts a query string into a normalized term array.
     *
     * @param query Query string.
     */
    public static Vector<String> toTerms(String query) {
        Vector<String> terms = new Vector<>();
        Scanner input = new Scanner(query);
        while(input.hasNext()){
          terms.add( normalize(input.next()) );
        }
        return terms;
    }

    /**
     * Returns a normalized a word by making the given word
     * lowercase and by removing all punctuation.
     *
     * @param word An unprocessed word.
     */
    public static String normalize(String word) {
        word = word.toLowerCase();
        for (int i = 0; i<word.length();i++)
          if(!Character.isLetter(word.charAt(i))) {
            word = word.replace(String.valueOf(word.charAt(i))," ");
          }
        return word.trim().replaceAll("\\s+", "");
    }

}
