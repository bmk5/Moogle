import structure5.*;
import java.nio.file.*;
import java.lang.Math;
import java.util.Comparator;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class stores key-value pairs of documents and the terms and the term counts
 * The document serves as the key of the association while the terms in the document
 * along with their counts collectively form a TermFrequency that serves as the value
 * of the association.
 */

class Table {
    private Hashtable<String, TermFrequency> _table;

    /**
     * Build term frequency table for all documents in path. Searches for
     * documents recursively.
     *
     * @param dir Document path.
     */
    public Table(Path dir) {
        _table = new Hashtable<String,TermFrequency>();
        try {
         Path[] paths = Files.walk(dir).toArray(Path[]::new);
         for (Path path : paths){
             if(!path.toFile().isDirectory()){
               add(path.toString(),new TermFrequency(path));
             }
           }
        } catch (IOException e) {System.err.println(e.getMessage());}
    }

    /*
     * creates an empty hashtable
     */
    public Table(){
      _table = new Hashtable<String,TermFrequency>();
    }

    /*
     * adds the given key-value association into the hashtable
     */
    public void add(String doc, TermFrequency freq){
      _table.put(doc,freq);
    }
    /**
     * Compute inverse document frequency (IDF) for term across a corpus.
     *
     * @param term A string term.
     */
    public double idf(String term) {
      double total = _table.size();
      double docs = 1;
      for(String file : _table.keySet())
        if ( _table.get(file).containsTerm(term)) docs+=1;
      double div = total/docs;
      return (double)(Math.log(div) / Math.log(2) );
    }

    /**
     * Compute the TF-IDF score for a collection of documents and
     * a given search term.
     *
     * @param term A search term.
     */
    public Hashtable<String, Double> tfidf(String term) {
        Hashtable<String, Double> tfidf = new Hashtable<>();
        for(String doc : _table.keySet()) {
          tfidf.put(doc, (double)(_table.get(doc).tf(term) * idf(term)));
        }
        return tfidf;
    }

    /**
     * Computes the cumulative TF-IDF score for each document with
     * respect to a given query.
     6 *
     * @param query A vector of search terms.
     */
    public Hashtable<String, Double> score(Vector<String> query) {
      Hashtable<String, Double> score = new Hashtable<>();
      for(String doc : _table.keySet()){
        double sum = 0.0d;
        for(String term : query) sum+=tfidf(term).get(doc);
        score.put(doc,sum);
      }
      return score;
    }

    /**
     * Returns the top K documents.
     *
     * @param doc_tfidf A map from documents to their cumulative TF-IDF score.
     * @param k The number of documents to return.
     */
    public Vector<Association<String, Double>> topK(Vector<String> query, int k) {
        Vector<Association<String, Double>> result = new Vector<>();
        Hashtable<String,Double> docs = score(query);
        for (Association<String,Double> assc : docs.entrySet()) result.add(assc);
        return sort(k,result,new ValueComparator());
    }

    /*
     * this method takes a Vector that is to be sorted as a parameter, as well as
     * a Comparator used to sort the values of the Associations. It also takes an integer
     * that specifies the number of documents that are to be in the Vector.
     */
    public static <E> Vector<E> sort(int k,Vector<E> query, Comparator<E> c){
      int numSort = 1;//number of values in place
      int index;
      while (numSort<query.size()) {
      E temp = query.get(numSort);//take the first unsorted value
           for(index = numSort; index>0; index--){
             if (c.compare(temp,query.get(index-1)) > 0) {
               query.set(index, query.get(index-1));
             } else break;
           }
           query.set(index,temp);
           numSort++;
         }
      query.setSize(k); //setting the size of the vector to the number of docs to be returned
      return query;
    }

    /*
     * this method takes a path to a csv file. The file is a cached version
     * of a previously constructed Table. It reads the contents of that Table,
     * creates a new Table given those contents, and returns the newly constructed table
     */
    public Table readFromFile(Path dir){
      Scanner input = new Scanner(new FileStream(dir.toString()));
      Table myTable = new Table();
      while (input.hasNext()){
        String[] values = input.nextLine().split(",");
        String docs = values[0];//key of the Table
        docs = docs.substring(docs.indexOf('"')+1,docs.indexOf('"',docs.indexOf('"')+1));
        String tf = values[1];//key of TermFrequency
        tf =  tf.substring(tf.indexOf('"')+1,tf.indexOf('"',tf.indexOf('"')+1));
        int count = Integer.parseInt(values[2]);//value of TermFrequency
        build(docs,tf,count,myTable);
      }
      return myTable;
    }

    /*
     * this method is a helper to the readFromFile() function. It takes a string that is the key of
     * of the hashtable, and another String and int, which together form a TermFrequency. It also takes
     * a current version of the Table.
     */
    public Table build(String file,String term, int count, Table myTable){
      if(myTable.contains(file)){//if the hashtable aready contains the given key
        TermFrequency fr = myTable.getTerms(file);
        fr.add(term,count);//update the TermFrequency
      }else{//not in the Table
        TermFrequency fr = new TermFrequency();
        fr.add(term,count);
        myTable.add(file,fr);
      }
      return myTable;
    }

    /*
     * returns true iff the given key is located in the hashtable
     */
    public boolean contains(String doc){
      return _table.containsKey(doc);
    }

    /*
     * returns the TermFrequency association with the given key
     */
    public TermFrequency getTerms(String file){
      return _table.get(file);
    }
    /**
     * Outputs frequency table in CSV format.  Useful for
     * debuggging.
     *
     * @param table A frequency table for the entire corpus.
     */
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String doc : _table.keySet()) {
        TermFrequency docFreqs = _table.get(doc);
        for (String term : docFreqs.terms()) {
          sb.append("\"" + doc + "\",");
          sb.append("\"" + term + "\",");
          sb.append(docFreqs.getCount(term));
          sb.append("\n");
        }
      }
      return sb.toString();
    }
}
