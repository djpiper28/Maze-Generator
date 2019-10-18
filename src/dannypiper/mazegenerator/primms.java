package dannypiper.mazegenerator;

import java.util.LinkedList;

public class primms {
		
	public static void executePrimms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = mazegen.height * ( mazegen.width - 1) + mazegen.width * ( mazegen.height - 1);
		
		//mazegen.pivotColumns = new int[nodesNeeded+1];
		mazegen.pivotColumns = new  LinkedList<Integer>();
		mazegen.deletedRows = new  LinkedList<Integer>();
		//mazegen.deletedRows = new int[nodesNeeded+1];
	
		mazegen.pivotColumns.add(mazegen.height * mazegen.width / 2);
		mazegen.deletedRows.add(mazegen.pivotColumns.get(0));
		
		mazegen.pivotColumnsLength = 1;
		mazegen.deletedRowsLength = 1;		
		
		Thread[] workers = new Thread[nodesNeeded];
		primmsWorkerThread[] worker = new primmsWorkerThread[nodesNeeded];
		
		for(int i = 0; i<nodesNeeded; i++) {
			worker[i] = new primmsWorkerThread();
			workers[i] = new Thread(worker[i], "Worker "+i);
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
			
			for(int i = 1; i < mazegen.pivotColumnsLength; i++) {
				int x = mazegen.pivotColumns.get(i);
				
				worker[i].columnInput = x;
				workers[i] = new Thread(worker[i]);
				workers[i].start();
			}			

			//Main thread			
			int x = mazegen.pivotColumns.get(0) % mazegen.width; 
			int y = mazegen.pivotColumns.get(0) / mazegen.width;
			Coord = (mazegen.width * y) + x;
			
			column = mazegen.pivotColumns.get(0);			

			boolean yCurrentXPlusDeleted = false;
			boolean yCurrentXMinusDeleted = false;
			boolean yDownDeleted = false;
			boolean yUpDeleted = false;
			
			int offSet = 0;
			int j = 0;
			while(j < mazegen.deletedRowsLength && !(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {	
				if (mazegen.deletedRows.get(j)==Coord + 1) {
					yCurrentXPlusDeleted = true;
				} else if (mazegen.deletedRows.get(j)==Coord - 1) {
					yCurrentXMinusDeleted = true;
				} else if (mazegen.deletedRows.get(j)==Coord+mazegen.width) {
					yUpDeleted = true;
				} else if (mazegen.deletedRows.get(j)==Coord-mazegen.width) {
					yDownDeleted = true;
				}
				j++;
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
				mazegen.pivotColumnsLength--;
				mazegen.pivotColumns.remove(0);		
				offSet++;
			}
			
			//wait for all to be complete and get the minimum value.
			boolean finished = false;
			while(!finished) {
				finished = true;
				for(int i = 1; i < mazegen.pivotColumnsLength; i++) {
					finished &= worker[i].finished;
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
						if(worker[i].delete) {
							mazegen.pivotColumnsLength--;
							mazegen.pivotColumns.remove(i - offSet);
							offSet++;
							worker[i].delete = false;
						}
					}
				}
			}
	
			mazegen.deletedRows.add(row);
			mazegen.deletedRowsLength++;
	
			mazegen.pivotColumns.add(row);
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
