package task1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class ReadFolder {
	
	private ArrayList<String> pathes = new ArrayList<>();
	private Map<String, HashSet<String>> dics;
	
	protected ReadFolder(String path) {
		dics = new HashMap<String, HashSet<String>>();
		final File folder = new File(path);
		listFilesForFolder(folder, path);
		processFiles(path);
	}
	
	protected void processFiles(String path) {
		for(String p : pathes) {
			String filePath = p;
			ReadFile rFile = new ReadFile(filePath);
			Map<String, HashSet<String>> dic = rFile.getDic();
			for(Map.Entry<String, HashSet<String>> entry : dic.entrySet()) 
				dics.put(entry.getKey(), entry.getValue());
			
		}
		System.out.println("mapInfo");
		Map<String, HashSet<String>> sortedDic = new MapSort(dics).getDic();
		for(Map.Entry<String, HashSet<String>> entry : sortedDic.entrySet()) {
			System.out.println("key : " + entry.getKey() + " value : " + entry.getValue() + " SetSize : " + entry.getValue().size());
		}
		System.out.println(path);
		Scanner input = new Scanner(System.in);
		System.out.println("1:저장O 2:저장X");
		String save = input.next();
		if(save.equals("1")) {
			SaveOnLocal saveonlocal = new SaveOnLocal(sortedDic, path);
		}
	}
	
	protected void listFilesForFolder(final File folder, String path) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	String now_path = checkLastCharacter(path, fileEntry.getName());
	            listFilesForFolder(fileEntry, now_path);
	        } else {
	            String fileName = fileEntry.getName();
	            int lastIdx = fileName.lastIndexOf(".");
	            String extender = fileName.substring(lastIdx+1, fileName.length());
	            if(extender.equals("txt")) { //폴더안의 텍스트 파일만 저장
	            	String now_path = checkLastCharacter(path, fileName);
	            	//System.out.println(fileName + " " + now_path);
	            	pathes.add(now_path);
	            }
	        }
	    }
	}
	
	protected String checkLastCharacter(String path, String directoryName) {
		if(path.charAt(path.length()-1)=='\\')
    		path += directoryName;
    	else
    		path += "\\" + directoryName;
		return path;
	}
	
}
