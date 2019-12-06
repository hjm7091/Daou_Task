package task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;

public class SaveOnLocal {
	
	protected SaveOnLocal(Map<String, HashSet<String>> sortedDic, String path) {
		saveUnitMinute(sortedDic, path);
	}
	
	protected void saveUnitMinute(Map<String, HashSet<String>> sortedDic, String path) {
		String newFolder = "resultMinute";
		String newPath = checkLastCharacter(path, newFolder);
		File Folder = new File(newPath);
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더가 생성되었습니다.");
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
	         }else {
			System.out.println("이미 폴더가 생성되어 있습니다.");
		}
		
		try{
			for(Map.Entry<String, HashSet<String>> entry : sortedDic.entrySet()) {
				//System.out.println("key : " + entry.getKey() + " value : " + entry.getValue() + " SetSize : " + entry.getValue().size());
				BufferedWriter fw = new BufferedWriter(new FileWriter(newPath + "\\" + entry.getKey() + ".txt", true));
	            
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
