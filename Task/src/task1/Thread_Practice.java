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
					System.out.println("���� ũ�� : " + now_pos);
					byte[] data = new byte[(int)now_pos];
					randomAccessFile.seek(0);
					randomAccessFile.read(data);
					out.write("ó�� ���� : " + new String(data));
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
					System.out.println("���� ũ�� : " + now_pos);
					long diff = now_pos - pos;
					if(diff > 0) {
						System.out.println("ũ�Ⱑ Ŀ�����ϴ�.");
						byte[] data = new byte[(int) diff]; 
						randomAccessFile.seek(now_pos - diff);
						randomAccessFile.read(data);
						out.write("���ο� ���� : " + new String(data));
						out.newLine();
						out.flush();
					}
					else if(now_pos == pos)
						System.out.println("ũ�Ⱑ �״���Դϴ�.");
					pos = now_pos;
				}
			} catch (FileNotFoundException e) {
				System.out.println("������ ã�� �� �����ϴ�.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(1000 * 5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("����");
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
		System.out.print("���� ��� �Է� : ");
		String path = input.next();
		MyThread t = new MyThread(path);
		t.start();
		boolean flag = true;
		System.out.println("�����Ϸ��� end�� �Է��ϼ���.");
		while(flag) {
			String s = input.next();
			if(s.equals("end")) {
				t.interrupt();
				flag = false;
			}
		}
	}

}
