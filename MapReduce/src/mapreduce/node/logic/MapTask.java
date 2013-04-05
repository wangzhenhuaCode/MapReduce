package mapreduce.node.logic;

import mapreduce.sdk.InputSplit;

public class MapTask extends Task {
	public MapTask(String jobId, String taskId) {
		super(jobId, taskId);
		// TODO Auto-generated constructor stub
	}
	private InputSplit inputSplit;
	private String reduceNodeHost;
	private Integer reduceNodePort;
	public InputSplit getInputSplit() {
		return inputSplit;
	}
	public void setInputSplit(InputSplit inputSplit) {
		this.inputSplit = inputSplit;
	}
	
	
}
