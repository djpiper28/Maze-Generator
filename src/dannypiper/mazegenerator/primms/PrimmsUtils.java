package dannypiper.mazegenerator.primms;

import java.util.LinkedList;
import java.util.Random;

import dannypiper.mazegenerator.MazeGen;

public class PrimmsUtils {

	public static LinkedList < Integer > pivotColumns;
	public static boolean [ ] deletedRows;
	public static short [ ] [ ] adjMat;
	private static Random rand;

	private PrimmsUtils ( ) {
		// Cannot instantiate the utils class
	}

	public static void populateAdjMat ( ) {
		PrimmsUtils.rand = new Random ( );
		PrimmsUtils.adjMat = new short [ MazeGen.max ] [ MazeGen.max ];

		for ( int x = 0; x < MazeGen.max; x ++ ) {

			for ( int y = 0; y < MazeGen.max; y ++ ) {
				PrimmsUtils.adjMat [ x ] [ y ] = 0;
			}

		}

		for ( int x = 0; x < MazeGen.width; x ++ ) {

			for ( int y = 0; y < MazeGen.height; y ++ ) {
				final int Coord = ( MazeGen.width * y ) + x;

				if ( x < ( MazeGen.width - 1 ) ) {
					PrimmsUtils.randIntAdjMat ( Coord + 1, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord + 1 );
				}

				if ( x > 0 ) {
					PrimmsUtils.randIntAdjMat ( Coord - 1, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord - 1 );
				}

				if ( y < ( MazeGen.height - 1 ) ) {
					PrimmsUtils.randIntAdjMat ( Coord + MazeGen.width, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord + MazeGen.width );
				}

				if ( y > 0 ) {
					PrimmsUtils.randIntAdjMat ( Coord - MazeGen.width, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord - MazeGen.width );
				}

			}

		}

	}

	private static void randIntAdjMat ( final int x, final int y ) {
		int random = PrimmsUtils.rand.nextInt ( );

		if ( random < 0 ) {
			random *= - 1;
		}

		PrimmsUtils.adjMat [ x ] [ y ] = ( short ) ( random % MazeGen.maxRand );
	}
}
