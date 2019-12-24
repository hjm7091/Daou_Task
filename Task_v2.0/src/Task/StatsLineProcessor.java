package Task;

public class StatsLineProcessor {

	private String time;
	private Log log;
	
	public Log processLine(String line) {
		String[] separated = line.trim().split(" ");
		time = cutBehindCharacter(separated[0]);
		log = new Log();
		log.setCount(Integer.parseInt(separated[1]));
		log.setIp(separated[2]);
		log.setEmail(separated[3]);
		log.setMethod(separated[4]);
		log.setUrl(separated[5]);
		return log;
	}

	public String getTime() {
		return time;
	}
	
	private String cutBehindCharacter(String line) {
		return line.substring(0, line.length()-2);
	}

}
