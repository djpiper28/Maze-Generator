package dannypiper.mazegenerator.primms;

import java.util.LinkedList;
import java.util.Random;

import dannypiper.mazegenerator.mazegen;

public class primmsUtils {

	public static LinkedList < Integer > pivotColumns;
	public static boolean [ ] deletedRows;
	public static short [ ] [ ] adjMat;
	private static Random rand;

	public static void populateAdjMat ( ) {
		primmsUtils.rand = new Random ( );
		primmsUtils.adjMat = new short [ mazegen.max ] [ mazegen.max ];

		for ( int x = 0; x < mazegen.max; x ++ ) {

			for ( int y = 0; y < mazegen.max; y ++ ) {
				primmsUtils.adjMat [ x ] [ y ] = 0;
			}

		}

		for ( int x = 0; x < mazegen.width; x ++ ) {

			for ( int y = 0; y < mazegen.height; y ++ ) {
				final int Coord = ( mazegen.width * y ) + x;

				if ( x < ( mazegen.width - 1 ) ) {
					primmsUtils.randIntAdjMat ( Coord + 1, Coord );
					primmsUtils.randIntAdjMat ( Coord, Coord + 1 );
				}

				if ( x > 0 ) {
					primmsUtils.randIntAdjMat ( Coord - 1, Coord );
					primmsUtils.randIntAdjMat ( Coord, Coord - 1 );
				}

				if ( y < ( mazegen.height - 1 ) ) {
					primmsUtils.randIntAdjMat ( Coord + mazegen.width, Coord );
					primmsUtils.randIntAdjMat ( Coord, Coord + mazegen.width );
				}

				if ( y > 0 ) {
					primmsUtils.randIntAdjMat ( Coord - mazegen.width, Coord );
					primmsUtils.randIntAdjMat ( Coord, Coord - mazegen.width );
				}

			}

		}

	}

	private static void randIntAdjMat ( final int x, final int y ) {
		int random = primmsUtils.rand.nextInt ( );

		if ( random < 0 ) {
			random *= - 1;
		}

		primmsUtils.adjMat [ x ] [ y ] = ( short ) ( random % mazegen.maxRand );
	}
}
