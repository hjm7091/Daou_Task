package Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccessAnalyzer{
	
	private Directory sourceDirectory;
	private Directory targetDirectory;
	private InfoFile sourceInfo;
	private TextFile accessFile;
	private HashMap<String, ArrayList<Log>> tmpStorage;
	
	private AccessLineProcessor lineProcessor = new AccessLineProcessor();
	
	private boolean first;
	
	public AccessAnalyzer(String sourceDirectoryPath, String targetDirectoryPath, String sourceInfoPath) {
		this.sourceDirectory = new Directory(sourceDirectoryPath);
		this.targetDirectory = new Directory(targetDirectoryPath);
		this.sourceInfo = new InfoFile(sourceInfoPath);
		this.accessFile = new TextFile(sourceInfoPath);
		this.tmpStorage = new HashMap<>();
	}
	
	public void start(boolean flag) {
		
		first = flag;
		
		sourceDirectory.open();
		targetDirectory.open();
		sourceInfo.open();
		sourceInfo.makeInfo();
		
		//source파일 분석하여 target파일 생성
		for(String sourceFilePath : sourceDirectory.getFilePathes()) {
			accessFile = new TextFile(sourceFilePath);
			long startPointer = sourceInfo.getStartPointer(accessFile.getFileName());
			readFile(startPointer);
			sourceInfo.updateInfo(accessFile.getFileName(), accessFile.getPointer());
			sourceInfo.saveLocal();
		}
		
		sourceDirectory.close();
		targetDirectory.close();
		sourceInfo.close();
	}
	
	private void readFile(long startPointer) {
		accessFile.open();
//		System.out.println(accessFile.getFileName());
		String line = "";
		while(line!=null) {
			line = accessFile.readLine(startPointer);
			if(line!=null) {
				Log log = lineProcessor.processLine(line);
				String lineTime = lineProcessor.getTime();
				if((Long.parseLong(getCurrentDate()) > Long.parseLong(lineTime))) { //현재-1분에 대해서만 저장한다.
					//저장공간이 크기가 0이 아니고 읽어들인 시간이 저장공간에 존재하지 않으면 저장공간에 저장되어 있는 이전 정보를 로컬에 저장하고 비워준다.
					if(tmpStorage.size()!=0 && !tmpStorage.containsKey(lineTime)) { 
						processStorage();
					}
					updateLog(lineTime, log); //저장공간 업데이트 해준다.
					startPointer = accessFile.getPointer();
				}
				else {
					accessFile.setPointer(startPointer);
					break;
				}
			} 
		}
		if(tmpStorage.size()!=0) { //마지막에 저장공간에 있는 정보를 로컬에 저장한다.
			processStorage();
		}
		accessFile.close();
	}
	
	private void processStorage() {
		sortLog();
		saveLogLocal();
		tmpStorage.clear();
	}
	
	private void saveLogLocal() {
		try {
			for(Map.Entry<String, ArrayList<Log>> logMap : tmpStorage.entrySet()) {
				String fileName = logMap.getKey();
				ArrayList<Log> logList = logMap.getValue();
				String newPath = targetDirectory.getDirectoryPath() + "\\" + fileName + ".txt";
				BufferedWriter fw = new BufferedWriter(new FileWriter(newPath));
				for(int idx=0; idx<logList.size(); idx++) {
					Log log = logList.get(idx);
					fw.write(log.getCount() + " " + log.getIp() + " " + log.getEmail() + " " + log.getMethod() + " " + log.getUrl() + "\n");
				}
				fw.flush();
				fw.close();
				if(!first)
					removeOldFile(fileName);
			}
			printStorage();
		}catch(Exception e) {
			System.out.println("쓰기 실패");
			System.exit(1);
		}
	}
	
	private void updateLog(String time, Log log) {
		boolean same = false;
		ArrayList<Log> newLogList = new ArrayList<>();
		if(tmpStorage.containsKey(time)) {
			ArrayList<Log> oldLogList = tmpStorage.get(time);
			for(int idx=0; idx<oldLogList.size(); idx++) {
				Log nowLog = oldLogList.get(idx);
				if((log.hashCode() == nowLog.hashCode()) && log.equals(nowLog)) {
					nowLog.setCount(nowLog.getCount()+1);
					same = true;
				}
				newLogList.add(nowLog);
			}
			if(!same)
				newLogList.add(log);
		}
		else {
			newLogList.add(log);
		}
		tmpStorage.put(time, newLogList);
	}
	
	private void sortLog(){
		for(ArrayList<Log> logList : tmpStorage.values()) {
			Collections.sort(logList, new Comparator<Log>() {
				@Override
				public int compare(Log o1, Log o2) {
					return (o1.getCount() - o2.getCount()) * -1;
				}
			});
		}
	}
	
	private String getCurrentDate() {
		SimpleDateFormat formatMinute = new SimpleDateFormat("yyyyMMddHHmm");
		Date curDate = new Date();
		return formatMinute.format(curDate);
	}
	
	private void printStorage() {
		for(Map.Entry<String, ArrayList<Log>> log : tmpStorage.entrySet()) {
			System.out.print(log.getKey() + " ");
			for(int idx=0; idx<log.getValue().size(); idx++) {
				System.out.print(log.getValue().get(idx).toString() + " ");
			}
			System.out.print("size:" + log.getValue().size());
			System.out.println();	
		}
	}
	
	private void removeOldFile(String fileName) {
		int lastIdx = sourceDirectory.getDirectoryPath().lastIndexOf("\\");
		String infoPath = sourceDirectory.getDirectoryPath().substring(0, lastIdx+1) + "minute_info.txt";
		InfoFile infoFile = new InfoFile(infoPath);
		infoFile.open();
		infoFile.makeInfo();
		for(String targetFileName : targetDirectory.getFilePathes()) {
			targetFileName = targetFileName.substring(targetFileName.lastIndexOf("\\")+1, targetFileName.length()-4);
			if(Math.abs((Long.parseLong(fileName) - Long.parseLong(targetFileName))) >= 100) {
				File file = new File(targetDirectory.getDirectoryPath() + "\\" + targetFileName + ".txt");
				file.delete();
				infoFile.removeInfo(targetFileName);
			}
		}
		infoFile.saveLocal();
		infoFile.close();
	}
}
