package Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InfoFile {
	
	private File infoFile;
	private String infoFilePath;
	private HashMap<String, Long> info = new HashMap<>();
	
	public InfoFile(String infoFilePath) {
		this.infoFilePath = infoFilePath;
	}
	
	public void open() {
		infoFile = new File(infoFilePath);
		if(!infoFile.exists()) {
			try {
				infoFile.createNewFile();
			} catch (IOException e) {
				System.out.println(infoFile.getName() + "생성 실패");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else
			System.out.println(infoFile.getName() + "열기 성공");
		
	}
	
	public void close() {
		info.clear();
		infoFile = null;
	}
	
	public void saveLocal() {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(infoFilePath));
			for(Map.Entry<String, Long> entry : info.entrySet()) {
	            fw.write(entry.getKey()+" "+entry.getValue()+"\n");
			}
			fw.flush();
            fw.close();
		} catch(Exception e) {
			System.out.println(infoFile.getName() + "저장 실패");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void makeInfo() {
		try {
			FileReader fileReader = new FileReader(infoFile);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line = "";
			while((line = bufReader.readLine())!=null) {
				String[] separated = line.trim().split(" ");
				info.put(separated[0], Long.parseLong(separated[1]));
			}
			bufReader.close();
			System.out.println(infoFile.getName() + "맵 만들기 성공");
		} catch (IOException e) {
			System.out.println(infoFile.getName() + "맵 만들기 실패");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void printInfo() {
		System.out.println(this.info);
	}
	
	public void updateInfo(String fileName, long pointer) {
		info.put(fileName, pointer);
	}
	
	public void removeInfo(String fileName) {
		info.remove(fileName);
	}
	
	public long getStartPointer(String filePath) {
		if(info.containsKey(filePath)) 
			return info.get(filePath);
		else
			return 0;
	}
	
	public String getFileName() {
		return infoFile.getName();
	}
	
}
