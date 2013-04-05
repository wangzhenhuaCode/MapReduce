package mapreduce.node.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mapreduce.sdk.JobConf;

public class Job implements Serializable {
	public static enum JobStatus{JOB_INIT};
	private JobConf conf;
	private JobStatus status;
	private String jobId;
	private transient List<Task> taskList;
	transient Integer workingNode;
	public JobConf getConf() {
		return conf;
	}
	public void setConf(JobConf conf) {
		this.conf = conf;
	}
	public JobStatus getStatus() {
		return status;
	}
	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
	public Job(JobConf conf, JobStatus status, String jobId) {
		super();
		this.conf = conf;
		this.status = status;
		this.jobId = jobId;
		taskList=new ArrayList<Task>();
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public List<Task> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	public Job() {
		taskList=new ArrayList<Task>();
	}
	public Integer getWorkingNode() {
		return workingNode;
	}
	public void setWorkingNode(Integer workingNode) {
		this.workingNode = workingNode;
	}
	public ReduceTask getAvailableReduce(Task task){
		ReduceTask reduce=null;
		for(int i=0;i<taskList.size();i++){
			Task t=taskList.get(i);
			if(task.getTaskId().equals(t.getTaskId())){
				task.setStatus(Task.TaskStatus.END);
			}
			if(t instanceof ReduceTask){
				reduce=(ReduceTask) t;
				if(reduce.getReduceNum()>reduce.getSourceTaskList().size()){
					reduce.getSourceTaskList().add(task);
				}else{
					reduce=null;
				}
			}
		}
		return reduce;
	}
	
	
}
