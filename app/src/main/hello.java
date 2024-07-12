import java.util.ArrayList;
import java.util.Collections;

public class hello {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, 1, 2, 3,4, 5,6 ,7);

        System.out.println(arrayList.subList(0, 5));
        System.out.println(arrayList.get(6));

        
        //System.out.println(arrayList.get(0));
    }
}
