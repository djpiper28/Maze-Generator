package dannypiper.mazegenerator.primms;

import java.util.LinkedList;
import java.util.Random;

import dannypiper.mazegenerator.mazegen;

public class primmsUtils {	

	public static LinkedList<Integer> pivotColumns;
	public static int pivotColumnsLength;
	public static boolean[] visitedRows;
	public static short[][] adjMat;
	private static Random rand;
	
	private static void randIntAdjMat(int x, int y) {
		int random = rand.nextInt();
		if (random < 0) {
			random *= -1;			
		} 
		adjMat[x][y] = (short) (random % mazegen.maxRand);
	}

	public static void populateAdjMat() {
		rand = new Random();
		adjMat = new short[mazegen.max][mazegen.max];
		for(int x = 0; x < mazegen.max; x++) {
			for(int y =0; y < mazegen.max; y++) {
				adjMat[x][y] = 0;
			}
		}
		
		for(int x = 0; x < mazegen.width; x++) {
			for(int y = 0; y < mazegen.height; y++) {
				int Coord = (mazegen.width * y) + x;
				
				if(x < mazegen.width - 1) {
					randIntAdjMat(Coord + 1, Coord);
					randIntAdjMat(Coord, Coord + 1);
				}
				if(x > 0) {
					randIntAdjMat(Coord - 1, Coord);
					randIntAdjMat(Coord, Coord - 1);
				}
				if(y < mazegen.height - 1) {
					randIntAdjMat(Coord + mazegen.width, Coord );
					randIntAdjMat(Coord, Coord + mazegen.width);
				}
				if(y > 0) {
					randIntAdjMat(Coord - mazegen.width, Coord);
					randIntAdjMat(Coord, Coord - mazegen.width);
				}				
			}
		}
	}
}
