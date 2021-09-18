import structure5.*;
import java.nio.file.*;
import java.util.Scanner;

/**
 * A class that tracks term frequencies (counts) for a single document.
 */
class TermFrequency {
    private Hashtable<String,Integer> _counts;

    /**
      * Opens the given file, and for each word in the file, converts
      * it to a normalized term, and counts it.
      *
      * @param file Path to a document.
      */
    public TermFrequency(Path file) {
      _counts = new Hashtable<String,Integer>();
      buildTable(file);
    }

    /*
     * This methods takes the given file path, reads in the words in the file
     * and constructs counts for each term in the file
     */
    public void buildTable(Path file){
      Scanner input = new Scanner(new FileStream(file.toString()));
      while (input.hasNext()){
        String word = Term.normalize( input.next() );
        Integer value = _counts.get(word);
        if(value==null) _counts.put(word,1);
        else{
          value+=1;
          _counts.remove(word);
          add(word,value);
        }
      }
    }

    /*
     * Builds an empty hashtable
     */
    public TermFrequency(){
      _counts = new Hashtable<String,Integer>();
    }

    /*
     * adds the given key-value association into the hashtable
     */
    public void add(String term, int count){
      _counts.put(term,count);
    }

    /*
     * returns true iff there are no entries in the hashtable
     */
    public boolean isEmpty() {
        return _counts.size() == 0;
    }

    /**
     * Computes the term frequency (TF_i) for term i in this document.
     *
     * @param term A string term.
     */
    public double tf(String term) {
        double high = mostFrequentTerm().getValue();
        double num = getCount(term);
        return (double) num/high;
    }

    /*
     * Returns true iff the given string is located in the hashtable
     */
    public boolean containsTerm(String term){
      return _counts.containsKey(term);
    }

    /**
     * Returns an association containing the most frequent term
     * along with its count.
     */
    public Association<String,Integer> mostFrequentTerm() {
      Set<Association<String,Integer>> entries = _counts.entrySet();
      int highest = 0;
      Association<String,Integer> freq = new Association<String,Integer>("",highest);
      for(Association<String,Integer> as : entries){
        if(as.getValue()>highest){
          highest = as.getValue();
          freq = as;
        }
      }
      return freq;
    }

    /**
     * Returns the count for a given term.
     *
     * @param term The given term.
     */
    public int getCount(String term) {
        if (_counts.get(term)==null)return 0;
        return _counts.get(term);
    }

    /**
     * Returns all of the stored terms as a set.
     */
    public Set<String> terms() {
      return _counts.keySet();
    }

    /*
     * Returns string representation of the hashtable
     */
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String doc : _counts.keySet()) {
        int count = _counts.get(doc);
          sb.append("\"" + doc + "\",");
          sb.append("\"" + count + "\",");
          sb.append("\n");
        }
      return sb.toString();
    }
}
