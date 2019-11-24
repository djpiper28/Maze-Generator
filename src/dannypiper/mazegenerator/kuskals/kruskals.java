package dannypiper.mazegenerator.kuskals;

import java.util.LinkedList;
import java.util.List;

import dannypiper.mazegenerator.gui;
import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.sorting.kruskalsSortManager;
import dannypiper.mazegenerator.kuskals.sorting.sortType;

public class kruskals implements Runnable{
	
	private List<arc> sortedData;
	private sortType type;
	private int[] disjointSet;
	
	public kruskals (sortType type) {
		this.type = type;
	}
	
	private int find(int v) {
		List<Integer> queue = new LinkedList<Integer>();
		while (disjointSet[v] >= 0) {
			queue.add(v);
			v = disjointSet[v];
		}
		for (int i : queue) {
			disjointSet[i] = v;
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
		int i = 0;
		int nodesFound = 0;
		int nodesNeeded = mazegen.width * mazegen.height - 1;
		long frameControlTime = System.currentTimeMillis();
		
		while(nodesFound < nodesNeeded) {
			arc Arc = this.sortedData.get(i);
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
			i++;
		}
	}
	
	private void initNodesVisited() {
		disjointSet = new int[mazegen.max];	
		for(int i = 0; i< mazegen.max; i++) {
			disjointSet[i] = -1;
		}
	}
	
	private List<arcWeighted> generateNodes(int width, int height, List<arcWeighted> data) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int Coord = (width * y) + x;
				
				if(x < width - 1) {
					data.add(new arcWeighted(Coord + 1, Coord));
					data.add(new arcWeighted(Coord, Coord + 1));
				}
				if(x > 0) {
					data.add(new arcWeighted(Coord - 1, Coord));
					data.add(new arcWeighted(Coord, Coord - 1));
				}
				if(y < height - 1) {
					data.add(new arcWeighted(Coord + width, Coord));
					data.add(new arcWeighted(Coord, Coord + width));
				}
				if(y > 0) {
					data.add(new arcWeighted(Coord - width, Coord));
					data.add(new arcWeighted(Coord, Coord - width));
				}				
			}
		}
		return data;
	}
	
	private void sortData(List<arcWeighted> datain, sortType type) throws Exception {
		kruskalsSortManager sortManager =  new kruskalsSortManager(type, datain);
		this.sortedData = sortManager.sortedData();
	}

	@Override
	public void run() {

		List<arcWeighted> unsortedData = new LinkedList<arcWeighted>();
		
		unsortedData = generateNodes(mazegen.width, mazegen.height, unsortedData);
		
		try {
			System.out.println("Sorting...");
			sortData(unsortedData, type);	
			System.out.println("Sorted");		
			assert(sortedData.size() != 0);
			initNodesVisited();	
			executeKruskals();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
