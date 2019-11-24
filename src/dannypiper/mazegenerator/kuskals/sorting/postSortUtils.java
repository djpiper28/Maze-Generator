package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;

import dannypiper.mazegenerator.kuskals.arc;
import dannypiper.mazegenerator.kuskals.arcWeighted;

public class postSortUtils {
	
	public static List<arc> arcWeightedListToArcList(List<arcWeighted> listToConvert){
		List<arc> Data = new LinkedList<arc>();
		
		for(arcWeighted currentArc: listToConvert) {
			Data.add(currentArc);
		}
		
		return Data;
	}
}
