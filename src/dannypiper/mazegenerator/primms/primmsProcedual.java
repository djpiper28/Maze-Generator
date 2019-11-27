package dannypiper.mazegenerator.primms;

import java.util.LinkedList;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;

public class primmsProcedual {
	
	public static void executePrimms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = mazegen.width * mazegen.height - 1;
		
		primmsUtils.pivotColumns = new LinkedList<Integer>();
		primmsUtils.deletedRows = new boolean[mazegen.max];
		
		for(int i = 0; i< mazegen.max; i++) {
			primmsUtils.deletedRows[i] = false;
		}		
		
		int start = mazegen.entranceY * mazegen.width;
		primmsUtils.pivotColumns.add(start);
		primmsUtils.deletedRows[start] = true;
				
		Thread[] workers = new Thread[nodesNeeded];
		primmsProcedualWorkerThread[] worker = new primmsProcedualWorkerThread[nodesNeeded];
		
		for(int i = 0; i < nodesNeeded; i++) {
			worker[i] = new primmsProcedualWorkerThread();
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
			int x = primmsUtils.pivotColumns.get(primmsUtils.pivotColumns.size ( ) - 1) % mazegen.width; 
			int y = primmsUtils.pivotColumns.get(primmsUtils.pivotColumns.size ( )  - 1) / mazegen.width;
			Coord = (mazegen.width * y) + x;
			
			column = primmsUtils.pivotColumns.get(primmsUtils.pivotColumns.size ( )  - 1);			

			//Detect deleted rows
			boolean yCurrentXPlusDeleted = true;
			boolean yCurrentXMinusDeleted = true;
			boolean yDownDeleted = true;
			boolean yUpDeleted = true;
			
			if(x < mazegen.width - 1) {
				yCurrentXPlusDeleted = primmsUtils.deletedRows[Coord + 1];
			}
			if(x > 0) {
				yCurrentXMinusDeleted = primmsUtils.deletedRows[Coord - 1];
			}
			if(y < mazegen.height - 1) {
				yUpDeleted = primmsUtils.deletedRows[Coord + mazegen.width];
			}
			if(y > 0) {
				yDownDeleted = primmsUtils.deletedRows[Coord - mazegen.width];
			}

			//Find shortest local arc
			if(!(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {				
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
			} else {	
				primmsUtils.pivotColumns.removeLast ( );
			}
			
			//If an arc can be expanded from the latest node do it
			if(minValue <= mazegen.maxRand) {				
				//commit arc
				primmsUtils.deletedRows[row] = true;
		
				primmsUtils.pivotColumns.add(row);
		
				mazegen.drawArc(column, row);
				nodesFound++;				
			} else {
				//Start up worker threads and look for another arc to expand from
				//Then clear dead nodes
				for(int i = 0; i < primmsUtils.pivotColumns.size ( ) - 1; i++) {
					int columnValue = primmsUtils.pivotColumns.get(i);
					
					worker [i].columnInput = columnValue;
					worker [i].finished = false;
					workers[i] = new Thread(worker[i]);
					workers[i].start();
				}						
				
				//Wait for all to be complete and get the minimum value.
				boolean finished = false;
				boolean breakStatmentRepalcement = false;
				while(!finished) {
					finished = true;
					for(int i = 0; (i < primmsUtils.pivotColumns.size ( ) - 1) && !breakStatmentRepalcement ; i++) {
						if(worker[i].finished) {
							if(worker[i].minValue < minValue) {
								minValue = worker[i].minValue;
								row = worker[i].row;
								column = worker[i].column;
								if(minValue == 0) {
									finished = true;	
									breakStatmentRepalcement = true;	
									//^ escape for loop without a break to appease Mark
								}
							}
						}
						finished &= worker[i].finished;					
					}
				}

				//Delete finished nodes from queue
				for(int i = 0; i < primmsUtils.pivotColumns.size ( ) - 1 ; i++) {
					if(worker[i].delete) {
						try {
							primmsUtils.pivotColumns.remove((Integer) worker[i].columnInput);
						} catch(Exception e) {
							
						}
					}
				}
				
				//Delete row that is committed to image
				primmsUtils.deletedRows[row] = true;
		
				//Add it as a pivot
				primmsUtils.pivotColumns.add(row);
				
				//Draw arc
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
