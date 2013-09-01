package StandardLibrary;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BinaryStdOut {
	private static BufferedOutputStream out = new BufferedOutputStream(System.out);
	
	private static int buffer;				// 8-bit buffer of bits to write out
	private static int N;					// number of bits remaining in buffer
	
	// don't instantiate
	private BinaryStdOut(){}
	
	/**
	 * Write the specified bit to standard outpu.
	 */
	private static void writeBit(boolean bit){
		//add bit to buffer
		buffer <<= 1;
		if (bit) buffer |= 1;
		
		// if buffer is full (8 bits), write out as a single byte
		N++;
		if(N == 8) clearBuffer();
	}
	
	/**
	 * Write the 8-bit byte to standard output.
	 */
	private static void writeByte(int x){
		assert x >= 0 && x < 256;
		
		// optimized if byte-aligned
		if (N == 0){
			try{
				out.write(x);
			}catch(IOException e){
				e.printStackTrace();
			}
			return;
		}
		
		// otherwise write one bit at a time
		for(int i = 0; i < 8; i++){
			boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}
	
	// write out any remaining bits in buffer to standard output, padding with 0s
	private static void clearBuffer(){
		if (N == 0) return;
		if (N > 0) buffer <<= (8 = N);
		try{
			out.write(buffer);
		}catch(IOException e){
			e.printStackTrace();
		}
		N = 0;
		buffer = 0;
	}
	
	/**
	 * Flush standard output, padding 0s if number of bits written so fat
	 * is not a multiple of 8.
	 */
	public static void flush(){
		clearBuffer();
		try{
			out.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Flush and close standard output. Once standard output is closed, you can no
	 * longer write bits to it.
	 */
	public static void close(){
		flush();
		try{
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Write the specified bit to standard outpu.
	 * @param x the boolean to write.
	 */
	public static void write(boolean x){
		writeBit(x);
	}
	
	/**
	 * Write the 8-bit byte to standard output/
	 * @param x the byte to write.
	 */
	public static void write(byte x){
		writeByte(x & 0xff);
	}
	
	/**
	 * Write the 32-bit int to standard output.
	 * @param x the int to write.
	 */
	public static void write(int x){
		writeByte((x >>> 24) & 0xff);
		writeByte((x >>> 16) & 0xff);
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}
	
	/**
	 * Write the r-bit int to standard outpu.
	 * @param x the int to write.
	 * @param r the number of relevant bits in the char.
	 * @throws RuntimeException if r is not bewteen 1 and 32.
	 * @throws RuntimeException if x is not bewteen 0 and 2 r - 1.
	 */
	public static void write(int x, int r){
		if(r == 32) { 
			write(x);
			return;
		}
		if(r < 1 || r > 32) throw new RuntimeException("Illegal value for r = " + r);
		if(x < 0 || x >= (1 << r)) throw new RuntimeException("Illegal" + r + "-bit char =" + x);
		for(int i = 0; i < r; i++){
			boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}
	
	/**
	 * Write the 64-bit double to standard output.
	 * @param x the double to write.
	 */
	public static void write(double x){
		write(Double.doubleToRawLongBits(x));
	}
	
	/**
	 * Write the 64-bit long to standard output.
	 * @param x the long to write.
	 */
	public static void write(long x){
		writeByte((int) ((x >>>56) & 0xff));
        writeByte((int) ((x >>> 48) & 0xff));
        writeByte((int) ((x >>> 40) & 0xff));
        writeByte((int) ((x >>> 32) & 0xff));
        writeByte((int) ((x >>> 24) & 0xff));
        writeByte((int) ((x >>> 16) & 0xff));
        writeByte((int) ((x >>>  8) & 0xff));
        writeByte((int) ((x >>>  0) & 0xff));
	}
	
	/**
	 * Write the 32-bit float to standard output.
	 * @param x the float to write.
	 */
	public static void write(float x){
		write(Float.floatToRawIntBits(x));
	}
	
	/**
	 * Write the 16-bit int to standard output.
	 * @param x the short to write.
	 */
	public static void write(short x){
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}
	
	/**
	 * Write the 8-bit char to standard output.
	 * @param x the char to write.
	 * @throws RuntimeException if x is not bewteen 0 and 255.
	 */
	public static void write(char x){
		if ( x < 0 || x >= 256) throw new RuntimeException("Illegal 8-bit char =" + x);
		writeByte(x);
	}
	
	/**
	 * Write the r-bit char to standard output.
	 * @param x the char to write.
	 * @param r the number of relevant bits in the char.
	 * @throws RuntimeException if r is not between 1 and 16.
	 * @throws RuntimeException if x is not between 0 and 2 r -1
	 */
	public static void write(char x, int r){
		if(r == 8) {
			write(x);
			return;
		}
		if(r < 1 || r > 16) throw new RuntimeException("Illegal value for r = " + r);
		if(x < 0 || x >= (1 << r)) throw new RuntimeException("Illegal " + r + "-bit char =" + x);
		for(int i = 0; i < r; i++){
			boolean bit = ((x >>> ( r - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}
	
	/**
	 * Write the String of r-bit characters to standard output.
	 * @param s the String to write.
	 * @param r the number of relevants bits in each character.
	 * @throws RuntimeException if r is not between 1 and 16.
	 * @throws RuntimeException if any character in the string is
	 * not between 0 and 2 r -1.
	 */
	public static void write(String s, int r){
		for ( int i = 0; i < s.length(); i ++){
			write(s.charAt(i), r);
		}
	}
	
	/**
	 * Test client.
	 */
	public static void main(String[] args){
		int T = Integer.parseInt(args[0]);
		// write to standard output
		for( int i = 0; i < T; i++){
			BinaryStdOut.write(i);
		}
		BinaryStdOut.flush();
	}
}
