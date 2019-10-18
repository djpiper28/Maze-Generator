package dannypiper.mazegenerator;

public class primmsWorkerThread implements Runnable {
	
	public boolean finished;
	public int columnInput;	
	public int row;
	public int column;
	public int minValue;
	
	public primmsWorkerThread() {
		this.columnInput=-1;
	}
	
	@Override
	public void run() {		
		this.finished = false;
		this.row = 0;
		this.column = columnInput;
		
		int minValue = mazegen.maxRand+1;
		
		int x = this.columnInput % mazegen.width; 
		int y = this.columnInput / mazegen.width;
		int Coord = columnInput;
		
		boolean yCurrentXPlusDeleted = false;
		boolean yCurrentXMinusDeleted = false;
		boolean yDownDeleted = false;
		boolean yUpDeleted = false;
		
		int j = 0;
		while(j < mazegen.deletedRowsLength && !(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {	
			if (mazegen.deletedRows[j]==Coord + 1) {
				yCurrentXPlusDeleted = true;
			} else if (mazegen.deletedRows[j]==Coord - 1) {
				yCurrentXMinusDeleted = true;
			} else if (mazegen.deletedRows[j]==Coord+mazegen.width) {
				yUpDeleted = true;
			} else if (mazegen.deletedRows[j]==Coord-mazegen.width) {
				yDownDeleted = true;
			}
			j++;
		}
	
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

		finished = true;
		this.minValue = minValue;
	}

}
