package task1;

import java.util.ArrayList;

public class StringProcess {
	
	private ArrayList<String> lines;
	
	protected StringProcess(ArrayList<String> lines) {
		this.lines = processing(lines);
	}
	
	protected ArrayList<String> processing(ArrayList<String> lines) {
		ArrayList<String> processedList = new ArrayList<>();
		for(String line : lines) {
			String realTime = cal_realTime(line);
			if(realTime.equals("error"))
				continue;
			String processedTime = cal_processedTime(realTime);
			String ip = cal_ip(line);
			if(ip.equals("- - -"))
				ip = "unknown_ip";
			String mail = cal_mail(line, ip, realTime);
			String[] method_url = cal_method_url(line);
			String key = processedTime;
			String value = ip + " " + mail + " " + method_url[0] + " " + method_url[1]; 
			line = key + " " + value;
			processedList.add(line);
		}
		return processedList;
	}
	
	protected String[] cal_method_url(String line) {
		int pos_time = line.indexOf("]");
		int pos_http = line.indexOf("HTTP");
		String s = line.substring(pos_time+2, pos_http);
		String[] str = s.split("\\s+");
		return str;
	}
	
	protected String cal_mail(String line, String ip, String realTime) {
		int pos_ip = line.indexOf(ip);
		int pos_time = line.indexOf(realTime);
		int pos_mail = line.indexOf("@");
		if(pos_mail!=-1 && pos_mail>pos_ip && pos_mail<pos_time) {
			int left = pos_mail - 1, right = pos_mail + 1;
			while(line.charAt(left)!=' ') 
				left--;
			while(line.charAt(right)!=' ')
				right++;
			return line.substring(left+1, right);
		}
		else
			return "unknown_mail";
	}
	
	protected String cal_ip(String line) {
		String ip = "";
		for(int idx=0; idx<line.length(); idx++) {
			char nowChar = line.charAt(idx);
			if(isNumber(nowChar) || nowChar=='.')
				ip += nowChar;
			else
				break;
		}
		if(ip.matches(".*[0-9].*"))
			return ip;
		else
			return "- - -";
	}
	
	protected boolean isNumber(char c) {
		if(c-'0'>=0 && c-'0'<=9)
			return true;
		return false;
	}
	
	protected String cal_realTime(String line) {
		int start = line.indexOf("[");
		int end = line.indexOf("]"); 
		if(start == -1 || end == -1)
			return "error";
		line = line.substring(start, end+1);
		return line;
	}
	
	protected String cal_processedTime(String line) {
		line = line.substring(1, line.length());
		String[] str = line.split(":|/|\\s+");
		String month = cal_month(str[1]);
		return str[2]+month+str[0]+str[3]+str[4];
	}
	
	protected String cal_month(String m) {
		if(m.equals("Jan"))
			return "01";
		if(m.equals("Feb"))
			return "02";
		if(m.equals("Mar"))
			return "03";
		if(m.equals("Apr"))
			return "04";
		if(m.equals("May"))
			return "05";
		if(m.equals("Jun"))
			return "06";
		if(m.equals("Jul"))
			return "07";
		if(m.equals("Aug"))
			return "08";
		if(m.equals("Sep"))
			return "09";
		if(m.equals("Oct"))
			return "10";
		if(m.equals("Nov"))
			return "11";
		if(m.equals("Dec"))
			return "12";
		return "unknown_month";
	}

	protected ArrayList<String> getLines() {
		return lines;
	}
	
}
