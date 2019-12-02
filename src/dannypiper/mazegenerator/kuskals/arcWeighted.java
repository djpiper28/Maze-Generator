package dannypiper.mazegenerator.kuskals;

import dannypiper.mazegenerator.MazeGen;

public class ArcWeighted extends Arc {
	public short weight;

	public ArcWeighted ( final int coord, final int i ) {
		super ( coord, i );
		this.weight = MazeGen.randInt ( );
	}

}
