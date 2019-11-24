package dannypiper.mazegenerator.kuskals;

import dannypiper.mazegenerator.mazegen;

public class arcWeighted extends arc {
	public short weight;	
	
	
	public arcWeighted(int startingNode, int endingNode) {
		super(startingNode, endingNode);
		this.weight = mazegen.randInt();	
	}

}
