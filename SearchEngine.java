import structure5.*;
import java.nio.file.*;
import java.io.File;


/**
 * This is an implementation of a search engine using TF-IDF and hash tables.
 */
class SearchEngine {
  /**
   * This main method should allow a user to call this program as follows:
   * $ java SearchEngine "<query string>" <document folder path> <k>
   *   where
   *   <query string> is a search query, potentially composed of multiple terms,
   *   <document folder path> is a string representing the location of a
   *     document collection, and
   *   <k> is a positive integer representing how many documents to return.
   *
   * For example,
   * $ java SearchEngine "dog" ~/Desktop/ufo 5
   *   will return the 5 most relevant documents in the ~/Desktop/ufo folder
   *   for documents mentioning the word "dog".
   *
   * This method should first understand the user's arguments, extract the search
   * terms from the query, compute term counts for the documents in the given
   * path, then compute the TF-IDF score for each document given the query. Finally,
   * it should generate a list of documents, sorted by their TF-IDF score, and it
   * should print out the top k most relevant documents back to the user.
   *
   * @param args The command line argument array.
   */
  public static void main(String[] args) {
    //taking in arguments of the user
    String query="";
    String path = "";
    int num = 0;
    try{
      query = args[0];
      path = args[1];
      num = Integer.parseInt(args[2]);
    }
    catch (ArrayIndexOutOfBoundsException e){//not enough input
      System.out.println("Enter a search query, the path, and the number of Documents to return!");
      System.exit(0);
   }

    Assert.pre(num>0,"The number cannot be negative!");

    Vector<String> theQuery = Term.toTerms(query);//normalized terms
    Table table = new Table(Paths.get(path));

    Vector<Association<String, Double>> result = table.topK(theQuery,num);//relevant documents according to the query
    for(Association<String, Double> s : result) System.out.println(s);
  }
}
