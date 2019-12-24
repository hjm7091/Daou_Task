package Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalysisProgram implements Runnable {

	private final String accessDirectoryPath = "C:\\Users\\user\\Downloads\\Task\\sample";
	private final String minuteDirectoryPath = "C:\\Users\\user\\Downloads\\Task\\minute";
	private final String hourDirectoryPath = "C:\\Users\\user\\Downloads\\Task\\hour";
	private final String dayDirectoryPath = "C:\\Users\\user\\Downloads\\Task\\day";
	
	private final String accessInfoPath = "C:\\Users\\user\\Downloads\\Task\\access_info.txt";
	private final String minuteInfoPath = "C:\\Users\\user\\Downloads\\Task\\minute_info.txt";
	private final String hourInfoPath = "C:\\Users\\user\\Downloads\\Task\\hour_info.txt";
	
	private final AccessAnalyzer accessAnalyzer = new AccessAnalyzer(accessDirectoryPath, minuteDirectoryPath, accessInfoPath);
	private final StatsAnalyzer minuteAnalyzer = new StatsAnalyzer(minuteDirectoryPath, hourDirectoryPath, minuteInfoPath);
	private final StatsAnalyzer hourAnalyzer = new StatsAnalyzer(hourDirectoryPath, dayDirectoryPath, hourInfoPath);

	private boolean stop;
	private boolean first = true;
	
	@Override
	public void run() {
		while(!stop) {
			accessAnalyzer.start(first);
			minuteAnalyzer.start(first);
			hourAnalyzer.start(first);
			if(first) {
				removeOldFiles(minuteDirectoryPath, minuteInfoPath); //오래된 분파일 지움
				removeOldFiles(hourDirectoryPath, hourInfoPath); //오래된 시파일 지움
			}
			first = false;
			System.out.println();
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				System.out.println("종료");
			}
		}
	}
	
	private void removeOldFiles(String directoryPath, String infoPath) {
		Directory directory = new Directory(directoryPath);
		directory.open();
		InfoFile infoFile = new InfoFile(infoPath);
		infoFile.open();
		infoFile.makeInfo();
//		infoFile.printInfo();
		
		ArrayList<String> files = directory.getFilePathes();
		String lastFileName = files.get(files.size()-1);
		lastFileName = lastFileName.substring(lastFileName.lastIndexOf("\\")+1, lastFileName.length()-4);
		for(int idx = files.size()-2; idx>=0; idx--) {
			String otherFileName = files.get(idx);
			otherFileName = otherFileName.substring(otherFileName.lastIndexOf("\\")+1, otherFileName.length()-4);
			if(Math.abs((Long.parseLong(lastFileName) - Long.parseLong(otherFileName))) >= 100) {
				File file = new File(directory.getDirectoryPath() + "\\" + otherFileName + ".txt");
				file.delete();
				infoFile.removeInfo(otherFileName);
			}
		}
//		infoFile.printInfo();
		infoFile.saveLocal();
		
		directory.close();
		infoFile.close();
	}
	
	public static void main(String[] args) {
		AnalysisProgram analysisProgram = new AnalysisProgram();
		Thread analysisThread = new Thread(analysisProgram);
		analysisThread.start();
		System.out.println("종료하려면 end를 입력하세요.");
		Scanner input = new Scanner(System.in);
		while(!analysisProgram.stop) {
			String str = input.next();
			if(str.equals("end")) {
				analysisProgram.stop = true;
				analysisThread.interrupt();
			}
		}
		input.close();
	}

}