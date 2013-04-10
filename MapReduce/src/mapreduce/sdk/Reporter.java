package mapreduce.sdk;

import java.io.Serializable;

public interface Reporter extends Serializable {
	public void log(String logInfo);
	public String getLog();
}
