package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.mazegen;

public class primmsProcedualWorkerThread implements Runnable {

	
	public boolean finished;
	public boolean delete;
	public int columnInput;	
	public int row;
	public int column;
	public short minValue;
	
	public primmsProcedualWorkerThread() {
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
		
		boolean yCurrentXPlusDeleted = true;
		boolean yCurrentXMinusDeleted = true;
		boolean yDownDeleted = true;
		boolean yUpDeleted = true;
		
		if(x < mazegen.width - 1) {
			yCurrentXPlusDeleted = primmsUtils.visitedRows[Coord + 1];
		}
		if(x > 0) {
			yCurrentXMinusDeleted = primmsUtils.visitedRows[Coord - 1];
		}
		if(y < mazegen.height - 1) {
			yUpDeleted = primmsUtils.visitedRows[Coord + mazegen.width];
		}
		if(y > 0) {
			yDownDeleted = primmsUtils.visitedRows[Coord - mazegen.width];
		}
		
		if(!yCurrentXPlusDeleted) {
			short value = mazegen.randInt();
			if(value < minValue) {
				minValue = value;
				row = Coord + 1;
			}
		}
		if(!yCurrentXMinusDeleted) {
			short value = mazegen.randInt();
			if(value < minValue) {
				minValue = value;
				row = Coord - 1;
			}
		}				
		if(!yUpDeleted) {
			short value = mazegen.randInt();
			if(value < minValue) {
				minValue = value;
				row = Coord + mazegen.width;
			}
		}
		if(!yDownDeleted) {
			short value = mazegen.randInt();
			if(value < minValue) {
				minValue = value;
				row = Coord - mazegen.width;
			}
		}			
		this.delete = yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted;

		finished = true;
		this.minValue = minValue;
	}

}
