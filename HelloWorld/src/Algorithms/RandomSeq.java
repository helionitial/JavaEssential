package Algorithms;

public class RandomSeq {
	public static void main(String[] args){
		
		//command-line arguments
		int N = Integer.parseInt(args[0]);
		
		if (args.length == 1){
			//generate and print N numbers between 0.0 and 1.0
			for (int i = 0; i < N; i++){
				double x = StdRandom.uniform();
				StdOut.println(x);
			}
		}
		
		else if (args.length == 3){
			double low = Double.parseDouble(args[1]);
			double high = Double.parseDouble(args[2]);
			
			//generate and print N numbers between low and high
			for (int i = 0; i < N; i++){
				double x = StdRandom.uniform(low, high);
				StdOut.printf("%.2f\n", x);
			}
		}
		
		else{
			throw new IllegalArgumentException("Invalid number of arguments");
		}
				
	}
}
