package dannypiper.mazegenerator.primms;

import java.util.LinkedList;
import java.util.Random;

import dannypiper.mazegenerator.Mazegen;

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
		PrimmsUtils.adjMat = new short [ Mazegen.max ] [ Mazegen.max ];

		for ( int x = 0; x < Mazegen.max; x ++ ) {

			for ( int y = 0; y < Mazegen.max; y ++ ) {
				PrimmsUtils.adjMat [ x ] [ y ] = 0;
			}

		}

		for ( int x = 0; x < Mazegen.width; x ++ ) {

			for ( int y = 0; y < Mazegen.height; y ++ ) {
				final int Coord = ( Mazegen.width * y ) + x;

				if ( x < ( Mazegen.width - 1 ) ) {
					PrimmsUtils.randIntAdjMat ( Coord + 1, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord + 1 );
				}

				if ( x > 0 ) {
					PrimmsUtils.randIntAdjMat ( Coord - 1, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord - 1 );
				}

				if ( y < ( Mazegen.height - 1 ) ) {
					PrimmsUtils.randIntAdjMat ( Coord + Mazegen.width, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord + Mazegen.width );
				}

				if ( y > 0 ) {
					PrimmsUtils.randIntAdjMat ( Coord - Mazegen.width, Coord );
					PrimmsUtils.randIntAdjMat ( Coord, Coord - Mazegen.width );
				}

			}

		}

	}

	private static void randIntAdjMat ( final int x, final int y ) {
		int random = PrimmsUtils.rand.nextInt ( );

		if ( random < 0 ) {
			random *= - 1;
		}

		PrimmsUtils.adjMat [ x ] [ y ] = ( short ) ( random % Mazegen.maxRand );
	}
}
