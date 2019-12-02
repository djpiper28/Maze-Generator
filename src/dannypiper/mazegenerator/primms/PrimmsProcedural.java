package dannypiper.mazegenerator.primms;

import java.util.LinkedList;

import dannypiper.mazegenerator.Gui;
import dannypiper.mazegenerator.Mazegen;

public class PrimmsProcedural {

	private PrimmsProcedural ( ) {
		// Cannot instantiate the class
	}

	public static void executePrimms ( ) {
		long frameControlTime = System.currentTimeMillis ( );

		int nodesFound = 0;
		final int nodesNeeded = ( Mazegen.width * Mazegen.height ) - 1;

		PrimmsUtils.pivotColumns = new LinkedList <> ( );
		PrimmsUtils.deletedRows = new boolean [ Mazegen.max ];

		for ( int i = 0; i < Mazegen.max; i ++ ) {
			PrimmsUtils.deletedRows [ i ] = false;
		}

		final int start = Mazegen.entranceY * Mazegen.width;
		PrimmsUtils.pivotColumns.add ( start );
		PrimmsUtils.deletedRows [ start ] = true;

		final Thread [ ] workers = new Thread [ nodesNeeded ];
		final PrimmsProceduralWorkerThread [ ] worker = new PrimmsProceduralWorkerThread [ nodesNeeded ];

		for ( int i = 0; i < nodesNeeded; i ++ ) {
			worker [ i ] = new PrimmsProceduralWorkerThread ( );
		}

		short minValue = Mazegen.maxRand + 1;
		int column = 0;
		int row = 0;
		int Coord;

		while ( nodesFound < nodesNeeded ) {
			// PRIMMS
			minValue = Mazegen.maxRand + 1;
			column = 0;
			row = 0;

			// Main thread
			final int x = PrimmsUtils.pivotColumns.get ( PrimmsUtils.pivotColumns.size ( ) - 1 ) % Mazegen.width;
			final int y = PrimmsUtils.pivotColumns.get ( PrimmsUtils.pivotColumns.size ( ) - 1 ) / Mazegen.width;
			Coord = ( Mazegen.width * y ) + x;

			column = PrimmsUtils.pivotColumns.get ( PrimmsUtils.pivotColumns.size ( ) - 1 );

			// Detect deleted rows
			boolean yCurrentXPlusDeleted = true;
			boolean yCurrentXMinusDeleted = true;
			boolean yDownDeleted = true;
			boolean yUpDeleted = true;

			if ( x < ( Mazegen.width - 1 ) ) {
				yCurrentXPlusDeleted = PrimmsUtils.deletedRows [ Coord + 1 ];
			}

			if ( x > 0 ) {
				yCurrentXMinusDeleted = PrimmsUtils.deletedRows [ Coord - 1 ];
			}

			if ( y < ( Mazegen.height - 1 ) ) {
				yUpDeleted = PrimmsUtils.deletedRows [ Coord + Mazegen.width ];
			}

			if ( y > 0 ) {
				yDownDeleted = PrimmsUtils.deletedRows [ Coord - Mazegen.width ];
			}

			// Find shortest local arc
			if ( ! ( yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted ) ) {

				if ( ! yCurrentXPlusDeleted ) {
					final short value = Mazegen.randInt ( );

					if ( value < minValue ) {
						minValue = value;
						row = Coord + 1;
					}

				}

				if ( ! yCurrentXMinusDeleted ) {
					final short value = Mazegen.randInt ( );

					if ( value < minValue ) {
						minValue = value;
						row = Coord - 1;
					}

				}

				if ( ! yUpDeleted ) {
					final short value = Mazegen.randInt ( );

					if ( value < minValue ) {
						minValue = value;
						row = Coord + Mazegen.width;
					}

				}

				if ( ! yDownDeleted ) {
					final short value = Mazegen.randInt ( );

					if ( value < minValue ) {
						minValue = value;
						row = Coord - Mazegen.width;
					}

				}

			}
			else {
				PrimmsUtils.pivotColumns.removeLast ( );
			}

			// If an arc can be expanded from the latest node do it
			if ( minValue <= Mazegen.maxRand ) {
				// commit arc
				PrimmsUtils.deletedRows [ row ] = true;

				PrimmsUtils.pivotColumns.add ( row );

				Mazegen.drawArc ( column, row );
				nodesFound ++ ;
			}
			else {

				// Start up worker threads and look for another arc to expand from
				// Then clear dead nodes
				for ( int i = 0; i < ( PrimmsUtils.pivotColumns.size ( ) - 1 ); i ++ ) {
					final int columnValue = PrimmsUtils.pivotColumns.get ( i );

					worker [ i ].columnInput = columnValue;
					worker [ i ].finished = false;
					workers [ i ] = new Thread ( worker [ i ] );
					workers [ i ].start ( );
				}

				// Wait for all to be complete and get the minimum value.
				boolean finished = false;
				boolean breakStatmentRepalcement = false;

				while ( ! finished ) {
					finished = true;

					for ( int i = 0; ( i < ( PrimmsUtils.pivotColumns.size ( ) - 1 ) )
					        && ! breakStatmentRepalcement; i ++ ) {

						if ( worker [ i ].finished ) {

							if ( worker [ i ].minValue < minValue ) {
								minValue = worker [ i ].minValue;
								row = worker [ i ].row;
								column = worker [ i ].column;

								if ( minValue == 0 ) {
									finished = true;
									breakStatmentRepalcement = true;
									// ^ escape for loop without a break to appease Mark
								}

							}

						}

						finished &= worker [ i ].finished;
					}

				}

				// Delete finished nodes from queue
				for ( int i = 0; i < ( PrimmsUtils.pivotColumns.size ( ) - 1 ); i ++ ) {

					if ( worker [ i ].delete ) {

						try {
							PrimmsUtils.pivotColumns.remove ( ( Integer ) worker [ i ].columnInput );
						}
						catch ( final Exception e ) {

						}

					}

				}

				// Delete row that is committed to image
				PrimmsUtils.deletedRows [ row ] = true;

				// Add it as a pivot
				PrimmsUtils.pivotColumns.add ( row );

				// Draw arc
				Mazegen.drawArc ( column, row );
				nodesFound ++ ;
			}

			if ( ( System.currentTimeMillis ( ) - frameControlTime ) >= Mazegen.frameRate ) {
				frameControlTime = System.currentTimeMillis ( );
				Mazegen.render ( );
				final double percentage = ( double ) nodesFound / ( double ) nodesNeeded;
				Gui.setProgress ( percentage );
			}

		}

	}
}
