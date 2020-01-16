package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import dannypiper.mazegenerator.MazeGen;
import dannypiper.mazegenerator.kuskals.Arc;
import dannypiper.mazegenerator.kuskals.ArcWeighted;

public class sortingAlgorithms {

	public static ArcWeighted [ ] bogoSort ( ArcWeighted [ ] data ) {
		System.out.println ( "BOGO SORT MAY NEVER TERMINATE, PLEASE BE WARNED. IT HAS COMLEXITY NxN!" );

		while ( ! sortingAlgorithms.isSorted ( data ) ) {
			data = sortingAlgorithms.randomise ( data );
		}

		return data;
	}

	// ascending order
	public static ArcWeighted [ ] bubbleSort ( final ArcWeighted [ ] data ) {
		boolean finished = false;

		while ( ! finished ) {
			finished = true;

			for ( int i = 0; i < data.length; i ++ ) {

				for ( int j = 0; j < data.length; j ++ ) {

					if ( ( data [ j ].weight < data [ i ].weight ) && ( i < j ) ) {
						final ArcWeighted temp = data [ j ];
						data [ j ] = data [ i ];
						data [ i ] = temp;
						finished = false;
					}

				}

			}

		}

		return data;
	}

	// ascending order
	public static List < Arc > countingSort ( final ArcWeighted [ ] data, final int maxRand ) {
		@SuppressWarnings ( "unchecked" )
		final Queue < Arc > [ ] categories = new LinkedList [ maxRand ];
		// weight : (z ∩ 0 < weight < maxrand)

		for ( int i = 0; i < MazeGen.maxRand; i ++ ) {
			categories [ i ] = new LinkedList <> ( );
		}

		for ( final ArcWeighted currentArc : data ) {
			categories [ currentArc.weight ].add ( currentArc );
		}

		final List < Arc > output = new LinkedList <> ( );

		for ( int i = 0; i < maxRand; i ++ ) {

			while ( ! categories [ i ].isEmpty ( ) ) {
				output.add ( categories [ i ].remove ( ) );
			}

		}

		return output;
	}

	// ascending order
	public static List < Arc > insertionSort ( final ArcWeighted [ ] data ) {
		final List < ArcWeighted > output = new LinkedList <> ( );

		output.set ( 0, data [ 0 ] ); // need an item to compare to.

		for ( int i = 1; i < data.length; i ++ ) {

			for ( int j = 0; j < i; j ++ ) {

				if ( output.get ( j ).weight <= data [ i ].weight ) {
					output.add ( j, data [ i ] );
					break;
				}

				if ( ( j + 1 ) == i ) {
					output.set ( j + 1, data [ i ] );
				}

			}

		}

		final List < Arc > outputTwo = new LinkedList <> ( );

		while ( ! output.isEmpty ( ) ) {
			outputTwo.add ( output.remove ( 0 ) );
		}

		return outputTwo;
	}

	private static boolean isSorted ( final ArcWeighted [ ] data ) {

		// Complexity O(n)
		for ( int i = 0; i < ( data.length - 1 ); i ++ ) {

			if ( data [ i ].weight > data [ i + 1 ].weight ) {
				return false;
			}

		}

		return true;
	}

	// Ascending order, recursive binary tree based quicksort.
	public static List < ArcWeighted > quickSort ( final List < ArcWeighted > data ) {
		List < ArcWeighted > a = new LinkedList <> ( );
		List < ArcWeighted > b = new LinkedList <> ( );
		final int Pivot = data.size ( ) / 2;

		if ( data.size ( ) <= 1 ) {
			return data;
		}

		final ArcWeighted PivotArc = data.get ( Pivot );
		final int PivotValue = data.get ( Pivot ).weight;

		while ( ! data.isEmpty ( ) ) {
			final ArcWeighted temp = data.remove ( 0 );

			if ( temp != PivotArc ) {

				if ( PivotValue <= temp.weight ) {
					a.add ( temp );
				}
				else {
					b.add ( temp );
				}

			}

		}

		// A binary tree like this is formed.
		// ========================//
		// pivotValue //
		// //
		// a b //
		// ========================//
		// a and b are sub-trees
		// (they may have no children and be laeves)

		// The data is placed into a tree and then exported in order.

		a = sortingAlgorithms.quickSort ( a ); // Call itself
		b = sortingAlgorithms.quickSort ( b ); // Call itself

		// merge
		a.add ( PivotArc );
		a.addAll ( b );

		return a;
		// All of the data is returned and merged (see code above) forming a sorted list
	}

	private static ArcWeighted [ ] randomise ( final ArcWeighted [ ] data ) {
		// Complexity O(n)
		final Random rand = new Random ( );

		for ( int i = 0; i < data.length; i ++ ) {
			// Swap data[i] with a random element
			final ArcWeighted temp = data [ i ];
			final int randomNumber = rand.nextInt ( data.length - 1 );

			data [ i ] = data [ randomNumber ];
			data [ randomNumber ] = temp;
		}

		return data;
	}

}
