import structure5.*;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;

/*
 * This class is similar to the Search Engine class. The only difference is that
 * before running a certain query, it checks to see whether there is a cached version
 * of a Table. If there is one, it reads the content of that file and creates a Table from
 * that. Otherwise, it creates a Table from the given path, and creates a cached .csv version
 * of the Table to be used when another query is run.
 */
public class CachedSearchEngine extends SearchEngine{

  public static void main(String[] args) {
    //getting arguments of the user
    String query="";
    String path = "";
    int num = 0;
    try{
      query = args[0];
      path = args[1];
      num = Integer.parseInt(args[2]);
    }
    catch (ArrayIndexOutOfBoundsException e){
      System.out.println("Enter a search query, the path, and the number of Documents to return!");
      System.exit(0);
   }
    Assert.pre(num>0,"The number cannot be negative!");

    Vector<String> theQuery = Term.toTerms(query);//normalized terms
    Table table = new Table();
    File tmpDir = new File("database.csv");
    if(!tmpDir.exists()){//if there is no cached Table csv file
      table = new Table(Paths.get(path));
         try{//create a cached csv file of the table
            FileWriter myWriter = new FileWriter("database.csv");
            myWriter.write(table.toString());
            myWriter.close();
         } catch(IOException e){
            System.out.print(e);
         }

   }else{//if there is a cached file, read that file and create a table from it
     table = table.readFromFile(tmpDir.toPath());
   }


    Vector<Association<String, Double>> result = table.topK(theQuery,num);//documents returned according to the query
    for(Association<String, Double> s : result) System.out.println(s);
  }
}
