package task1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ReadFile {

	private ArrayList<String> lines;
	private HashMap<String, HashSet<String>> dic;
	private long filePointer;
	
	protected ReadFile(String path, long pointer) {
		lines = new ArrayList<>();
		dic = new HashMap<String, HashSet<String>>();
		filePointer = pointer;
		processFile(path);
	}
	
	protected void processFile(String path) {
		try {
			RandomAccessFile raFile = new RandomAccessFile(path, "r");
			raFile.seek(filePointer);
			String line = "";
			while((line = raFile.readLine()) != null) {  
				if(!line.equals("")) {
					lines.add(line);
					System.out.println(line);
				}
			}
			filePointer = raFile.getFilePointer();
			StringProcess process = new StringProcess(lines);
			for(String processedLine : process.getLines()) {
				String[] slt = processedLine.split(" ", 2);
				store_on_dic(slt[0], slt[1]);
			}
			raFile.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void store_on_dic(String key, String value) {
		HashSet<String> value_set;
		if(dic.containsKey(key)) {
			value_set = dic.get(key);
			value_set.add(value);
		}
		else {
			value_set = new HashSet<>();
			value_set.add(value);
		}
		dic.put(key, value_set);
	}

	protected HashMap<String, HashSet<String>> getDic() {
		return dic;
	}

	protected long getFilePointer() {
		return filePointer;
	}
	
}
