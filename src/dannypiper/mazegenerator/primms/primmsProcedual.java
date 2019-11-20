package dannypiper.mazegenerator.primms;

import java.util.LinkedList;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;

public class primmsProcedual {
	
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
		
		mazegen.pivotColumns.add(mazegen.entranceY * mazegen.width);
		mazegen.deletedRows[mazegen.entranceY * mazegen.width] = true;
		
		mazegen.pivotColumnsLength = 1;
		
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
			int x = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1) % mazegen.width; 
			int y = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1) / mazegen.width;
			Coord = (mazegen.width * y) + x;
			
			column = mazegen.pivotColumns.get(mazegen.pivotColumnsLength - 1);			

			//Detect deleted rows
			boolean yCurrentXPlusDeleted = true;
			boolean yCurrentXMinusDeleted = true;
			boolean yDownDeleted = true;
			boolean yUpDeleted = true;
			
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
				mazegen.pivotColumnsLength--;	
				mazegen.pivotColumns.remove(mazegen.pivotColumnsLength);
			}
			
			//If an arc can be expanded from the latest node do it
			if(minValue <= mazegen.maxRand) {
				
				//commit arc
				mazegen.deletedRows[row] = true;
		
				mazegen.pivotColumns.add(row);
				mazegen.pivotColumnsLength++;
		
				mazegen.drawArc(column, row);
				
			} else {
				//Start up worker threads and look for another arc to expand from
				//Then clear dead nodes
				for(int i = 0; i < mazegen.pivotColumnsLength - 1; i++) {
					int columnValue = mazegen.pivotColumns.get(i);
					
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
					for(int i = 0; (i < mazegen.pivotColumnsLength - 1) && !breakStatmentRepalcement ; i++) {
						if(worker[i].finished) {
							if(worker[i].minValue < minValue) {
								minValue = worker[i].minValue;
								row = worker[i].row;
								column = worker[i].column;
								if(minValue == 0) {
									finished = true;	
									breakStatmentRepalcement = true;	
									//^ escape for loop without a break to appease Mark Laver
								}
							}
						}
						finished &= worker[i].finished;					
					}
				}

				//Delete finished nodes from queue
				int offSet = 0;	
				for(int i = 0; i < mazegen.pivotColumnsLength -1 ; i++) {
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
				
				//Delete row that is committed to image
				mazegen.deletedRows[row] = true;
		
				//Add it as a pivot
				mazegen.pivotColumns.add(row);
				mazegen.pivotColumnsLength++;
				
				//Draw arc
				mazegen.drawArc(column, row);				
			}				
			if(System.currentTimeMillis() - frameControlTime >= mazegen.frameRate) {				
				frameControlTime = System.currentTimeMillis();
				mazegen.render();
				double percentage = (double) nodesFound / (double) nodesNeeded;
				gui.setProgress(percentage);
			}
			
			nodesFound++;
		}
	}
}