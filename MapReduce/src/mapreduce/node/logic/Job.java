package mapreduce.node.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mapreduce.sdk.JobConf;

public class Job implements Serializable {
	public static enum JobStatus{JOB_INIT, JOB_FINALL_STATE,JOB_FINISHED};
	private JobConf conf;
	private JobStatus status;
	private String jobId;
	private transient List<Task> taskList;

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

	public ReduceTask getAvailableReduce(Task task){
		ReduceTask reduce=null;
		Integer unfinished=0;
		
		Boolean added=false;
		for(int i=0;i<taskList.size();i++){
			Task t=taskList.get(i);
			if(t.getStatus().equals(Task.TaskStatus.BEGIN)){
				if(task.getTaskId().equals(t.getTaskId())){
					task.setStatus(Task.TaskStatus.END);
					unfinished--;
				}
				if(t instanceof ReduceTask){
					
					reduce=(ReduceTask) t;
			
					if(!added){
						if(reduce.getReduceNum()>reduce.getSourceTaskList().size()){
							reduce.getSourceTaskList().add(task);
							added=true;
						
						}else{
							reduce=null;
						}
					}
				}
				
				unfinished++;
			}
			
		}
		if(unfinished==1&&added){
			this.status=JobStatus.JOB_FINALL_STATE;
		
			
		}
		return reduce;
	}
	
	
}
