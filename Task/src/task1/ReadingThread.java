package task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ReadingThread extends Thread{

	private ArrayList<String> pathes;
	private Map<String, HashSet<String>> dics;
	private Map<String, Long> filePointers; //파일 경로와 파일 경로의 포인터 값 저장
	private String path;
	private boolean state;
	
	protected ReadingThread(String path, ArrayList<String> pathes) {
		this.path = path;
		this.pathes = pathes;
		this.dics = new HashMap<String, HashSet<String>>();
		this.filePointers = new HashMap<String, Long>();
		this.state = true;
	}
	
	public void run() {
		while(state) {
			processFiles();
			System.out.println();
			try {
				sleep(1000 * 10);
			} catch (InterruptedException e) {
				System.out.println("종료");
				this.state = false;
			}
		}
	}
	
	protected void processFiles() {
		boolean change = false;
		for(String filePath : pathes) {
			System.out.println(filePath);
			ReadFile rFile;
			if(filePointers.containsKey(filePath))
				rFile = new ReadFile(filePath, filePointers.get(filePath));
			else
				rFile = new ReadFile(filePath, 0);
			System.out.println("포인터 위치 : " + rFile.getFilePointer()); 
			if(compareFilePointer(filePath, rFile.getFilePointer())) { //파일포인터가 변했으면 변한 부분을 추가해줌
				if(!change)
					change = true;
				System.out.println("바뀜");
				Map<String, HashSet<String>> dic = rFile.getDic();
				for(Map.Entry<String, HashSet<String>> entry : dic.entrySet()) 
					dics.put(entry.getKey(), entry.getValue());
			}
			else
				System.out.println("안바뀜");
		}
		if(change) {//바뀐부분이 있으면 정렬하고 저장
			dics = new MapSort(dics).getDic();
			new SaveOnLocal(dics, path);
		}
		System.out.println("mapInfo");
		for(Map.Entry<String, HashSet<String>> entry : dics.entrySet()) {
			System.out.println("key : " + entry.getKey() + " value : " + entry.getValue() + " SetSize : " + entry.getValue().size());
		}
		
	}
	
	protected boolean compareFilePointer(String path, long nowPointer) {
		if(filePointers.containsKey(path)) {
			long prevPointer = filePointers.get(path);
			if(nowPointer > prevPointer) {
				filePointers.put(path, nowPointer);
				return true;
			}
			else
				return false;
		}
		else {
			filePointers.put(path, nowPointer);
			return true;
		}
	}
}
