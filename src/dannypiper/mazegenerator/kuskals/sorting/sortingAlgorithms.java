package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.arc;
import dannypiper.mazegenerator.kuskals.arcWeighted;

public class sortingAlgorithms {
	
	private static List<arcWeighted> swap(int indexA, int indexB, List<arcWeighted> data) {
		arcWeighted temp = data.get(indexA);
		data.set(indexA, data.get(indexB));
		data.set(indexB, temp);
		return data;
	}
			
	//ascending order
	public static List<arcWeighted>bubbleSort(List<arcWeighted> data) {
		boolean finished = false;
		while(!finished) {
			finished = true;
			for(int i = 0; i<data.size() ; i++) {
				for(int j = 0;j<data.size(); j++) {
					if(data.get(j).weight < data.get(i).weight && i < j) {
						data = swap(i, j, data);
						finished = false;
					}
				}
			}
		}
		return data;
	}
		//ascending order
	public static List<arcWeighted> insertionSort(List<arcWeighted> Data) {
		List<arcWeighted> output = new LinkedList<arcWeighted>();
		
		output.set(0, Data.get(0)); //need an item to compare to.
		
		for(int i = 1; i< Data.size(); i++) {
			for(int j = 0; j<i ;j++) {
				if(output.get(j).weight <= Data.get(i).weight) {
					output.add(j, Data.get(i));
					break;
				}
				if(j+1==i) {
					output.set(j+1, Data.get(i));
				}
			}
		}
			
		return output;
	}
	
	//ascending order
	public static List<arcWeighted> quickSort(List<arcWeighted> Data) {
		List<arcWeighted> a = new LinkedList<arcWeighted>();
		List<arcWeighted> b = new LinkedList<arcWeighted>();
		int Pivot = Data.size() / 2;
		
		if(Data.size() <= 1) {
			return Data;
		}	
			
		for(int i = 0; i<Data.size(); i++) {			
			if(i!=Pivot) {
				if(Data.get(Pivot).weight <= Data.get(i).weight) {
					a.add(Data.get(i));
				} else {
					b.add(Data.get(i));
				}
			} 
		}
			
		a = quickSort(a);		
		b = quickSort(b);
			
		//merge
		a.add(Data.get(Pivot));
		a.addAll(b);	
	
		return a;
	}

	
	public static List<arcWeighted> quickSortThreaded(List<arcWeighted> data){
		
		quickSortThread a = new quickSortThread ( data );
		(new Thread(a)).start ( );
		
		while(!a.finished) {
			
		}	
		
		return a.data;
	}
	
	
	public static List<arc> countingSort(List<arcWeighted> arcs, int maxRand) {
		@SuppressWarnings("unchecked")
		List<arcWeighted>[] categories = new LinkedList[maxRand];
		
		for(int i = 0; i< mazegen.maxRand; i++) {
			categories[i] = new LinkedList<arcWeighted>();
		}
		
		for(arcWeighted currentArc: arcs) {
			categories[currentArc.weight].add(currentArc);
		}
		
		List<arc> output = new LinkedList<arc>();
		
		for(List<arcWeighted> category: categories) {
			for(arcWeighted Arc: category) {
				output.add(Arc);
			}
		}
		
		return output;
	}
}
class quickSortThread implements Runnable{

	public List<arcWeighted> data;
	public boolean finished;
	
	public quickSortThread(List<arcWeighted> data) {
		this.data = data;
		this.finished = false;
	}
	
	@Override
	public void run ( ) {
		this.data = quickSort(data);		
		this.finished = true;
	}

	//ascending order
	private List<arcWeighted> quickSort(List<arcWeighted> Data) {
		List<arcWeighted> a = new LinkedList<arcWeighted>();
		List<arcWeighted> b = new LinkedList<arcWeighted>();
		int Pivot = Data.size() / 2;
		
		if(Data.size() <= 1) {
			return Data;
		}	
			
		for(int i = 0; i<Data.size(); i++) {			
			if(i!=Pivot) {
				if(Data.get(Pivot).weight <= Data.get(i).weight) {
					a.add(Data.get(i));
				} else {
					b.add(Data.get(i));
				}
			} 
		}
		
		quickSortThread aObj = new quickSortThread(a);
		quickSortThread bObj = new quickSortThread(b);
		
		(new Thread(aObj)).start ( );
		(new Thread(bObj)).start ( );
		
		while(!(aObj.finished && bObj.finished)) {
			
		}
			
		a = aObj.data;		
		b = bObj.data;	
			
		//merge
		a.add(Data.get(Pivot));
		a.addAll(b);	
	
		return a;
	}	
	
}
