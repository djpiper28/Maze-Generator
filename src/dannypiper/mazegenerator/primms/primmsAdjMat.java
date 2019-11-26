package dannypiper.mazegenerator.primms;

import java.util.LinkedList;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;

public class primmsAdjMat {
		
	public static void executePrimms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = mazegen.width * mazegen.height - 1;
		
		primmsUtils.pivotColumns = new LinkedList<Integer>();
		primmsUtils.visitedRows = new boolean[mazegen.max];
		
		for(int i = 0; i< mazegen.max; i++) {
			primmsUtils.visitedRows[i] = false;
		}		
		
		int start = mazegen.entranceY * mazegen.width;
		primmsUtils.pivotColumns.add(start);
		primmsUtils.visitedRows[start] = true;
		
		primmsUtils.pivotColumnsLength = 1;
		
		Thread[] workers = new Thread[nodesNeeded];
		primmsAdjMatWorkerThread[] worker = new primmsAdjMatWorkerThread[nodesNeeded];
		
		for(int i = 0; i < nodesNeeded; i++) {
			worker[i] = new primmsAdjMatWorkerThread();
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
			int x = primmsUtils.pivotColumns.get(primmsUtils.pivotColumnsLength - 1) % mazegen.width; 
			int y = primmsUtils.pivotColumns.get(primmsUtils.pivotColumnsLength - 1) / mazegen.width;
			Coord = (mazegen.width * y) + x;
			
			column = primmsUtils.pivotColumns.get(primmsUtils.pivotColumnsLength - 1);			

			boolean yCurrentXPlusDeleted = false;
			boolean yCurrentXMinusDeleted = false;
			boolean yDownDeleted = false;
			boolean yUpDeleted = false;
			
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

			if(!(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {				
				if(x < mazegen.width - 1 && !yCurrentXPlusDeleted) {
					if(primmsUtils.adjMat[Coord][Coord+1] < minValue) {
						minValue = primmsUtils.adjMat[Coord][Coord+1];
						row = Coord + 1;
					}
				}
				if(x > 0 && !yCurrentXMinusDeleted) {
					if(primmsUtils.adjMat[Coord][Coord-1] < minValue) {
						minValue = primmsUtils.adjMat[Coord][Coord-1];
						row = Coord - 1;
					}
				}				
				if(y < mazegen.height - 1 && !yUpDeleted) {
					if(primmsUtils.adjMat[Coord][Coord + mazegen.width] < minValue) {
						minValue = primmsUtils.adjMat[Coord][Coord + mazegen.width];
						row = Coord + mazegen.width;
					}
				}
				if(y > 0 && !yDownDeleted) {
					if(primmsUtils.adjMat[Coord][Coord - mazegen.width] < minValue) {
						minValue = primmsUtils.adjMat[Coord][Coord - mazegen.width];
						row = Coord - mazegen.width;
					}
				}	
			} else {	
				primmsUtils.pivotColumnsLength--;	
				primmsUtils.pivotColumns.remove(primmsUtils.pivotColumnsLength);
			}
			
			if(minValue <= mazegen.halfMaxRand) {
				
				primmsUtils.visitedRows[row] = true;
		
				primmsUtils.pivotColumns.add(row);
				primmsUtils.pivotColumnsLength++;
		
				mazegen.drawArc(column, row);
				nodesFound++;				
			} else {
				//Start up worker threads
				for(int i = 0; i < primmsUtils.pivotColumnsLength - 1; i++) {
					int columnValue = primmsUtils.pivotColumns.get(i);
					
					worker [i].columnInput = columnValue;
					worker [i].finished = false;
					workers[i] = new Thread(worker[i]);
					workers[i].start();
				}						
				
				//wait for all to be complete and get the minimum value.
				boolean finished = false;
				boolean breakStatmentRepalcement = false;
				while(!finished) {
					finished = true;
					for(int i = 0; (i < primmsUtils.pivotColumnsLength - 1) && !breakStatmentRepalcement ; i++) {
						if(worker[i].finished) {
							if(worker[i].minValue < minValue) {
								minValue = worker[i].minValue;
								row = worker[i].row;
								column = worker[i].column;
								if(minValue == 0) {
									finished = true;	
									breakStatmentRepalcement = true;
								}
							}
						}
						finished &= worker[i].finished;
					}
				}

				for(int i = 0; i<primmsUtils.pivotColumnsLength -1 ; i++) {
					if(worker[i].delete) {
						primmsUtils.pivotColumnsLength--;
						try {
							primmsUtils.pivotColumns.remove((Integer) worker[i].columnInput);
						} catch(Exception e) {
							
						}
						worker[i].delete = false;
					}
				}
				
				primmsUtils.visitedRows[row] = true;
		
				primmsUtils.pivotColumns.add(row);
				primmsUtils.pivotColumnsLength++;
				
				mazegen.drawArc(column, row);
				nodesFound++;				
			}	
			
			if(System.currentTimeMillis() - frameControlTime >= mazegen.frameRate) {				
				frameControlTime = System.currentTimeMillis();
				mazegen.render();
				double percentage = (double) nodesFound / (double) nodesNeeded;
				gui.setProgress(percentage);
			}			
		}
	}

}
