package task1;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFolder {
	
	protected ReadFolder(String folderPath) {
		final File folder = new File(folderPath);
		ArrayList<String> pathes = listFilesForFolder(folder, folderPath);
		ReadingThread rThread = new ReadingThread(folderPath, pathes);
		rThread.start();
		boolean state = true;
		System.out.println("종료하려면 end를 입력하세요.");
		Scanner input = new Scanner(System.in);
		while(state) {
			String str = input.next();
			if(str.equals("end")) {
				rThread.interrupt();
				state = false;
			}
		}
		input.close();
	}
	
	protected ArrayList<String> listFilesForFolder(final File folder, String path) {
		ArrayList<String> pathes = new ArrayList<>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	if(fileEntry.getName().contains("result")) //결과 폴더는 읽지 않음
	        		continue;
	        	String now_path = checkLastCharacter(path, fileEntry.getName());
	            pathes.addAll(listFilesForFolder(fileEntry, now_path));
	        } else {
	            String fileName = fileEntry.getName();
	            int lastIdx = fileName.lastIndexOf(".");
	            String extender = fileName.substring(lastIdx+1, fileName.length());
	            if(extender.equals("txt")) { //폴더안의 텍스트 파일만 저장
	            	String now_path = checkLastCharacter(path, fileName);
	            	pathes.add(now_path);
	            }
	        }
	    }
	    return pathes;
	}
	
	protected String checkLastCharacter(String path, String directoryName) {
		if(path.charAt(path.length()-1)=='\\')
    		path += directoryName;
    	else
    		path += "\\" + directoryName;
		return path;
	}
	
}
