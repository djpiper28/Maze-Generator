package dannypiper.mazegenerator.primms;

import java.util.LinkedList;

import dannypiper.mazegenerator.Gui;
import dannypiper.mazegenerator.Mazegen;

public class PrimmsAdjMat {

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
		final PrimmsAdjMatWorkerThread [ ] worker = new PrimmsAdjMatWorkerThread [ nodesNeeded ];

		for ( int i = 0; i < nodesNeeded; i ++ ) {
			worker [ i ] = new PrimmsAdjMatWorkerThread ( );
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

			boolean yCurrentXPlusDeleted = false;
			boolean yCurrentXMinusDeleted = false;
			boolean yDownDeleted = false;
			boolean yUpDeleted = false;

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

			if ( ! ( yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted ) ) {

				if ( ( x < ( Mazegen.width - 1 ) ) && ! yCurrentXPlusDeleted ) {

					if ( PrimmsUtils.adjMat [ Coord ] [ Coord + 1 ] < minValue ) {
						minValue = PrimmsUtils.adjMat [ Coord ] [ Coord + 1 ];
						row = Coord + 1;
					}

				}

				if ( ( x > 0 ) && ! yCurrentXMinusDeleted ) {

					if ( PrimmsUtils.adjMat [ Coord ] [ Coord - 1 ] < minValue ) {
						minValue = PrimmsUtils.adjMat [ Coord ] [ Coord - 1 ];
						row = Coord - 1;
					}

				}

				if ( ( y < ( Mazegen.height - 1 ) ) && ! yUpDeleted ) {

					if ( PrimmsUtils.adjMat [ Coord ] [ Coord + Mazegen.width ] < minValue ) {
						minValue = PrimmsUtils.adjMat [ Coord ] [ Coord + Mazegen.width ];
						row = Coord + Mazegen.width;
					}

				}

				if ( ( y > 0 ) && ! yDownDeleted ) {

					if ( PrimmsUtils.adjMat [ Coord ] [ Coord - Mazegen.width ] < minValue ) {
						minValue = PrimmsUtils.adjMat [ Coord ] [ Coord - Mazegen.width ];
						row = Coord - Mazegen.width;
					}

				}

			}
			else {
				PrimmsUtils.pivotColumns.removeLast ( );
			}

			if ( minValue <= Mazegen.halfMaxRand ) {

				PrimmsUtils.deletedRows [ row ] = true;

				PrimmsUtils.pivotColumns.add ( row );

				Mazegen.drawArc ( column, row );
				nodesFound ++ ;
			}
			else {

				// Start up worker threads
				for ( int i = 0; i < ( PrimmsUtils.pivotColumns.size ( ) - 1 ); i ++ ) {
					final int columnValue = PrimmsUtils.pivotColumns.get ( i );

					worker [ i ].columnInput = columnValue;
					worker [ i ].finished = false;
					workers [ i ] = new Thread ( worker [ i ] );
					workers [ i ].start ( );
				}

				// wait for all to be complete and get the minimum value.
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
								}

							}

						}

						finished &= worker [ i ].finished;
					}

				}

				for ( int i = 0; i < ( PrimmsUtils.pivotColumns.size ( ) - 1 ); i ++ ) {

					if ( worker [ i ].delete ) {

						try {
							PrimmsUtils.pivotColumns.remove ( ( Integer ) worker [ i ].columnInput );
						}
						catch ( final Exception e ) {

						}

						worker [ i ].delete = false;
					}

				}

				PrimmsUtils.deletedRows [ row ] = true;

				PrimmsUtils.pivotColumns.add ( row );

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
