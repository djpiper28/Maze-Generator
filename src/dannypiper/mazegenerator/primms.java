package dannypiper.mazegenerator;

import java.util.LinkedList;

public class primms {
		
	public static void executePrimms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = mazegen.height * ( mazegen.width - 1) + mazegen.width * ( mazegen.height - 1);
		nodesNeeded = mazegen.height * mazegen.width - 1;
		
		mazegen.pivotColumns = new LinkedList<Integer>();
		mazegen.deletedRows = new boolean[mazegen.max];
		
		for(int i = 0; i< mazegen.max; i++) {
			mazegen.deletedRows[i] = false;
		}		
		
		mazegen.pivotColumns.add(mazegen.height * mazegen.width / 2);
		mazegen.deletedRows[0] = true;
		
		mazegen.pivotColumnsLength = 1;
		
		Thread[] workers = new Thread[nodesNeeded];
		primmsWorkerThread[] worker = new primmsWorkerThread[nodesNeeded];
		
		for(int i = 0; i < nodesNeeded; i++) {
			worker[i] = new primmsWorkerThread();
		}
		
		short minValue = mazegen.maxRand+1;
		int column = 0;
		int row = 0;
		int Coord;
		
		while(nodesFound < nodesNeeded) {
			
			//PRIMMS
			minValue = mazegen.maxRand+1;
			column = 0;
			row = 0;
			
			//Main thread			
			int x = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1) % mazegen.width; 
			int y = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1) / mazegen.width;
			Coord = (mazegen.width * y) + x;
			
			column = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1);			

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
						row = Coord + 1;
					}
				}
				if(x > 0 && !yCurrentXMinusDeleted) {
					if(mazegen.adjMat[Coord][Coord-1] < minValue) {
						minValue = mazegen.adjMat[Coord][Coord-1];
						row = Coord - 1;
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
				mazegen.pivotColumnsLength--;	
				mazegen.pivotColumns.remove(mazegen.pivotColumnsLength);
			}
			
			if(minValue <= mazegen.halfMaxRand) {
				
				mazegen.deletedRows[row] = true;
		
				mazegen.pivotColumns.add(row);
				mazegen.pivotColumnsLength++;
		
				mazegen.drawArc(column, row);
				
			} else {
				//Start up worker threads
				for(int i = 0; i < mazegen.pivotColumnsLength - 1; i++) {
					int columnValue = mazegen.pivotColumns.get(i);
					
					worker [i].columnInput = columnValue;
					worker [i].finished = false;
					workers[i] = new Thread(worker[i]);
					workers[i].start();
				}						
				
				//wait for all to be complete and get the minimum value.
				boolean finished = false;
				while(!finished) {
					finished = true;
					for(int i = 0; (i < mazegen.pivotColumnsLength - 1) ; i++) {
						if(worker[i].finished) {
							if(worker[i].minValue < minValue) {
								minValue = worker[i].minValue;
								row = worker[i].row;
								column = worker[i].column;
								if(minValue == 0) {
									finished = true;	
									break;
								}
							}
						}
						finished &= worker[i].finished;
					}
				}

				int offSet = 0;	
				for(int i = 0; i<mazegen.pivotColumnsLength -1 ; i++) {
					if(worker[i].delete) {
						mazegen.pivotColumnsLength--;
						try {
							mazegen.pivotColumns.remove(i - offSet);
						} catch(Exception e) {
							
						}
						offSet++;
						worker[i].delete = false;
					}
				}
				
				mazegen.deletedRows[row] = true;
		
				mazegen.pivotColumns.add(row);
				mazegen.pivotColumnsLength++;
				
				mazegen.drawArc(column, row);
				
			}	
			
			if(System.currentTimeMillis() - frameControlTime >=16) {				
				frameControlTime = System.currentTimeMillis();
				mazegen.render();
			}
			
			nodesFound++;
		}
	}

}
