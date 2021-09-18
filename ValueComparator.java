import structure5.*;
import java.util.Comparator;

public class ValueComparator implements Comparator<Association<String,Double>>{

  public int compare(Association<String,Double> a, Association<String,Double> b){
    return Double.compare(a.getValue(),b.getValue());
  }

}
