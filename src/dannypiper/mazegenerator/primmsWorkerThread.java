package dannypiper.mazegenerator;

public class primmsWorkerThread implements Runnable {
	
	public boolean finished;
	public int x;	
	public int row;
	public int minValue;
	
	@Override
	public void run() {
		this.finished = false;
		this.row = 0;
		
		int minValue = mazegen.maxRand+1;
		
		for(int y = 0; y < mazegen.max; y++) {
			boolean deleted = false;
			int j = 0; 
			while(j < mazegen.deletedRowsLength && !deleted) {	
				if (y==mazegen.deletedRows[j]) {
					deleted = true;
				}
				j++;
			}
			
			if(!deleted) {
				if((mazegen.adjMat[x][y] < minValue) && mazegen.adjMat[x][y]!=-1) {
					minValue = mazegen.adjMat[x][y];
					row = y;		
					
					if(minValue==0) {
						break;			//TODO: Remove break
					}
				}
			}
		}

		finished = true;
		this.minValue = minValue;
	}

}
