package mapreduce.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for partitions for one map. It has list of FileSplit
 *
 */
public class InputSplit implements Serializable {
	private List<FileSplit> fileList;

	public List<FileSplit> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileSplit> fileList) {
		this.fileList = fileList;
	}

	public InputSplit() {
		fileList=new ArrayList<FileSplit>();
	}
	
}
