package mapreduce.sdk;

import java.io.Serializable;

public class FileSplit implements Serializable {

	private String path;
	private long start;
	private long end;
	
	public FileSplit(String path, long start, long end) {
		super();
		this.path = path;
		this.start = start;
		this.end = end;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	
}
