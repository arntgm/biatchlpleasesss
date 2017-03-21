package utils;

import java.util.Comparator;
import java.util.Set;

public class SizeComparator implements Comparator<Set<?>>{

	    @Override
	    public int compare(Set<?> o1, Set<?> o2) {
	        return Integer.valueOf(o1.size()).compareTo(o2.size());
	    }
}
