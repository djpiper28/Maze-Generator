package dannypiper.mazegenerator;

public class primms {
		
	public static void executePrimms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = mazegen.height * mazegen.width - 1;
		
		mazegen.pivotColumns = new int[nodesNeeded+1];
		mazegen.deletedRows = new int[nodesNeeded+1];
	
		mazegen.pivotColumns[0] = mazegen.height * mazegen.width / 2;
		mazegen.deletedRows[0] = mazegen.pivotColumns[0];
		
		mazegen.pivotColumnsLength = 1;
		mazegen.deletedRowsLength = 1;		
		
		Thread[] workers = new Thread[nodesNeeded];
		primmsWorkerThread[] worker = new primmsWorkerThread[nodesNeeded];
		
		for(int i = 0; i<nodesNeeded; i++) {
			worker[i] = new primmsWorkerThread();
			workers[i] = new Thread(worker[i], "Worker "+i);
		}
		
		while(nodesFound < nodesNeeded) {
			
			//PRIMMS
			int minValue = mazegen.maxRand+1;
			int column = 0;
			int row = 0;
			
			for(int i = 1; i < mazegen.pivotColumnsLength; i++) {
				int x = mazegen.pivotColumns[i];
				
				worker[i].x = x;
				workers[i] = new Thread(worker[i]);
				workers[i].start();
			}			

			//Iterate on the main thread as well
			int x = mazegen.pivotColumns[0];
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
						column = x;
						row = y;		
						
						if(minValue==0) {
							break;			//TODO: Remove break
						}
					}
				}
			}
			//wait for all to be complete and get the minimum value.
			boolean finished = false;
			while(!finished) {
				finished = true;
				for(int i = 1; i < mazegen.pivotColumnsLength; i++) {
					finished &= worker[i].finished;
					if(worker[i].finished) {
						if(worker[i].minValue<minValue) {
							minValue = worker[i].minValue;
							row = worker[i].row;
							column = worker[i].x;
							if(minValue==0) {
								finished = true;
								break;								
							}
						}
					}
				}
			}
	
			mazegen.deletedRows[mazegen.deletedRowsLength] = row;
			mazegen.deletedRowsLength++;
	
			mazegen.pivotColumns[mazegen.pivotColumnsLength] = row;
			mazegen.pivotColumnsLength++;
	
			mazegen.drawArc(column, row);
			
			if(System.currentTimeMillis() - frameControlTime >=16) {				
				frameControlTime = System.currentTimeMillis();
				mazegen.render();
			}
			nodesFound++;
		}
	}

}
