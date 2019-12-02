package dannypiper.mazegenerator.kuskals;

import dannypiper.mazegenerator.Mazegen;

public class ArcWeighted extends Arc {
	public short weight;

	public ArcWeighted ( final int coord, final int i ) {
		super ( coord, i );
		this.weight = Mazegen.randInt ( );
	}

}
