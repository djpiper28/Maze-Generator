package dannypiper.mazegenerator.kuskals.sorting;

import java.util.LinkedList;
import java.util.List;
import dannypiper.mazegenerator.mazegen;
import dannypiper.mazegenerator.kuskals.arc;
import dannypiper.mazegenerator.kuskals.arcWeighted;

public class kruskalsSortManager {
	
	public sortType type;
	public List<arcWeighted> data;
	
	public kruskalsSortManager(sortType type, List<arcWeighted> data) {
		this.type = type;
		this.data = data;
	}
	
	public List<arc> sortedData() throws Exception{
		if(this.type != sortType.countingSort) {
			List<arcWeighted> sortedData = new LinkedList<arcWeighted>(); 
			switch(this.type) {
			case countingSort:
				System.out.println("ERROR at line 25");
				throw new Exception("Assertion error at line 25");
			case bubbleSort:
				sortedData = sortingAlgorithms.bubbleSort(this.data);
				break;
			case insersionSort:
				sortedData = sortingAlgorithms.insertionSort(this.data);
				break;
			case quickSort:
				sortedData = sortingAlgorithms.quickSort(this.data);
				break;
			default:
				System.out.println("ERROR at line 36");
				throw new Exception("Assertion error at line 36");		
			}	
			return postSortUtils.arcWeightedListToArcList(sortedData);
		} else {
			return sortingAlgorithms.countingSort(this.data, mazegen.maxRand);
		}
	}
}
