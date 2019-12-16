package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dannypiper.mazegenerator.MazeGen;
import dannypiper.mazegenerator.kuskals.Arc;
import dannypiper.mazegenerator.kuskals.ArcWeighted;

public class KruskalsSortManager {

	private final sortType typeOfSort;
	private final ArcWeighted [ ] arcsToSort;

	public KruskalsSortManager ( final sortType type, final ArcWeighted [ ] unsortedData ) {
		this.typeOfSort = type;
		this.arcsToSort = unsortedData;
	}

	private List < Arc > arrayToList ( final ArcWeighted [ ] data ) {
		final List < Arc > output = new LinkedList <> ( );

		for ( int i = 0; i < data.length; i ++ ) {
			output.add ( data [ i ] );
			data [ i ] = null;
		}

		return output;
	}

	private List < ArcWeighted > arrayToListWeighted ( final ArcWeighted [ ] data ) {
		final List < ArcWeighted > output = new LinkedList <> ( );

		for ( int i = 0; i < data.length; i ++ ) {
			output.add ( data [ i ] );
			data [ i ] = null;
		}

		return output;
	}

	private List < Arc > correctType ( final List < ArcWeighted > data ) {
		final List < Arc > output = new LinkedList <> ( );

		while ( ! data.isEmpty ( ) ) {
			output.add ( data.remove ( 0 ) );
		}

		return output;
	}

	@SuppressWarnings ( "unchecked" )
	public Queue < Arc > sortedData ( ) throws Exception {

		if ( this.typeOfSort != sortType.COUNTINGSORT ) {
			List < Arc > sortedData = null;

			switch ( this.typeOfSort ) {
				case BUBBLESORT :
					sortedData = this.arrayToList ( sortingAlgorithms.bubbleSort ( this.arcsToSort ) );
					break;

				case INSERTIONSORT :
					sortedData = sortingAlgorithms.insertionSort ( this.arcsToSort );
					break;

				case QUICKSORT :
					sortedData = this.correctType (
					        sortingAlgorithms.quickSort ( this.arrayToListWeighted ( this.arcsToSort ) ) );
					break;

				default :
					break;
			}

			return ( Queue < Arc > ) sortedData;
		}
		else {
			return ( Queue < Arc > ) sortingAlgorithms.countingSort ( this.arcsToSort, MazeGen.maxRand );
		}

	}
}
