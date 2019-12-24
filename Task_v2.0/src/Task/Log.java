package Task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(exclude = "count")
@ToString
public class Log {
	
	private String ip;
	private String email;
	private String method;
	private String url;
	private int count = 1;
	
}
