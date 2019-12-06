package task1;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("폴더 경로를 입력하세요 : ");
		String folderPath = input.next();
		ReadFolder rFolder = new ReadFolder(folderPath);
		
	}

}