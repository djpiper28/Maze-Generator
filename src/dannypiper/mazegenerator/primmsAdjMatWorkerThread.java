package dannypiper.mazegenerator;

public class primmsAdjMatWorkerThread implements Runnable {
	
	public boolean finished;
	public boolean delete;
	public int columnInput;	
	public int row;
	public int column;
	public short minValue;
	
	public primmsAdjMatWorkerThread() {
		this.columnInput=-1;
	}
	
	@Override
	public void run() {		
		//this.finished = false;
		this.delete = false;
		this.row = 0;
		this.column = columnInput;
		
		short minValue = mazegen.maxRand+1;
		
		int x = this.columnInput % mazegen.width; 
		int y = this.columnInput / mazegen.width;
		int Coord = columnInput;
		
		boolean yCurrentXPlusDeleted = false;
		boolean yCurrentXMinusDeleted = false;
		boolean yDownDeleted = false;
		boolean yUpDeleted = false;
		
		if(x < mazegen.width - 1) {
			yCurrentXPlusDeleted = mazegen.deletedRows[Coord + 1];
		}
		if(x > 0) {
			yCurrentXMinusDeleted = mazegen.deletedRows[Coord - 1];
		}
		if(y < mazegen.height - 1) {
			yUpDeleted = mazegen.deletedRows[Coord + mazegen.width];
		}
		if(y > 0) {
			yDownDeleted = mazegen.deletedRows[Coord - mazegen.width];
		}
		
		if(!(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {	
			if(x < mazegen.width - 1 && !yCurrentXPlusDeleted) {
				if(mazegen.adjMat[Coord][Coord+1] < minValue) {
					minValue = mazegen.adjMat[Coord][Coord+1];
					row = Coord+1;
				}
			}
			if(x > 0 && !yCurrentXMinusDeleted) {
				if(mazegen.adjMat[Coord][Coord-1] < minValue) {
					minValue = mazegen.adjMat[Coord][Coord-1];
					row = Coord-1;
				}
			}			
			if(y < mazegen.height - 1 && !yUpDeleted) {
				if(mazegen.adjMat[Coord][Coord + mazegen.width] < minValue) {
					minValue = mazegen.adjMat[Coord][Coord + mazegen.width];
					row = Coord + mazegen.width;
				}
			}
			if(y > 0 && !yDownDeleted) {
				if(mazegen.adjMat[Coord][Coord - mazegen.width] < minValue) {
					minValue = mazegen.adjMat[Coord][Coord - mazegen.width];
					row = Coord - mazegen.width;
				}
			}			
		} else {
			this.delete = true;
		}

		finished = true;
		this.minValue = minValue;
	}

}
