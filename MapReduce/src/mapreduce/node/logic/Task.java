package mapreduce.node.logic;

import java.io.Serializable;

public class Task implements Serializable {
	public enum Integer{TASK_MAP_BEGIN, TASK_MAP_END, TASK_REDUCE_BEGIN, TASK_REDUCE_END}
	
	private Integer jobId;
	private Integer status;
	
}
