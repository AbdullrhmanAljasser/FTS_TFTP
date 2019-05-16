import java.io.*;
import java.util.*;



/*********
 * 
 * @author AyeJay
 * 
 * Class: File Utility 
 * Description: Allows the construction of a 2-d byte array of any files, construction of each layer of the the 2-d array is the 
 * 				the length of MAX_SIZE and last array will include the rest of bytes
 * 
 * Constructors:
 * 	FILEUtil(String): String variable used as the directory of the file to be constructed.
 * 	FILEUtil(byte[][]): 2-d byte array used to recostruct into a file and saved locally.
 * 
 * Methods:
 * 	saveFile(String): Saves 2-d byte array constructed locally as a file
 * 	getData(): Get's 2-d array constructed from a local file
 *
 */



public final class FILEUtil {
	private static int MAX_SIZE = 508;
	private File loadedFile;
	private byte[][] FBreak; //File Break down
	private byte[] sData;
	private int length; //Number of 508 Byte array's
	
	public FILEUtil(String fileDIR) {
		this.loadedFile = new File(fileDIR);  
		length = (int) Math.ceil((double)this.loadedFile.length()/508); //Break down on of the file in 508 packets
		byte[] fByte = new byte[(int) this.loadedFile.length()];
		FileInputStream fStream = null;
		
		try {
			fStream = new FileInputStream(this.loadedFile);
			fStream.read(fByte);
			
			if(length > 1) {
				int i=0;
				
				this.FBreak = new byte[length][];
				for(i=0; i < length -1; i++) {
					this.FBreak[i] = new byte[MAX_SIZE];
					this.FBreak[i] = Arrays.copyOfRange(fByte, (i*MAX_SIZE),((i+1)*MAX_SIZE));
				}
				this.FBreak[length -1] = new byte[(int) (this.loadedFile.length()%MAX_SIZE)];
				this.FBreak[length -1] = Arrays.copyOfRange(fByte, (i*MAX_SIZE),fByte.length);
			} else {
				this.FBreak = new byte[1][];
				this.FBreak[0] = new byte[(int) (this.loadedFile.length()%MAX_SIZE)];
				this.FBreak[0] = Arrays.copyOf(fByte, 0);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found, exiting...");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOException, exiting...");
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if(fStream != null)
					fStream.close();
			} catch (IOException e) {
				System.out.println("IOException at closing File Stream, Exiting...");
				e.printStackTrace();
				System.exit(1);
			}
		}

	}
	
	public FILEUtil(byte[][] incData) {
		int totalSize; //Total size of incoming Data
		
		if(incData.length > 1) {
			totalSize = ((incData.length - 1) * 508) + incData[incData.length-1].length; //Total size of incoming Data
		} else {
			totalSize = incData.length;
		}
		sData = new byte[totalSize];
		for(int i=0,x=0; i < incData.length;i++) {
			for(int z=0; z < incData[i].length;z++,x++) {
				sData[x] = incData[i][z]; //Going through each byte
			}
		}
	}
	
	public byte[][] getData(){
		return this.FBreak;
	}
	public int getLength() {
		return this.length;
	}
	//TODO: DIR must have name file too.
	public void saveFile(String dir) {
		this.loadedFile = new File(dir);
		
		
		
		try(FileOutputStream sFile = new FileOutputStream(this.loadedFile)) {
			if(!this.loadedFile.exists()) {
				this.loadedFile.createNewFile();
			}
			
			sFile.write(sData);
			sFile.flush();
			sFile.close();	
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found, exiting...");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOException at sFile write/close, exiting...");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
