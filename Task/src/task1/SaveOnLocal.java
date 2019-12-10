package task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;

public class SaveOnLocal {
	
	//private Map<String, Integer> fileInfo;
	
	protected SaveOnLocal(Map<String, HashSet<String>> sortedDic, String path) {
		saveUnitMinute(sortedDic, path);
	}
	
	protected void saveUnitMinute(Map<String, HashSet<String>> sortedDic, String path) {
		String newFolder = "resultMinute";
		String newFolderPath = checkLastCharacter(path, newFolder);
		File Folder = new File(newFolderPath);
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더가 생성되었습니다.");
		    } 
		    catch(Exception e){
			    System.out.println("폴더 생성 오류");
			}
	    }
		else {
			System.out.println("폴더가 이미 존재합니다.");
			File[] deleteFolderList = Folder.listFiles();
			for(int i=0; i<deleteFolderList.length; i++)
				deleteFolderList[i].delete();
		}
		
		try{
			for(Map.Entry<String, HashSet<String>> entry : sortedDic.entrySet()) {
				String newFilePath = newFolderPath + "\\" + entry.getKey() + ".txt";
				BufferedWriter fw = new BufferedWriter(new FileWriter(newFilePath));
	            for(String str : entry.getValue()) {
	            	fw.write(str+"\n");
	            }
	            fw.flush();
	            fw.close();
			}
			System.out.println("저장 완료");
        }catch(Exception e){
            e.printStackTrace();
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
