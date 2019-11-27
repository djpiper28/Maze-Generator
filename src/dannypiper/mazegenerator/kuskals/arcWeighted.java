package dannypiper.mazegenerator.kuskals;

import dannypiper.mazegenerator.mazegen;

public class arcWeighted extends arc {
	public short weight;		
	
	public arcWeighted(int coord, int i) {
		super(coord, i);
		this.weight = mazegen.randInt();	
	}

}
