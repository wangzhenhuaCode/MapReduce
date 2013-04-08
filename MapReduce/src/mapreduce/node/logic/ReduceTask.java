package mapreduce.node.logic;

import java.util.ArrayList;
import java.util.List;

public class ReduceTask extends Task {

	
	private Integer reduceNum;
	public ReduceTask(String jobId, String taskId, Integer reduceNum) {
		super(jobId, taskId);
		this.reduceNum=reduceNum;
		sourceTaskList=new ArrayList<Task>();
	}
	private ArrayList<Task> sourceTaskList;
	public Integer getReduceNum() {
		return reduceNum;
	}
	public void setReduceNum(Integer reduceNum) {
		this.reduceNum = reduceNum;
	}
	public ArrayList<Task> getSourceTaskList() {
		return sourceTaskList;
	}
	public void setSourceTaskList(ArrayList<Task> sourceTaskList) {
		this.sourceTaskList = sourceTaskList;
	}
	

}
