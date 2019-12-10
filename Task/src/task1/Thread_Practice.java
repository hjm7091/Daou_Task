package task1;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

class MyThread extends Thread{
	
	private String path;
	private boolean state = true;
	private long pos = -1;
	
	public MyThread(String path) {
		this.path = path;
	}
	
	public void run() {
		while(state) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
				String new_path = get_path(path);
				BufferedWriter out = new BufferedWriter(new FileWriter(new_path, true));
				if(pos == -1) {
					String line = "";
					while((line = randomAccessFile.readLine()) != null) {
						System.out.println(line);
					}
					long now_pos = randomAccessFile.length();
					System.out.println("파일 크기 : " + now_pos);
					byte[] data = new byte[(int)now_pos];
					randomAccessFile.seek(0);
					randomAccessFile.read(data);
					out.write("처음 내용 : " + new String(data));
					out.newLine();
					out.flush();
					pos = now_pos;
				}
				else {
					String line = "";
					while((line = randomAccessFile.readLine()) != null) {
						System.out.println(line);
					}
					long now_pos = randomAccessFile.length();
					System.out.println("파일 크기 : " + now_pos);
					long diff = now_pos - pos;
					if(diff > 0) {
						System.out.println("크기가 커졌습니다.");
						byte[] data = new byte[(int) diff]; 
						randomAccessFile.seek(now_pos - diff);
						randomAccessFile.read(data);
						out.write("새로운 내용 : " + new String(data));
						out.newLine();
						out.flush();
					}
					else if(now_pos == pos)
						System.out.println("크기가 그대로입니다.");
					pos = now_pos;
				}
			} catch (FileNotFoundException e) {
				System.out.println("파일을 찾을 수 없습니다.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(1000 * 5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("종료");
				state = false;
			}
		}
	}
	
	public String get_path(String path) {
		int last = path.lastIndexOf("\\");
		return path.substring(0, last+1) + "result.txt";
	}
}

public class Thread_Practice {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("파일 경로 입력 : ");
		String path = input.next();
		MyThread t = new MyThread(path);
		t.start();
		boolean flag = true;
		System.out.println("종료하려면 end를 입력하세요.");
		while(flag) {
			String s = input.next();
			if(s.equals("end")) {
				t.interrupt();
				flag = false;
			}
		}
	}

}
