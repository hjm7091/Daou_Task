package Task;

public class AccessLineProcessor{
	
	private int ipPos;
	private int timePos;
	private String time;
	private Log log;
	
	public Log processLine(String line) {
		ipPos = -1;
		timePos = -1;
		time = findTime(line);
		log = new Log();
		log.setIp(findIp(line));
		log.setEmail(findEmail(line));
		log.setMethod(findMethod(line));
		log.setUrl(findUrl(line));
		return log;
	}
	
	public String getTime() {
		return time;
	}
	
	private String findTime(String line) {
		int start = line.indexOf("[");
		int end = line.indexOf("]"); 
		if(start == -1 || end == -1)
			return "unknown_time";
		timePos = start;
		line = line.substring(start+1, end);
		String[] separated = line.split(":|/|\\s+");
		return separated[2] + findMonth(separated[1]) + separated[0] + separated[3] + separated[4];
	}
	
	private String findMonth(String m) {
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
	
	private String findIp(String line) {
		String ip = "";
		int idx = 0;
		for(; idx<line.length(); idx++) {
			char nowChar = line.charAt(idx);
			if(isNumber(nowChar) || nowChar=='.')
				ip += nowChar;
			else
				break;
		}
		if(ip.matches(".*[0-9].*")) {
			ipPos = idx - 1;
			return ip;
		}
		else
			return "unknown_ip";
	}
	
	private boolean isNumber(char c) {
		if(c-'0'>=0 && c-'0'<=9)
			return true;
		return false;
	}
	
	private String findEmail(String line) {
		int emailPos = line.indexOf("@");
		if(emailPos!=-1 && emailPos>ipPos && emailPos<timePos) {
			int left = emailPos - 1, right = emailPos + 1;
			while(line.charAt(left)!=' ') 
				left--;
			while(line.charAt(right)!=' ')
				right++;
			return line.substring(left+1, right);
		}
		else
			return "unknown_mail";
	}
	
	private String findMethod(String line) {
		int posTime = line.indexOf("]");
		int posHttp = line.indexOf("HTTP");
		if(posTime == -1 || posHttp == -1)
			return "unknown_method";
		line = line.substring(posTime+2, posHttp);
		String[] separated = line.split("\\s+");
		return separated[0];
	}
	
	private String findUrl(String line) {
		int posTime = line.indexOf("]");
		int posHttp = line.indexOf("HTTP");
		if(posTime == -1 || posHttp == -1)
			return "unknown_url";
		line = line.substring(posTime+2, posHttp);
		String[] separated = line.split("\\s+");
		return separated[1];
	}
	
}
