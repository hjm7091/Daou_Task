package Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StatsAnalyzer{
	
	private Directory sourceDirectory;
	private Directory targetDirectory;
	private InfoFile sourceInfo;
	private TextFile file;
	private HashMap<String, ArrayList<Log>> tmpStorage;
	
	private StatsLineProcessor lineProcessor = new StatsLineProcessor();
	
	private boolean first;

	public StatsAnalyzer(String sourceDirectoryPath, String targetDirectoryPath, String sourceInfoPath) {
		this.sourceDirectory = new Directory(sourceDirectoryPath);
		this.targetDirectory = new Directory(targetDirectoryPath);
		this.sourceInfo = new InfoFile(sourceInfoPath);
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
			file = new TextFile(sourceFilePath);
			long startPointer = sourceInfo.getStartPointer(file.getFileName());
			readFile(startPointer);
			sourceInfo.updateInfo(file.getFileName(), file.getPointer());
		}
		
		sourceInfo.saveLocal();
		
		if(tmpStorage.size()!=0) { //마지막에 저장공간에 있는 정보를 로컬에 저장한다.
			processStorage();
		}
		
		sourceDirectory.close();
		targetDirectory.close();
		sourceInfo.close();
		
	}
	
	private void readFile(long startPointer) {
		file.open();
//		System.out.println(file.getFileName());
		String line = "";
		while(line!=null) {
			line = file.readLine(startPointer);
			if(line!=null) {
				Log log = lineProcessor.processLine(file.getFileName() + " " + line);
				String time = lineProcessor.getTime();
				//저장공간이 크기가 0이 아니고 읽어들인 시간이 저장공간에 존재하지 않으면 저장공간에 저장되어 있는 이전 정보를 로컬에 저장하고 비워준다.
				if(tmpStorage.size()!=0 && !tmpStorage.containsKey(time)) { 
					processStorage();
				}
				updateLog(time, log); //저장공간 업데이트 해준다.
				startPointer = file.getPointer();
			} 
		}
		file.close();
	}
	
	private void processStorage() {
		if(first) {
			sortLog();
			saveLogLocal();
		}
		else {
			collectLocal();
			sortLog();
			saveLogLocal();
		}
		tmpStorage.clear();
//		try {
//			Thread.sleep(1000 * 3);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void collectLocal() {
		try {
			for(Map.Entry<String, ArrayList<Log>> logMap : tmpStorage.entrySet()) {
				String fileName = logMap.getKey();
				String newPath = "";
				if(fileName.length()==8)
					newPath = targetDirectory.getDirectoryPath() + "\\" + convertName(fileName) + ".txt";
				else {
					newPath = targetDirectory.getDirectoryPath() + "\\" + fileName + ".txt";
					resetPointer(fileName);
				}
				File file = new File(newPath);
				if(file.exists()) {
					FileReader fileReader = new FileReader(file);
					BufferedReader bufReader = new BufferedReader(fileReader);
					String line = "";
					while((line = bufReader.readLine())!=null) {
						String[] separated = line.trim().split(" ");
						Log log = new Log();
						log.setCount(Integer.parseInt(separated[0]));
						log.setIp(separated[1]);
						log.setEmail(separated[2]);
						log.setMethod(separated[3]);
						log.setUrl(separated[4]);
	//					System.out.println("log : " + log.toString());
						updateLog(fileName, log);
					}
					bufReader.close();
				}
			}
		}
		catch(Exception e) {
			System.out.println("읽기 실패");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void resetPointer(String fileName) {
		int lastIdx = sourceDirectory.getDirectoryPath().lastIndexOf("\\");
		String infoPath = sourceDirectory.getDirectoryPath().substring(0, lastIdx+1) + "hour_info.txt";
//		System.out.println("infoPath:" + infoPath);
		InfoFile infoFile = new InfoFile(infoPath);
		infoFile.open();
		infoFile.makeInfo();
		infoFile.updateInfo(fileName, 0);
		infoFile.saveLocal();
		infoFile.close();
//		try {
//			Thread.sleep(1000 * 100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	
	private void saveLogLocal() {
		try {
			for(Map.Entry<String, ArrayList<Log>> logMap : tmpStorage.entrySet()) {
				String fileName = logMap.getKey();
				if(fileName.length()==8)
					fileName = convertName(fileName);
				ArrayList<Log> logList = logMap.getValue();
				String newPath = targetDirectory.getDirectoryPath() + "\\" + fileName + ".txt";
				BufferedWriter fw = new BufferedWriter(new FileWriter(newPath));
				for(int idx=0; idx<logList.size(); idx++) {
					Log log = logList.get(idx);
					fw.write(log.getCount() + " " + log.getIp() + " " + log.getEmail() + " " + log.getMethod() + " " + log.getUrl() + "\n");
				}
				fw.flush();
				fw.close();
				if(!first && !fileName.contains("stat"))
					removeOldFile(fileName);
			}
			printStorage();
		}catch(Exception e) {
			System.out.println("쓰기 실패");
			e.printStackTrace();
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
					nowLog.setCount(nowLog.getCount() + log.getCount());
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

	private void sortLog() {
		for(ArrayList<Log> logList : tmpStorage.values()) {
			Collections.sort(logList, new Comparator<Log>() {
				@Override
				public int compare(Log o1, Log o2) {
					return (o1.getCount() - o2.getCount()) * -1;
				}
			});
		}
	}
	
	private String convertName(String fileName) {
		int len = fileName.length();
		String yyyy = fileName.substring(0, len-4);
		String MM = fileName.substring(len-4, len-2);
		String dd = fileName.substring(len-2, len);
		return "stat_" + yyyy + "-" + MM + "-" + dd;
	}
	
	private void removeOldFile(String fileName) {
		int lastIdx = sourceDirectory.getDirectoryPath().lastIndexOf("\\");
		String infoPath = sourceDirectory.getDirectoryPath().substring(0, lastIdx+1) + "hour_info.txt";
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
