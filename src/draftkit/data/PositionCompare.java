package draftkit.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Devon Maguire
 */
public class PositionCompare implements Comparator<String> {
    
    HashMap<String,Integer> positionCompare = new HashMap<>();
    
    public PositionCompare () {
        positionCompare.put("C", 1);
        positionCompare.put("1B", 2);
        positionCompare.put("CI", 3);
        positionCompare.put("3B", 4);
        positionCompare.put("2B", 5);
        positionCompare.put("MI", 6);
        positionCompare.put("SS", 7);
        positionCompare.put("OF", 8);
        positionCompare.put("U", 9);
        positionCompare.put("P", 10);
    }
    
    @Override
    public int compare(String p1, String p2) {
        if (positionCompare.get(p1) > positionCompare.get(p2))
            return 1;
        else if (Objects.equals(positionCompare.get(p1), positionCompare.get(p2)))
            return 0;
        else
            return -1;
    }
    
}
