package task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class ReadFile {

	private ArrayList<String> lines = new ArrayList<>();
	private HashMap<String, HashSet<String>> dic;
	
	protected ReadFile(String path) {
		dic = new HashMap<String, HashSet<String>>();
		processFile(path);
	}
	
	protected void processFile(String path) {
		try {
			File file = new File(path);
			System.out.println(file.getName());
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while((line = bufReader.readLine()) != null) {  //(line = bufReader.readLine()) != null
				//System.out.println(line);
				lines.add(line);
			}
			bufReader.close();
			StringProcess process = new StringProcess(lines);
			for(String processedLine : process.getLines()) {
				//System.out.println(processedLine);
				String[] slt = processedLine.split(" ", 2);
				store_in_dic(slt[0], slt[1]);
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void store_in_dic(String key, String value) {
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

}
