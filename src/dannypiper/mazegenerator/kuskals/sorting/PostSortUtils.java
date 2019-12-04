package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;

import dannypiper.mazegenerator.kuskals.Arc;
import dannypiper.mazegenerator.kuskals.ArcWeighted;

public class PostSortUtils {

	public static List < Arc > arcWeightedListToArcList ( final List < ArcWeighted > listToConvert ) {
		final List < Arc > Data = new LinkedList <> ( );

		for ( final ArcWeighted currentArc : listToConvert ) {
			Data.add ( currentArc );
		}

		return Data;
	}
}
