package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	public static arcWeighted [ ] bubbleSort(arcWeighted [ ] data) {
		boolean finished = false;
		while(!finished) {
			finished = true;
			for(int i = 0; i<data.length ; i++) {
				for(int j = 0;j<data.length; j++) {
					if(data[j].weight < data[i].weight && i < j) {
						arcWeighted temp = data[j];
						data[j] = data[i];
						data[i] = temp;
						finished = false;
					}
				}
			}
		}
		return data;
	}
		//ascending order
	public static List < arc > insertionSort(arcWeighted [ ] data) {
		List<arcWeighted> output = new LinkedList<arcWeighted>();
		
		output.set(0, data[0]); //need an item to compare to.
		
		for(int i = 1; i< data.length; i++) {
			for(int j = 0; j<i ;j++) {
				if(output.get(j).weight <= data[i].weight) {
					output.add(j, data[i]);
					break;
				}
				if(j+1==i) {
					output.set(j+1, data[i]);
				}
			}
		}
		
		List<arc> outputTwo = new LinkedList<arc>();
		
		while(!output.isEmpty ( )) {
			outputTwo.add ( output.remove ( 0 ) );
		}
			
		return outputTwo;
	}
	
	//ascending order
	public static List<arcWeighted> quickSort(List<arcWeighted> data) {
		List<arcWeighted> a = new LinkedList<arcWeighted>();
		List<arcWeighted> b = new LinkedList<arcWeighted>();
		int Pivot = data.size() / 2;
		
		if(data.size() <= 1) {
			return data;
		}	
			
		for(int i = 0; i<data.size(); i++) {			
			if(i!=Pivot) {
				if(data.get(Pivot).weight <= data.get(i).weight) {
					a.add(data.get(i));
				} else {
					b.add(data.get(i));
				}
			} 
		}
			
		a = quickSort(a);		
		b = quickSort(b);
			
		//merge
		a.add(data.get(Pivot));
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
	
	
	public static List<arc> countingSort(arcWeighted [ ] data, int maxRand) {
		@SuppressWarnings("unchecked")
		Queue<arc>[] categories = new LinkedList[maxRand];
		
		for(int i = 0; i< mazegen.maxRand; i++) {
			categories[i] = new LinkedList<arc>();
		}
		
		for(arcWeighted currentArc: data) {
			categories[currentArc.weight].add(currentArc);
		}
		
		List<arc> output = new LinkedList<arc>();
		
		for(int i = 0; i < maxRand; i++){
			while(! categories[i].isEmpty ( )) {
				output.add(categories[i].remove ( ));
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
