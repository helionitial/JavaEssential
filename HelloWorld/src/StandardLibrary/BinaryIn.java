package StandardLibrary;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public final class BinaryIn {
	private static final int EOF = -1;			// end of file
	
	private BufferedInputStream in;				// the input stream
	private int buffer;							// one character buffer
	private int N;								// number of bits left in buffer
	
	/**
	 * Create a binary input stream from standard input.
	 */
	public BinaryIn(){
		in = new BufferedInputStream(System.in);
		fillBuffer();
	}
	
	/**
	 * Create a binary input stream from an InputStream.
	 */
	public BinaryIn(InputStream is){
		in = new BufferedInputStream(is);
		fillBuffer();
	}
	
	/**
	 * Create a binary input stream from a socket.
	 */
	public BinaryIn(Socket socket){
		try{
			InputStream is = socket.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}catch(IOException ioe){
			System.err.println("Could not open" + socket);
		}
	}
	
	/**
	 * Create a binary input stream from a URL.
	 */
	public BinaryIn(URL url){
		try{
			URLConnection site = url.openConnection();
			InputStream is = site.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}catch(IOException ioe){
			System.err.println("Could not open" + url);
		}
	}
	
	/**
	 * Create a binary input stream from a filename or URL name.
	 */
	public BinaryIn(String s){
		
		try{
			// first try to read file from local file system
			File file = new File(s);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				in = new BufferedInputStream(fis);
				fillBuffer();
				return;
			}
			
			// next try for files included in jar
			URL url = getClass().getResource(s);
			
			// or URL from web
			if( url == null) {
				url = new URL(s);
			}
			
			URLConnection site = url.openConnection();
			InputStream is = site.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}catch(IOException ioe){
			System.err.println("Could not open" + s	);
		}		
	}
	private void fillBuffer(){
		try{
			buffer = in.read();
			N = 8;
		}catch (IOException e){
			System.err.println("EOF");
			buffer = EOF;
			N = -1;
		}
	}
	
	/**
	 * Does the binary input stream exist?
	 */
	public boolean exists(){
		return in != null;
	}
	
	/**
	 * Returns true if the binary input stream is empty.
	 * @return true if and only if the binary input stream is empty
	 */
	public boolean isEmpty(){
		return buffer == EOF;
	}
	
	/**
	 * Read the next bit of data from the binary input stream and return as 
	 * a boolean.
	 * @return the next bit of data from the binary input stream as a boolean
	 * @throws RuntimeException if the input stream is empty
	 */
	public boolean readBoolean(){
		if(isEmpty()){
			throw new RuntimeException("Reading from empty input stream");
		}
		N--;
		boolean bit = ((buffer >> N) & 1) == 1;
		if(N == 0) fillBuffer();
		return bit;
	}
	
	/**
	 * Read the next 8 bits from the binary input stream and return as an 8-bit char.
	 * @return the next 8 bits of data from the binary input stream as a char.
	 * @throws RuntimeException if there are fewer than 8 bits available.
	 */
	public char readChar(){
		if (isEmpty()) throw new RuntimeException("Reading from empty input stream!");
		
		// special case when aligned byte
		if(N == 8){
			int x = buffer;
			fillBuffer();
			return (char) (x & 0xff);
		}
		
		// combine last N bits of current buffer with first 8-N bits of new buffer
		int x = buffer;
		x <<= (8 - N);
		int oldN = N;
		fillBuffer();
		if(isEmpty()) throw new RuntimeException("Reading from empty input stream");
		N = oldN;
		x |= (buffer >>> N);
		return (char) (x & 0xff);
		// the above code doesn't quite work for the last character if N = 8
		// because buffer will be -1
	}

	
	/**
	 * Read the next r bits from the binary input stream and return as an r-bit character.
	 * @param r number of bits to read.
	 * @return the next r bits of data from the binary input stream as a char
	 * @throws RuntimeException if there are fewer than r bits available
	 */
	public char readChar(int r){
		if (r < 1 || r > 16) throw new RuntimeException("Illegal value of r = " + r );
		
		// optimize r = 8 case
		if (r == 8 ) return readChar();
		
		char x = 0;
		for (int i = 0; i < r; i ++){
			x <<= 1;
			boolean bit = readBoolean();
			if (bit) x |= 1;
		}
		return x;
	}
	
	/**
	 * Read the remaining butes of data from the binary input stream and return as a string.
	 * @return the remaining bytes of data from the binary input stream as a String.
	 * @throws RuntimeException if the input stream is empty or if the number of bits 
	 * available is not a multiple of 8(byte--aligned).
	 */
	public String readString(){
		if (isEmpty()) throw new RuntimeException("Reading from empty input stream");
		
		StringBuilder sb = new StringBuilder();
		while(!isEmpty()){
			char c = readChar();
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Read the next 16 bits from the binary input stream and return as a 16-bit short.
	 * @return the next 16 bits of data from the binary standard input as a short.
	 * @throws RuntimeException if there are fewer than 16 bits available
	 */
	public short readShort(){
		short x = 0;
		for(int i = 0; i < 2; i ++){
			char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}
	
	/**
	 * Read the next 32 bits from the binary input stream and return as a 32-bits int.
	 * @return the next 32 bits of data from the binary input stream as a int.
	 * @throws RuntimeException if there are fewer than 32 bits available.
	 */
	public int readInt(){
		int x = 0;
		for ( int i = 0; i < 4; i ++){
			char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}
	
	/**Read the next r bits from the binary input stream return as an r-bit int.
	 * @param r number of bits to read.
	 * @return the next r bits of data from the binary input stream as a int.
	 * @throws RuntimeException if there are fewer than r bits available on standard input.
	 */
	public int readInt(int r){
		
	}
}
