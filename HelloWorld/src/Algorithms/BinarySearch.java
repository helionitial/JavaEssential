package Algorithms;
import java.util.Arrays;
import StandardLibrary.StdIn;;

public class BinarySearch {
	//Precondition: array a[] is sorted
	public static int rank(int key, int[] a){
		int low = 0;
		int high = a.length - 1;
		while(low <= high){
			//Key is in a[low...high] or not present
			int mid = low + (high - low) / 2;
			if(key < a[mid]) high = mid - 1;
			else if(key > a[mid]) low = mid + 1;
			else return mid;
		}
		return -1;
	}
	
	public static void main(String[] args){
		In in = new In(args[0]);
		int[] whiteList = in.readAllInts();
		
		Arrays.sort(whiteList);
		
		// read key; print if not in whiteList
		while(!StdIn.isEmpty()){
			int key = StdIn.readInt();
			if (rank(key, whiteList) == -1)
				StdOut.println(key);
		}
	}
}

