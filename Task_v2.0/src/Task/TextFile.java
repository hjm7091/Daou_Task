package Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TextFile {
	
	private RandomAccessFile file;
	private String filePath;
	private long pointer;
	private String fileName;
	
	public TextFile(String filePath) {
		this.pointer = -1;
		this.filePath = filePath;
		this.file = null;
		this.fileName = findFileName(filePath);
	}
	
	public void open() {
		try {
			file = new RandomAccessFile(filePath, "r");
		} catch (FileNotFoundException e) {
			System.out.println(fileName + "열기 실패");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			System.out.println(fileName + "닫기 실패");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String readLine(long startPointer) {
		String line = "";
		try {
			file.seek(startPointer);
			line = file.readLine();
			pointer = file.getFilePointer();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("라인 읽기 실패");
			System.exit(1);
		}
		return line;
	}

	public long getPointer() {
		return pointer;
	}
	
	public void setPointer(long pointer) {
		this.pointer = pointer;
	}
	
	public String getFileName() {
		return fileName;
	}

	private String findFileName(String filePath) {
		int lastIdx = filePath.lastIndexOf("\\");
		return filePath.substring(lastIdx+1, filePath.length()-4);
	}
	
}
