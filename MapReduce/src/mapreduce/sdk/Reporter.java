package mapreduce.sdk;

import java.io.Serializable;

/**
 * Interface for log the information
 *
 */
public interface Reporter extends Serializable {
	/** log the information. System will automatically includes the time and node hostname.
	 * @param logInfo
	 */
	public void log(String logInfo);
	public String getLog();
}
