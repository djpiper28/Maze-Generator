package dannypiper.mazegenerator.kuskals;

import java.util.LinkedList;
import java.util.Queue;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.sorting.kruskalsSortManager;
import dannypiper.mazegenerator.kuskals.sorting.sortType;

public class kruskals implements Runnable {

	private Queue < arc > sortedData;
	private final sortType type;
	private int [ ] disjointSet;

	public kruskals ( final sortType type ) {
		this.type = type;
	}

	private void executeKruskals ( ) {
		int nodesFound = 0;
		final int nodesNeeded = ( mazegen.width * mazegen.height ) - 1;
		long frameControlTime = System.currentTimeMillis ( );

		while ( nodesFound < nodesNeeded ) {
			final arc Arc = this.sortedData.remove ( );

			if ( this.union ( Arc ) ) {
				mazegen.drawArc ( Arc.startingNode, Arc.endingNode );
				nodesFound ++ ;
			}

			if ( ( System.currentTimeMillis ( ) - frameControlTime ) >= mazegen.frameRate ) {
				frameControlTime = System.currentTimeMillis ( );
				mazegen.render ( );
				final double percentage = ( double ) nodesFound / ( double ) nodesNeeded;
				gui.setProgress ( percentage );
			}

		}

	}

	private int find ( int v ) {
		final Queue < Integer > queue = new LinkedList < > ( );

		while ( this.disjointSet [ v ] >= 0 ) {
			queue.add ( v );
			v = this.disjointSet [ v ];
		}

		while ( ! queue.isEmpty ( ) ) {
			this.disjointSet [ queue.remove ( ) ] = v;
		}

		return v;
	}

	private arcWeighted [ ] generateArcs ( final short width, final short height ) {
		final arcWeighted [ ] data = new arcWeighted [ 2
		        * ( ( width * ( height - 1 ) ) + ( height * ( width - 1 ) ) ) ];
		int i = 0;

		for ( int x = 0; x < width; x ++ ) {

			for ( int y = 0; y < height; y ++ ) {
				final int Coord = ( width * y ) + x;

				if ( x < ( width - 1 ) ) {
					data [ i ] = new arcWeighted ( Coord, Coord + 1 );
					i ++ ;
				}

				if ( x > 0 ) {
					data [ i ] = new arcWeighted ( Coord, Coord - 1 );
					i ++ ;
				}

				if ( y < ( height - 1 ) ) {
					data [ i ] = new arcWeighted ( Coord, Coord + width );
					i ++ ;
				}

				if ( y > 0 ) {
					data [ i ] = new arcWeighted ( Coord, Coord - width );
					i ++ ;
				}

			}

		}

		return data;
	}

	private void initNodesVisited ( ) {
		this.disjointSet = new int [ mazegen.max ];

		for ( int i = 0; i < mazegen.max; i ++ ) {
			this.disjointSet [ i ] = - 1;
		}

	}

	@Override
	public void run ( ) {
		long time = System.currentTimeMillis ( );
		System.out.println ( "Generating arcs..." );
		final arcWeighted [ ] unsortedData = this.generateArcs ( ( short ) mazegen.width, ( short ) mazegen.height );
		System.out.println ( "Generated in " + ( System.currentTimeMillis ( ) - time ) + "ms" );

		try {
			time = System.currentTimeMillis ( );
			System.out.println ( "Sorting..." );
			this.sortData ( unsortedData, this.type );
			System.out.println ( "Sorted in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
			assert ( this.sortedData.size ( ) != 0 );
			this.initNodesVisited ( );
			this.executeKruskals ( );
		}
		catch ( final Exception e ) {
			e.printStackTrace ( );
		}

	}

	private void sortData ( final arcWeighted [ ] unsortedData, final sortType type ) throws Exception {
		final kruskalsSortManager sortManager = new kruskalsSortManager ( type, unsortedData );
		this.sortedData = sortManager.sortedData ( );
	}

	private boolean union ( final arc a ) {
		final int r0 = this.find ( a.startingNode );
		final int r1 = this.find ( a.endingNode );

		if ( r0 == r1 ) {
			return false;
		}

		if ( this.disjointSet [ r0 ] < this.disjointSet [ r1 ] ) {
			this.disjointSet [ r1 ] = r0;
		}
		else {

			if ( this.disjointSet [ r1 ] < this.disjointSet [ r0 ] ) {
				this.disjointSet [ r0 ] = r1;
			}
			else {
				this.disjointSet [ r1 ] = r0;
				this.disjointSet [ r0 ] -= 1;
			}

		}

		return true;
	}
}
