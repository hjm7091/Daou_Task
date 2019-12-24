package Task;

import java.io.File;
import java.util.ArrayList;

public class Directory {
	
	private File directory;
	private String directoryPath;
	
	public Directory(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	public void open() {
		directory = new File(directoryPath);
		if(!directory.exists()) {
			directory.mkdirs();
			System.out.println(directory.getName() + "생성됨");
		}
		else
			System.out.println(directory.getName() + "폴더 열기 성공");
	}
	
	public void close() {
		directory = null;
	}
	
	public ArrayList<String> getFilePathes() {
		ArrayList<String> pathes = new ArrayList<>();
	    for (File fileEntry : directory.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	        	String fileName = fileEntry.getName();
	            int lastIdx = fileName.lastIndexOf(".");
	            String extender = fileName.substring(lastIdx+1, fileName.length());
	            if(extender.equals("txt")) { //폴더안의 텍스트 파일만 저장
	            	String filePath = checkLastCharacter(directoryPath, fileName);
	            	pathes.add(filePath);
	            }
	        }
	    }
	    return pathes;
	}
	
	private String checkLastCharacter(String path, String directoryName) {
		if(path.charAt(path.length()-1)=='\\')
    		path += directoryName;
    	else
    		path += "\\" + directoryName;
		return path;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}
	
}
