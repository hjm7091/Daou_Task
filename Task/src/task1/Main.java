package task1;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("���� ��θ� �Է��ϼ��� : ");
		String folderPath = input.next();
		ReadFolder rFolder = new ReadFolder(folderPath);
		
	}

}