package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dannypiper.mazegenerator.MazeGen;
import dannypiper.mazegenerator.kuskals.Arc;
import dannypiper.mazegenerator.kuskals.ArcWeighted;

//Quick sort multi-threaded - WIP
class QuickSortThread implements Runnable {

	public List < ArcWeighted > data;
	public boolean finished;

	public QuickSortThread ( final List < ArcWeighted > data ) {
		this.data = data;
		this.finished = false;
	}

	// Ascending order, recursive binary tree based quicksort.
	private List < ArcWeighted > quickSort ( final List < ArcWeighted > Data ) {
		List < ArcWeighted > a = new LinkedList <> ( );
		List < ArcWeighted > b = new LinkedList <> ( );
		final int Pivot = Data.size ( ) / 2;

		if ( Data.size ( ) <= 1 ) {
			return Data;
		}

		for ( int i = 0; i < Data.size ( ); i ++ ) {

			if ( i != Pivot ) {

				if ( Data.get ( Pivot ).weight <= Data.get ( i ).weight ) {
					a.add ( Data.get ( i ) );
				}
				else {
					b.add ( Data.get ( i ) );
				}

			}

		}

		final QuickSortThread aObj = new QuickSortThread ( a );
		final QuickSortThread bObj = new QuickSortThread ( b );

		( new Thread ( aObj ) ).start ( );
		( new Thread ( bObj ) ).start ( );

		while ( ! ( aObj.finished && bObj.finished ) ) {

		}

		a = aObj.data;
		b = bObj.data;

		// merge
		a.add ( Data.get ( Pivot ) );
		a.addAll ( b );

		return a;
	}

	@Override
	public void run ( ) {
		this.data = this.quickSort ( this.data );
		this.finished = true;
	}

}

public class sortingAlgorithms {

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

	// Ascending order, recursive binary tree based quicksort.
	public static List < ArcWeighted > quickSort ( final List < ArcWeighted > data ) {
		List < ArcWeighted > a = new LinkedList <> ( );
		List < ArcWeighted > b = new LinkedList <> ( );
		final int Pivot = data.size ( ) / 2;

		if ( data.size ( ) <= 1 ) {
			return data;
		}

		for ( int i = 0; i < data.size ( ); i ++ ) {

			if ( i != Pivot ) {

				if ( data.get ( Pivot ).weight <= data.get ( i ).weight ) {
					a.add ( data.get ( i ) );
				}
				else {
					b.add ( data.get ( i ) );
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
		a.add ( data.get ( Pivot ) );
		a.addAll ( b );

		return a;
		// All of the data is returned and merged (see code above) forming a sorted list
	}

	// WIP
	public static List < ArcWeighted > quickSortThreaded ( final List < ArcWeighted > data ) {

		final QuickSortThread a = new QuickSortThread ( data );
		( new Thread ( a ) ).start ( );

		while ( ! a.finished ) {

		}

		return a.data;
	}
}
