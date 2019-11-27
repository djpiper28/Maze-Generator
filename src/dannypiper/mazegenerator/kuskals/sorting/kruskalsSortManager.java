package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.arc;
import dannypiper.mazegenerator.kuskals.arcWeighted;

public class kruskalsSortManager {

	public sortType type;
	public arcWeighted [ ] data;

	public kruskalsSortManager ( final sortType type, final arcWeighted [ ] unsortedData ) {
		this.type = type;
		this.data = unsortedData;
	}

	private List < arc > arrayToList ( final arcWeighted [ ] data ) {
		final List < arc > output = new LinkedList < > ( );

		for ( int i = 0; i < data.length; i ++ ) {
			output.add ( data [ i ] );
			data [ i ] = null;
		}

		return output;
	}

	private List < arcWeighted > arrayToListWeighted ( final arcWeighted [ ] data ) {
		final List < arcWeighted > output = new LinkedList < > ( );

		for ( int i = 0; i < data.length; i ++ ) {
			output.add ( data [ i ] );
			data [ i ] = null;
		}

		return output;
	}

	private List < arc > correctType ( final List < arcWeighted > data ) {
		final List < arc > output = new LinkedList < > ( );

		while ( ! data.isEmpty ( ) ) {
			output.add ( data.remove ( 0 ) );
		}

		return output;
	}

	public Queue < arc > sortedData ( ) throws Exception {

		if ( this.type != sortType.countingSort ) {
			List < arc > sortedData = new LinkedList < > ( );

			switch ( this.type ) {
				case countingSort :
					System.out.println ( "ERROR at line 25" );
					throw new Exception ( "Assertion error at line 25" );

				case bubbleSort :
					sortedData = this.arrayToList ( sortingAlgorithms.bubbleSort ( this.data ) );
					break;

				case insersionSort :
					sortedData = sortingAlgorithms.insertionSort ( this.data );
					break;

				case quickSort :
					sortedData = this
					        .correctType ( sortingAlgorithms.quickSort ( this.arrayToListWeighted ( this.data ) ) );
					break;

				default :
					System.out.println ( "ERROR at line 36" );
					throw new Exception ( "Assertion error at line 36" );
			}

			return ( Queue < arc > ) sortedData;
		}
		else {
			return ( Queue < arc > ) sortingAlgorithms.countingSort ( this.data, mazegen.maxRand );
		}

	}
}
