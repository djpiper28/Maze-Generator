package dannypiper.mazegenerator.kuskals;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.sorting.kruskalsSortManager;
import dannypiper.mazegenerator.kuskals.sorting.sortType;

public class kruskals implements Runnable{
	
	private Queue<arc> sortedData;
	private sortType type;
	private int[] disjointSet;
	
	public kruskals (sortType type) {
		this.type = type;
	}
	
	private int find(int v) {
		Queue <Integer> queue = new LinkedList < Integer > ( );
		while (disjointSet[v] >= 0) {
			queue.add(v);
			v = disjointSet[v];
		}

		while(!queue.isEmpty ( )) {
			disjointSet[queue.remove ( )] = v;
		}
		return v;
	}	
	
	private boolean isCyclic(arc a) { 
		int r0 = find(a.startingNode);
		int r1 = find(a.endingNode);
		if (r0 == r1) {
			return false;
		}

		if (disjointSet[r0] < disjointSet[r1]) {
			disjointSet[r1] = r0;
		}
		else {
			if (disjointSet[r1] < disjointSet[r0]) {
				disjointSet[r0] = r1;
		    }
		    else {
		    	disjointSet[r1] = r0;
		    	disjointSet[r0] -= 1;
		    }
		}
		
		return true;
	}
	
	private void executeKruskals() {
		int nodesFound = 0;
		int nodesNeeded = mazegen.width * mazegen.height - 1;
		long frameControlTime = System.currentTimeMillis();
		
		while(nodesFound < nodesNeeded) {
			arc Arc = this.sortedData.remove();
			if( isCyclic(Arc) ) {
				mazegen.drawArc(Arc.startingNode, Arc.endingNode);
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
	
	private void initNodesVisited() {
		disjointSet = new int[mazegen.max];	
		for(int i = 0; i< mazegen.max; i++) {
			disjointSet[i] = -1;
		}
	}
	
	private arcWeighted[] generateArcs(short width, short height) {
		arcWeighted[] data = new arcWeighted[ 2 * ( width * (height - 1) + height * (width - 1) )];
		int i = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int Coord = (width * y) + x ;
				
				if(x < width - 1) {
					data[i] = new arcWeighted(Coord, Coord + 1 );
					i++;
				}
				if(x > 0) {
					data[i] = new arcWeighted(Coord,  Coord - 1 );
					i++;
				}
				if(y < height - 1) {
					data[i] = new arcWeighted(Coord, Coord + width );
					i++;
				}
				if(y > 0) {
					data[i] = new arcWeighted(Coord, Coord - width );
					i++;
				}				
			}
		}
		return data;
	}
	
	private void sortData(arcWeighted [ ] unsortedData, sortType type) throws Exception {
		kruskalsSortManager sortManager =  new kruskalsSortManager(type, unsortedData);
		this.sortedData = sortManager.sortedData();
	}
	
	@Override
	public void run() {
		long time = System.currentTimeMillis ( );
		System.out.println("Generating arcs...");
		arcWeighted [ ] unsortedData  = generateArcs((short) mazegen.width, (short) mazegen.height);
		System.out.println("Generated in "+(System.currentTimeMillis ( ) - time)+"ms");
		
		try {
			time = System.currentTimeMillis ( );
			System.out.println("Sorting...");
			sortData(unsortedData, type);	
			System.out.println("Sorted in "+(System.currentTimeMillis ( ) - time)+"ms");		
			assert(sortedData.size() != 0);
			initNodesVisited();	
			executeKruskals();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
