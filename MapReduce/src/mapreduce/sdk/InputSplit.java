package mapreduce.sdk;

import java.util.ArrayList;
import java.util.List;

public class InputSplit {
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
